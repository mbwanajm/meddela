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
        scheduleEnabledNotifications()
        log.info("scheduler started succesfully")
    }

    def scheduleEnabledNotifications() {
        meddela.database.runDbQuery {
            def notifications = meddela.database.getObjectsByProperty(Notification, 'enabled', true)

            if (!notifications) return

            for (notification in notifications) {
                scheduleNotification(notification)
            }
        }
    }

    /**
     * Schedule a notification, when the schedule time for the notification reaches
     * the scheduler will check run a trigger check to see it needs to run the scheduler or
     * not
     *
     * @param notification
     * @return true if succesfull, false otherwise
     */
    boolean scheduleNotification(Notification notification) {
        if (!notification.isEnabled()) return true
        try {
            TriggerCheckTask triggerCheckTask = new TriggerCheckTask();
            def id = scheduler.schedule(notification.trigger.schedule, triggerCheckTask)

            triggerCheckTask.id = id
            notification.schedulerId = id

            updateNotification(notification)

            return true;
        } catch (InvalidPatternException ex) {
            log.error("failed to schedule notification ${notification.name}, invalid cron pattern", ex)
            return false

        }
    }

    /**
     * Removes the notification from the scheduler
     *
     * @param notification
     */
    void deScheduleNotification(Notification notification) {
        scheduler.deschedule(notification.schedulerId)
        notification.schedulerId = null
        updateNotification(notification)
    }

    /**
     * Reschedules the notification, it assumes that the notification
     * was once scheduled before, therefore it has schedulerId
     * @param notification
     */
    void reScheduleNotification(Notification notification) {
        if (scheduler.getTask(notification.schedulerId)) {
            scheduler.reschedule(notification.schedulerId, notification.trigger.schedule)
        } else {
            scheduleNotification(notification)
        }
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
    def updateNotification(Notification notification) {

        meddela.database.runDbAction { odb ->
            Notification dbNotification = meddela.database.getObjectByProperty(Notification, 'name', notification.name)

            if (dbNotification) {
                dbNotification.schedulerId = notification.schedulerId
                odb.store(dbNotification)
            }
        }
    }

}
