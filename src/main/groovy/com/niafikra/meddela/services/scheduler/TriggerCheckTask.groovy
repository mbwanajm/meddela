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
        Notification notification = meddela.database.getObjectByProperty(Notification, "schedulerId", id)

        // if for some reason the read notification is null log and leave immediately
        if (!notification) {
            log.warn("we have a scheduled trigger check but no trigger information in db, panicking!")
            return
        }

        // test if the sql or groovy condition is true
        def needToSendNotification = false;
        if (notification.trigger.sql) {
            needToSendNotification = SqlUtil.runWithSqlConnection(notification, checkSqlCondition)
        } else {
            needToSendNotification = SqlUtil.runWithSqlConnection(notification, checkGroovyCondition)
        }

        // if sql or groovy condition is true go ahead  and tell the composer to compose a message and send it out
        if (needToSendNotification) {
            meddela.transportManager.sendNotification(notification)
        }
    }

    /**
     * Check to see if the groovy condition in the notification is satisfied
     * the groovy condition is basically groovy code which when evaluated
     * returns true or false
     *
     * @param notification
     * @param sql the sql connection to use
     * @return true if satisfied false otherwise
     */
    def checkGroovyCondition =  { Sql sql, Notification notification ->
        def params = GStringUtil.setUpDates()
        params['sql'] = sql

        Binding binding = new Binding(params)
        GroovyShell shell = new GroovyShell(binding)

        shell.evaluate(notification.trigger.groovyCode)
    }

    /**
     * Check to see if the sql condition in the notification is satisfied
     * @param notification
     * @parama sql the sql connection
     * @return true if satisfied false otherwise
     */
    def checkSqlCondition = { Sql sql, Notification notification ->
        GString query = GStringUtil.evaluateSqlAsGString(notification.trigger.sql)
        sql.rows(query).size() > 0
    }



}
