package com.niafikra.meddela

import com.niafikra.meddela.auth.AuthenticationManager
import org.apache.log4j.Logger
import org.apache.log4j.PropertyConfigurator
import com.niafikra.meddela.services.ObjectDatabase
import com.niafikra.meddela.services.scheduler.SchedulerService

import com.niafikra.meddela.services.TransportManager
import com.niafikra.meddela.services.composer.Composer
import com.niafikra.meddela.services.NotificationManager
import com.niafikra.meddela.services.scheduler.TriggerCheck
import com.niafikra.meddela.services.xchange.XChangeService

/**
 * This class provides a facade to  the services that
 * provide the meddella functionality.
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/17/12
 * Time: 2:09 PM
 */
class meddela {
    private static Logger log = Logger.getLogger(meddela.class)
    static ObjectDatabase database                  // used to manipulate meddela's local db
    static SchedulerService scheduler               // used to schedule tasks
    static Composer composer;                       // used to compose notification messages
    static TransportManager transportManager        // used to send out messages
    static NotificationManager notificationManager  // used for CRUD actions on notifications
    static String appPath                           // this path must be set by the object that initializes medella
    static AuthenticationManager authenticationManager
    static TriggerCheck triggerCheck                //used to check if the notification should be triggered
    static XChangeService xChangeService            // used to export and import notifications
    /**
     * Initialize medella
     *
     * @param pathToApp is the path to where medella is running from
     * @return true if medella starts succesfully false otherwise
     */
    static boolean init(String pathToApp) {
        appPath = pathToApp
        initLog()

        try {
            database = new ObjectDatabase()
            scheduler = new SchedulerService()
            transportManager = new TransportManager()
            composer = new Composer()
            notificationManager = new NotificationManager()
            authenticationManager=new AuthenticationManager()
            triggerCheck=new TriggerCheck()
            xChangeService = new XChangeService();

            log.info("meddella successsfully started ")
            return true
        } catch (Exception ex) {
            log.info("meddella failed to start", ex)
            return false
        }
    }

    static def initLog() {
      /*
        String pathToSettings = new StringBuilder(appPath)
                .append(File.separator).append("config")
                .append(File.separator).append("log4j.properties")
                .toString();  */
        Properties props=new Properties()
        props.put("log4j.rootLogger","info, A, R")
        props.put("log4j.appender.A" ,"org.apache.log4j.ConsoleAppender")
        props.put "log4j.appender.A.layout","org.apache.log4j.PatternLayout"
        props.put("log4j.appender.A.layout.ConversionPattern","%d %-5p %c - %m%n")
        props.put("log4j.appender.R","org.apache.log4j.RollingFileAppender")
        props.put("log4j.appender.R.File"," meddela.log")
        props.put("log4j.appender.R.MaxFileSize","100KB")
        props.put("log4j.appender.R.MaxBackupIndex",1)
        props.put("log4j.appender.R.layout","org.apache.log4j.PatternLayout")
        props.put("log4j.appender.R.layout.ConversionPattern","%d %-5p %c - %m%n")

        PropertyConfigurator.configure(props);
    }

    /**
     * Shutdown medella
     */
    static void stop() {
        database.close()
        scheduler.stop()
        log.info("meddella successsfully stopped ")
    }

}
