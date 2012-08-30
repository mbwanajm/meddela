package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.template

import com.niafikra.meddela.data.Notification

import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.CodingTabSheet
import com.vaadin.ui.Window
import groovy.sql.GroovyRowResult

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com,bonifacechacha@niafikra.com>
 * Date: 8/26/12
 * Time: 2:18 PM
 */
class TemplateCodeUI extends CodingTabSheet{

    Notification notification
    boolean isNew
    TemplateCodeUI(Notification notification,boolean isNew){
        super(notification)
        this.notification = notification
        this.sqlArea = new TemplateSQLUI()
        this.groovyArea = new TemplateGroovyCodeArea()
        build()
        load()
        this.isNew = isNew
    }

    def load() {
        sqlArea.setCode(notification.template.sqls)
        groovyArea.setCode(notification.template.groovyCode)
    }
   /*
    @Override
    void onExecute() {
        def result = execute()
        outputArea.setValue(getOutputMessage(result))

    }

    String getOutputMessage(Object result) {
        if(result instanceof ArrayList){
              if(result[0] instanceof ArrayList)
               return "Template variables codes returns a resultset"
        } else
            return result
    }

    def execute() {
        def results= sqlArea.execute(notification)
        if (!results && !results.isEmpty())
            results = groovyArea.execute(notification)
        messageUI.fillSelectables(results)
        return  results

    }          */

    def getTableToShow(Object multiResults,String source) {
        if(multiResults.isEmpty())
            return super.getTableToShow(multiResults,source)
        if(!(multiResults[0] instanceof Collection)  )
        {
            if(source.equalsIgnoreCase("groovy"))
            getWindow().showNotification(
                    "Results returned by your codes are not in the required format"
                    ,Window.Notification.TYPE_WARNING_MESSAGE)
            return super.getTableToShow(multiResults,source)
        }
        def content = "<hr/>"
        for (results in multiResults){
        content += super.getTableToShow(results,source)
        content+="<hr/><br/>"
        }
        return content
    }

    def getTemplateVariables(){
        def result = sqlArea.getTemplateVariables(notification)
        if(!result)
            result = groovyArea.getTemplateVariables(notification)
        return  result
    }

    def commit(){
        notification.template.sqls = sqlArea.getCode()
        notification.template.groovyCode = groovyArea.getCode()
    }

    @Override
    CodingTabSheet getNewInstance() {
        return new TemplateCodeUI(notification,isNew)
    }
}
