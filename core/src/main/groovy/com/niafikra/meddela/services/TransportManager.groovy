package com.niafikra.meddela.services

import com.niafikra.meddela.data.SentNotification
import com.niafikra.meddela.services.transport.Transport

import com.niafikra.meddela.meddela
import org.neodatis.odb.ODB
import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.data.UniqueRecepient
import groovy.io.FileType

import com.niafikra.meddela.data.TransportInfo
import org.apache.commons.io.FileUtils
import org.apache.log4j.Logger

import com.niafikra.meddela.services.transport.TransportClassLoader

/**
 * This class coordinates the delivering of notifications and saves
 * the results of the action
 *
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 15:52
 */
class TransportManager {
    HashMap loadedTransport = new HashMap()
    TransportClassLoader transClassLoader
    String transportsPath
    private static final Logger log = Logger.getLogger(TransportManager)

    TransportManager() {
        transportsPath = System.getProperty("user.home") + File.separator + "meddela" + File.separator + "transport"
        log.info("set transport plugin path as: $transportsPath")
        initTransportLoader()
    }

    /**
     * initiate transport plugin for the meddela to be loaded dynamically per need
     * @return
     */
    private def initTransportLoader() {
        ArrayList<URL> urls = []

        File transDir = new File(transportsPath)
        if (transDir.exists()) {
            log.info('transport plugin dir exists! yeah..')
            transDir.eachFile(FileType.FILES, {
                urls << it.toURL()
                log.info("loaded transport at :${it}")
            })
        }
        else {
            log.info("could not find transport plugin dir, hence creating one at $transportsPath")
            transDir.mkdir()
        }

        urls << transDir.toURL()      //a little hack to initia a URL array
        transClassLoader = new TransportClassLoader(urls.toArray(new URL[1]), this.class.classLoader)

    }

    /**
     * Send out the specified notification
     *
     * Note that this will use the composer to get all the notifications that are to be sent out
     * then it will send each one of them saving the status to the db
     *
     * @param notification
     */
    def sendNotification(Notification notification) {
        Transport transport = getTransport(notification.transport.name)
        def notificationsToSend = meddela.composer.compose(notification)

        for (SentNotification sentNotification in notificationsToSend) {
            sentNotification.time = new Date()
            sentNotification.delivered = transport.sendNotification(sentNotification)

            meddela.database.runDbAction { ODB odb ->
                // store the sent notification
                odb.store(sentNotification)

                // if the recepient is not in the unique recepients list add him
                def recepients = meddela.database
                        .getObjectsByProperty(UniqueRecepient, 'recepient', sentNotification.receiver)

                if (recepients?.isEmpty()) {
                    odb.store(new UniqueRecepient(sentNotification.receiver))
                }
            }

        }
    }

    /**
     * Get a transport identified by the name
     * @param name name of the transport to be loaded which is also the name of the Transport class
     * @return
     */
    def getTransport(String name) {
        Transport transport = loadedTransport.get(name)
        if (!transport) {
            transport = loadTransport(name)
        }
        return transport
    }

    /**
     * Load a requested trandport plugin
     * @param name name of the plugin to be loaded
     * @return
     */
    Transport loadTransport(String name) {
        Class clazz = transClassLoader.loadClass(name);
        Transport transport = clazz.newInstance();
        loadedTransport.put(name, transport)
        return transport
    }

    /**
     * test a a plugin if follows meddela transport plugin convention
     * @param name name of the plugin
     * @return
     */
    def testTransport(String name) {
        try {
            Class clazz = transClassLoader.loadClass(name)
            Transport transport = clazz.newInstance()
            return true//groovy truth
        } catch (Exception e) {
            log.error("failed to test transport $name", e)
            return false
        }
    }

    /**
     * Add a transport plugin to running meddela  (hot install)
     * @param transportJar
     */
    def addTransport(File transportJar) {
        transClassLoader.addTransportURL(transportJar.toURL())
        //  TransportInfo transport=transClassLoader.loadClass()
    }

    /**
     * Save global info on the Transport plugin
     * @param transportInfo
     * @return
     */
    def saveTransportInfo(TransportInfo transportInfo) {

        return meddela.database.runDbAction {ODB odb ->
            def results = meddela.database.getObjectsByProperty(TransportInfo, 'name', transportInfo.name)
            if (results) {
                meddela.database.update(transportInfo, 'name')
            } else {
                odb.store(transportInfo)
            }
        }
    }

    /**
     * Remove installed Transport plugin
     * @param transportInfo
     * @return
     */
    def removeTransport(TransportInfo transportInfo) {
        def result = meddela.database.runDbAction {ODB odb ->
            def dbTransportInfo = meddela.database.getObjectByProperty(TransportInfo, 'name', transportInfo.name)
            if (dbTransportInfo)
                odb.delete(dbTransportInfo)
        }

        if (result) {
            File pluginjar = new File(transportsPath + File.separator + transportInfo.name + ".jar")
            try {
                pluginjar.delete()
                return true
            } catch (Exception ex) {
                log.error("failed to delete ${transportInfo.name} plugin jar", ex)
            }
        }

        return false
    }

    /**
     * installs a transport plugin uploaded to a meddelea transport plugin directory
     * @param filename name of the file containing the plugin
     * @return true if installation is successfully, i.e the file is a jar and contains java classes following the pattern of
     *                                                     meddela plugins
     */
    boolean installTransportPlugin(String filename) {
        File pluginjar = new File(transportsPath + File.separator + filename)
        log.info("attempting to install transport plugin from ${pluginjar.absolutePath}")
        addTransport(pluginjar)
        if (testTransport(filename.replace(".jar", ""))) {  //test if contain a meddela transport plugin

            TransportInfo transportinfo = new TransportInfo()
            transportinfo.name = filename.replace(".jar", "")
            //initiate the global configurations for the transport
            Transport transport = getTransport(transportinfo.name)
            transportinfo.configurations = transport.getGlobalConfigurations()
            saveTransportInfo(transportinfo)
            log.info("installed $transportinfo.name transport plugin succesfully")
            return true
        } else {
            FileUtils.deleteQuietly(pluginjar)
            return false
        }
    }

    Collection listAvailableTransport() {
        /*   def trans=[] as Set

File transDir = new File(transportsPath)
if (transDir.exists()) {
    transDir.eachFile(FileType.FILES,{
        trans<< it.name.replace(".jar","")
    })
}

return trans       */

        return meddela.database.getAll(TransportInfo)
    }
}
