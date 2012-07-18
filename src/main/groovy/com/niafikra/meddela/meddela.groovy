package com.niafikra.meddela

import org.apache.log4j.Logger
import org.apache.log4j.PropertyConfigurator
import com.niafikra.meddela.services.Database

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/17/12
 * Time: 2:09 PM
 */

class meddela {
    private static Logger  log =Logger.getLogger(meddela.class)
    static Database database

    def init() {
        try{
            initLog()
            database = new Database();

            log.info("meddella successsfully started ")
        } catch (Exception ex){
            log.info("meddella failed to start", ex)
        }
    }

    def initLog() {

            String pathToSettings = new StringBuilder(Controller.getAppPathExt())
                    .append(File.separator).append("config")
                    .append(File.separator).append("log4j.properties")
                    .toString();

            PropertyConfigurator.configure(pathToSettings);

    }

    void stop() {
        database.close()
        log.info("meddella successsfully stopped ")
    }
}
