package com.niafikra.meddela.services.scheduler

import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.meddela
import org.apache.log4j.Logger
import groovy.sql.Sql
import com.niafikra.meddela.utilities.GStringUtil
import com.niafikra.meddela.utilities.SqlUtil

/**
 * Triggers are scheduled to be executed at certain times.
 * When the time reaches the scheduler will call the run method of this object
 *
 * in the run method this object checks if any of the conditions is fullfilled
 * (sql or groovy) if so then then it delegate the task of composing and sending
 * messages to the message composer
 *
 * @author mbwana jaffari mbura
 * Date: 18/07/12
 * Time: 20:18
 */
class TriggerCheckTask implements Runnable {
    String id                   // the id of the Trigger task , it is assigned by CRON4J
    static final Logger log = Logger.getLogger(TriggerCheckTask)

    @Override
    void run() {
        // Get the notification info from the object database
        Collection notifications = meddela.database.getObjectsByProperty(Notification, "schedulerId", id)

        // if for some reason the read notification is null log and leave immediately
        if (!notifications || notifications?.isEmpty()) {
            log.warn("we have a scheduled trigger check but no trigger information in db, panicking!")
            return
        }

        def notification = notifications.iterator().next()
        // test if the sql or groovy condition is true
        def needToSendNotification = false;
        if (notification.trigger.sql) {
            needToSendNotification = meddela.triggerCheck.checkWithSQL(notification)
        } else {
            needToSendNotification = meddela.triggerCheck.checkWithGroovy(notification)
        }

        // if sql or groovy condition is true tell the transport manager to send out notification
        if (needToSendNotification) {
            meddela.transportManager.sendNotification(notification)
        }
    }

}
