package com.niafikra.meddela.services.scheduler

import groovy.sql.Sql
import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.utilities.GStringUtil
import com.niafikra.meddela.utilities.SqlUtil

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/22/12
 * Time: 7:51 PM
 */
class TriggerCheck {

    def checkWithGroovy(Notification notification){
        SqlUtil.runWithSqlConnection(notification, checkGroovyCondition)
    }

    def checkWithSQL(Notification notification){
        SqlUtil.runWithSqlConnection(notification, checkSqlCondition)
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
        String query = GStringUtil.evaluateSqlAsGString(notification.trigger.sql)
        sql.rows(query).size() > 0
    }

}
