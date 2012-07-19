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
            def readNotification = meddela.database.getObjectsByPropertiesAND(Notification,
                    [
                            name: notification.name,
                            description: notification.description
                    ])

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

        meddela.database.runDbAction {ODB odb -> odb.store(notification)}
        if (notification.enabled) {
            meddela.scheduler.reScheduleNotification(notification)
        } else {
            meddela.scheduler.deScheduleNotification(notification)

        }
    }
}
