package com.niafikra.meddela.services.composer

import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.data.SentNotification
import com.niafikra.meddela.utilities.GStringUtil
import com.niafikra.meddela.utilities.SqlUtil
import groovy.sql.Sql

/**
 * This class deals with composing a notification
 *
 *
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 13:13
 */
class Composer {

    /**
     * Given a notification object it will extract the required information
     * from the database using either the template sqls or groovycode
     * then using that information compose text messages
     *
     * @param notification
     * @return a collection of sms to be sent out
     */
    def compose(Notification notification) {
        def results
        if (notification.template.sqls) {
            results = evaluateSQL(notification)

        } else {
            results = SqlUtil.runWithSqlConnection(notification, runGroovyScript)
        }

        results = mergeResults(notification.template.joiningProperty, notification.template.receiverProperty, results)
        results = results.values()      // ignore keys and get only values of the returned map

        def notificationsToSend = []    // will contain sms to send
        for (result in results) {
            // handle multiple receivers separated by commas
            def receivers = result[notification.template.receiverProperty]?.split(',')
            if (receivers) {
                for (receiver in receivers) {
                    SentNotification sentNotification = new SentNotification(
                            notification: notification,
                            receiver: receiver,
                            content: GStringUtil.evaluateAsGString(notification.template.template, result)
                    )
                    notificationsToSend << sentNotification
                }
            } else {
                SentNotification sentNotification = new SentNotification(
                        notification: notification,
                        receiver: null,
                        content: GStringUtil.evaluateAsGString(notification.template.template, result)
                )
                notificationsToSend << sentNotification
            }
        }

        return notificationsToSend
    }

    /**
     * A groovy script will be given the variables
     *
     *  todaysDate
     *  firstDateOfMonth
     *  lastDateOfMonth
     *  sql
     *
     *  It is expected that it will return a list of resultset,
     *  a resultset is simply a map
     *
     * @param notification
     */
    def runGroovyScript = { Sql sql, Notification notification ->
        def params = GStringUtil.setUpDates()
        params['sql'] = sql

        Binding binding = new Binding(params)
        GroovyShell shell = new GroovyShell(binding);

        shell.evaluate(notification.template.groovyCode)
    }

    /**
     * Evaluate all the template
     *
     * @param notification
     * @return
     */
    def evaluateSQL(Notification notification) {
        def queryResults = []
        for (query in notification.template.sqls) {
            def result = SqlUtil.runWithSqlConnection(notification) { Sql sql, Notification notif -> sql.rows(query.SQL) }
            if (result) {
                queryResults << result
            } else {
                // return queryResults
            }
        }

        queryResults
    }

    /**
     * Normally the sql executed above will each result in its own resultset,
     * This merges all the results that have a column joining property the same
     *
     *
     * @param joiningProperty
     * @param receiverProperty
     * @param results
     */
    def mergeResults(String joiningProperty, String receiverProperty, List results) {
        // Collect all the unique joining properties in the resultset
        def joiningPropertyValues = [] as Set
        for (result in results) {
            for (innerResult in result) {
                joiningPropertyValues << innerResult[joiningProperty]
            }
        }

        // Group the results according to joining property values
        def mergedResults = [:]
        for (joinValue in joiningPropertyValues) {
            for (rows in results) {
                for (row in rows) {
                    if (row[joiningProperty].equals(joinValue)) {
                        if (!mergedResults[joinValue]) mergedResults[joinValue] = [:]

                        // if a certain merged result has more than one receiver
                        // then put them in one field as comma separated values
                        if (row.keySet().contains(receiverProperty) && mergedResults[joinValue]) {
                            def existingReceiverProperty = mergedResults[joinValue][receiverProperty]
                            if (existingReceiverProperty) {
                                // prevent a receiver from being used twice in the same notification
                                if (!existingReceiverProperty.contains(row[receiverProperty])) {
                                    mergedResults[joinValue][receiverProperty] =
                                        "$existingReceiverProperty,${row[receiverProperty]}"
                                }

                                row.remove(receiverProperty)
                            }
                        }
                        mergedResults[joinValue] << row
                    }
                }
            }
        }

        return mergedResults
    }

}
