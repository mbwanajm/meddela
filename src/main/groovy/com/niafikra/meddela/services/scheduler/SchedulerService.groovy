package com.niafikra.meddela.services.scheduler

import it.sauronsoftware.cron4j.Scheduler
import org.apache.log4j.Logger

/**
 *  This can be used to add notifications so that they are scheduled to be run at sometime specified
 *  in their triggers
 *
 * @author mbwana jaffari mbura
 * Date: 18/07/12
 * Time: 20:17
 */
class SchedulerService {
    Scheduler scheduler;
    private static final Logger log = Logger.getLogger(SchedulerService)

    SchedulerService() {
        scheduler = new Scheduler()
        scheduler.start()
        log.info("scheduler started succesfully")
    }

    /**
     * Stop the scheduler service
     */
    def stop(){
        scheduler.stop()
        log.info("scheduler stopped succesfully")
    }
}
