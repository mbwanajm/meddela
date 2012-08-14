package com.niafikra.meddela.services

import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.meddela
import org.neodatis.odb.ODB

/**
 * This object can be used to add notifications,
 * enable and disable them
 *
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 16:32
 */
class NotificationManager {

    /**
     * Adds a notification to the db and schedules it
     *
     * @param notification
     * @return true if succesful, false otherwise
     */
    boolean addNotification(Notification notification) {
        if (meddela.database.runDbAction {ODB odb -> odb.store(notification)}) {
            return meddela.scheduler.scheduleNotification(notification)
        }

        return false;
    }

    /**
     * Delete the particular notification from the db,
     * if the notification was active then remove it from the scheduler
     * @param notification
     * @return true if succesful, false otherwise
     */
    boolean deleteNotification(Notification notification) {
        meddela.scheduler.deScheduleNotification(notification)
        meddela.database.runDbAction {ODB odb ->
            Notification  readNotification = meddela.database.getObjectByProperty(Notification, "name", notification.name)
            odb.delete(readNotification.template)
            odb.delete(readNotification.trigger)
            odb.delete(readNotification)
            odb.delete(readNotification.transport)
            odb.delete(readNotification.transportConfigurations)
            odb.delete(readNotification)
        }
    }

    /**
     * Update a given notification,
     * this will also reschedule the notification
     *
     * @param notification
     * @return
     */
    boolean updateNotification(Notification notification) {
        meddela.database.runDbAction {ODB odb ->
            // Manually copy each of the property from the new notification to the one in the db
            def dbNotification = meddela.database.getObjectByProperty(Notification, 'name', notification.name)
            notification = copy(notification, dbNotification)

            odb.store(notification)
            if (notification.enabled) {
                meddela.scheduler.reScheduleNotification(notification)
            } else {
                meddela.scheduler.deScheduleNotification(notification)
            }
        }
    }

    /**
     * Copies properties from src to dest
     * @param src
     * @param dest
     */
    private def copy(Notification src, Notification dest) {
        dest.description = src.description
        dest.enabled = src.enabled
        dest.schedulerId = src.schedulerId
        dest.template.template = src.template.template
        dest.template.joiningProperty = src.template.joiningProperty
        dest.template.groovyCode = src.template.groovyCode
        dest.template.receiverProperty = src.template.receiverProperty
        dest.template.sqls = src.template.sqls
        dest.trigger.groovyCode = src.trigger.groovyCode
        dest.trigger.schedule = src.trigger.schedule
        dest.trigger.sql = src.trigger.sql
        dest.transport = src.transport
        dest.transportConfigurations = src.transportConfigurations

        return dest
    }
}

