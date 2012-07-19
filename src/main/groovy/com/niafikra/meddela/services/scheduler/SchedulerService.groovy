package com.niafikra.meddela.services.scheduler

import it.sauronsoftware.cron4j.Scheduler
import org.apache.log4j.Logger
import com.niafikra.meddela.data.Notification
import it.sauronsoftware.cron4j.InvalidPatternException
import com.niafikra.meddela.meddela
import org.neodatis.odb.ODBRuntimeException

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
     * the scheduler will check run a trigger check to see it needs to run the scheduler or
     * not
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

            updateNotification(notification)

            return true;
        } catch (InvalidPatternException ex) {
            log.error("failed to schedule notification ${notification.name}", ex)
            return false

        } catch (ODBRuntimeException){
            log.info("failed to update the notification with new information", ex)
        }
    }

    /**
     * Removes the notification from the scheduler
     *
     * @param notification
     */
    void deScheduleNotification(Notification notification){
        scheduler.deschedule(notification.schedulerId)
        notification.schedulerId = null
        updateNotification(notification)
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

    /**
     * Updates a notification in the database.
     * It reads a notification with similar name and description as the passed notification
     * then it updates its schedulerId and saves it.
     *
     * this method does not do anything if the notification doesn't exist in the db
     * @param notification
     * @return
     */
    def updateNotification(Notification notification){
        try{
            Notification dbNotification = meddela.database.getObjectsByPropertiesAND(Notification,
                    [
                            name:notification.name,
                            description:notification.description
                    ])

            if(dbNotification){
                dbNotification.schedulerId = notification.schedulerId
                meddela.database.getODB().store(dbNotification)
            }

        } catch (ODBRuntimeException ex){
            log.info("failed to update notification in database")
        }
    }
}
