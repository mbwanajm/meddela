package com.niafikra.meddela

import javax.servlet.ServletContextListener

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

    @Override
    void contextInitialized(javax.servlet.ServletContextEvent servletContextEvent) {

        setPath(servletContextEvent.servletContext.getRealPath(""))
        meddela = new meddela()
        meddela.init(appPath)
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
