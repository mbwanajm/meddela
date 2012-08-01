package com.niafikra.meddela.services

import com.niafikra.meddela.data.SentNotification
import com.niafikra.meddela.services.transport.Transport

import com.niafikra.meddela.meddela
import org.neodatis.odb.ODB
import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.data.UniqueRecepient
import groovy.io.FileType
import com.niafikra.meddela.services.transport.TransporClasstLoader

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
    TransporClasstLoader transClassLoader
    String transportsPath

    TransportManager() {
        transportsPath=meddela.appPath+File.separator+"transport"
        initTransportLoader()
    }

    private def initTransportLoader() {
        ArrayList<URL> urls = []

        File transDir = new File(transportsPath)
        if (transDir.exists()) {
            transDir.eachFile(FileType.FILES,{ urls << it.toURL() })
        }
        else transDir.mkdir()

        urls << transDir.toURL()      //a little hack to initia a URL array
        transClassLoader=new TransporClasstLoader(urls.toArray(new URL[1]),this.class.classLoader)

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
        Transport transport = getTransport("com.niafikra.meddela.services.transport.ConsoleTransport")
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


    def getTransport(String name) {
        Transport transport = loadedTransport.get(name)
        if (!transport) {
            transport=loadTransport(name)
        }
        return transport
    }

    Transport loadTransport(String name) {
        Class clazz = transClassLoader.loadClass(name);
        Transport transport = clazz.newInstance();
        loadedTransport.put(name, transport)
        return transport
    }

    def testTransport(String name){
        try{
            return transClassLoader.loadClass(name).newInstance()      //groovy truth
        }catch(Exception e){
            e.printStackTrace()
            return false
        }
    }

    def addTransport(File transportJar) {
        transClassLoader.addTransportURL(transportJar.toURL())
      //  Transport transport=transClassLoader.loadClass()
    }
    
    Set listAvailableTransport(){
        def trans=[] as Set
        File transDir = new File(transportsPath)
        if (transDir.exists()) {
            transDir.eachFile(FileType.FILES,{
                trans<< it.name.replace(".jar","")
            })
        }
        return trans
    }
}
