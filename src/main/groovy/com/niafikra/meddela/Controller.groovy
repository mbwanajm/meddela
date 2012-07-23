package com.niafikra.meddela

import com.niafikra.meddela.meddela as Meddela

import com.niafikra.meddela.data.security.Authentication
import javax.servlet.ServletContextListener
import org.apache.log4j.Logger

/**
 *
 * This act as a controller for meddella web application
 * by managing when to start or stop meddella
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/18/12
 * Time: 8:12 AM
 */
class Controller implements ServletContextListener {

    def static meddela
    def static appPath
    def static appPathExt
    Logger log = Logger.getLogger(Controller)

    @Override
    void contextInitialized(javax.servlet.ServletContextEvent servletContextEvent) {

        setPath(servletContextEvent.servletContext.getRealPath(""))
        meddela = new meddela()
        meddela.init(appPath)
        addDefaultUsers()
    }

    void addDefaultUsers() {
        if (!Meddela.authenticationManager.authExist("admin")) {
            Authentication admin = new Authentication()
            admin.setUsername("admin")
            admin.setPassword("admin")
            admin.setEnabled(true)
            Meddela.authenticationManager.addAuthentication(admin)
            log.info("Added default users")
        }
        else log.info("Default users exists")
    }

    void setPath(String path) {
        appPath = path
        appPathExt = new StringBuilder(appPath)
                .append(File.separator).append("WEB-INF")
                .append(File.separator).append("classes")
                .toString();
    }

    @Override
    void contextDestroyed(javax.servlet.ServletContextEvent servletContextEvent) {
        meddela.stop()
    }
}
