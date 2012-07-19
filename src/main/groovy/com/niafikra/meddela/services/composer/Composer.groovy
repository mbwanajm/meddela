package com.niafikra.meddela.services.composer

import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.utilities.GStringUtil
import com.niafikra.meddela.data.SentNotification
import com.niafikra.meddela.data.Template
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
            // results =
        }

        def notificationToSend = []          // will contain sms to send

        for (result in results) {
            SentNotification sentNotification = new SentNotification(
                    notification: notification,
                    receiver: result[notification.template.receiverProperty],
                    content: GStringUtil.evaluateAsGString(notification.template.template, result)
            )
        }

        return notificationToSend
    }

    /**
     * Evaluate all the template sql's and merge its results
     *
     * @param notification
     * @return
     */
    def evaluateSQL(Notification notification) {
        def queryResults = []
        for (query in notification.template.sqls) {
            queryResults << SqlUtil.runWithSqlConnection(notification) { Sql sql -> sql.rows(query) }
        }

        mergeResults(notification.template.joiningProperty, queryResults)
    }

    /**
     * Normally the sql executed above will each result in its own resultset,
     * This merges all the results that have a column joining property the same
     *
     *
     * @param joiningProperty
     * @param results
     */
    def mergeResults(String joiningProperty, List results) {
        // Collect all the unique joining properties in the resultset
        def joiningPropertyValues = [] as Set
        for (result in results) {
            joiningPropertyValues << result[joiningProperty]
        }

        // Group the results according to joining property values
        def mergedResults = [:]
        for (joinValue in joiningPropertyValues) {
            for (result in results) {
                if (result[joiningProperty].equals(joinValue)) {
                    if (!mergedResults[joinValue])  mergedResults[joinValue] = [:]
                    mergedResults[joinValue] << result
                }
            }
        }

        return mergedResults
    }

}
