package com.niafikra.meddela.services.scheduler

import it.sauronsoftware.cron4j.Scheduler
import org.apache.log4j.Logger
import com.niafikra.meddela.data.Notification
import it.sauronsoftware.cron4j.InvalidPatternException

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
     * Schedule a notification, when the schedule time for the notification reaches
     * the scheduler will check run a trigger check to see.
     *
     * @param notification
     * @return true if succesfull, false otherwise
     */
    def scheduleNotification(Notification notification) {
        try {
            TriggerCheckTask triggerCheckTask = new TriggerCheckTask();
            def id = scheduler.schedule(notification.trigger.schedule, triggerCheckTask)

            triggerCheckTask.id = id
            notification.schedulerId = id

            return true;
        } catch (InvalidPatternException ex) {
            log.error("failed to schedule notification ${notification.name}", ex)
            return false
        }
    }

    /**
     * Removes the notification from the scheduler
     *
     * @param notification
     */
    void deScheduleNotification(Notification notification){
        scheduler.deschedule(notification.schedulerId)

    }

    /**
     * Reschedules the notification, it assumes that the notification
     * was once scheduled before, therefore it has schedulerId
     * @param notification
     */
    void reScheduleNotification(Notification notification){
        scheduler.reschedule(notification.schedulerId, notification.trigger.schedule)
    }

    /**
     * Stop the scheduler service
     */
    def stop() {
        scheduler.stop()
        log.info("scheduler stopped succesfully")
    }
}
