package com.niafikra.meddela.services

import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.meddela
import org.apache.log4j.Logger
import com.niafikra.meddela.data.Trigger

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
class TriggerTask implements Runnable {
    String id                   // the id of the Trigger task , it is assigned by CRON4J
    static final Logger log = Logger.getLogger(TriggerTask)

    @Override
    void run() {
        // Get the notification info from the object database
        Notification notification = meddela.database.getObjectByProperty(Notification, "schedulerId", id)

        // if for some reason the read notification is null log and leave immediately
        if (!task) {
            log.warn ("we have a scheduled trigger check but no trigger information in db, panicking!")
            return
        }

        // test if the sql or groovy condition is true
        def needToSendNotification = false;
        if(notification.trigger.sql){
            needToSendNotification = checkSQLCondition(notification)
        } else {
            needToSendNotification = checkGroovyCondition(notification)
        }

        // if sql or groovy condition is true go ahead  and tell the composer to compose a message and send it out
        if(needToSendNotification){
            //medella.msgComposer.sendOutNotification(notification)
        }
    }

    /**
     * Check to see if the groovy condition in the notification is satisfied
     * @param notification
     * @return true if satisfied false otherwise
     */
    boolean checkGroovyCondition(Notification notification) {
    }

    /**
     * Check to see if the sql condition in the notification is satisfied
     * @param notification
     * @return true if satisfied false otherwise
     */
    boolean checkSQLCondition(Notification notification) {

    }
}
