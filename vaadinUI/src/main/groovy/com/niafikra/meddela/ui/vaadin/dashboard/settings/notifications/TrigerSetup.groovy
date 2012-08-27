package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.TabSheet
import com.niafikra.meddela.data.Trigger
import com.niafikra.meddela.data.Notification
import com.vaadin.ui.Window

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 1:17 PM
 */
class TrigerSetup extends CodingTabSheet {

    NotificationCodeArea sqlSetupView, groovySetupView
    Notification notification

    TrigerSetup(Notification notification) {
        this.notification = notification
        sqlSetupView = new CodeArea("SQL")

        groovySetupView = new CodeArea("GROOVY")

        build()
        load()
    }

    def load() {
        sqlSetupView.setCode(notification.trigger.getSql())
        groovySetupView.setCode(notification.trigger.getGroovyCode())
    }

    def build() {
        // setSizeFull()
        setSqlArea(sqlSetupView)
        setGroovyArea(groovySetupView)
        super.build()
    }

    @Override
    void onExecute() {
        def result = execute()
        /*  if (result)
            getWindow().showNotification("Code Executes Well", Window.Notification.TYPE_HUMANIZED_MESSAGE)
        else
            getWindow().showNotification("Code Fails to execute", Window.Notification.TYPE_WARNING_MESSAGE)  */
        def formattedResult = formatOutput(result)
        outputArea.setValue(formattedResult)
    }

    def formatOutput(Map result) {
        StringBuffer out = new StringBuffer("Trigger Check Output\n")

        if (!result) return "There is not trigger information to test"
        if(result.size()==2) out.append("\nWarning:YOU HAVE BOTH SQL AND GROOVY CODES ONLY SQL CODES WILL BE USED\n")

        if(result["sql"])
        out.append("SQL:\n").append(getMessageToShow(result.get("sql")))
        if(result["groovy"])
        out.append("\nGROOVY:\n").append(getMessageToShow(result.get("groovy")))
        return out

    }

    Object getMessageToShow(Object result) {
         if(result instanceof ArrayList)
             return "Trigger infomation returns a resultset"
        else return result
    }

    def commit() {
        notification.trigger.setSql(sqlSetupView.code)
        notification.trigger.setGroovyCode(groovySetupView.code)
    }

    def execute() {
        Map results = [:]
        // def result;
        def sqlResult = sqlSetupView.execute(notification)
        // if (!result)
        def groovyResult = groovySetupView.execute(notification)
        if (sqlResult)
            results.put("sql", sqlResult)
        if (groovyResult)
            results.put("groovy", groovyResult)
        return results
    }


}
