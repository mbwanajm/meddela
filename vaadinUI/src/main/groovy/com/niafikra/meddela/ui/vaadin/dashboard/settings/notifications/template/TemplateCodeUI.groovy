package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.template

import com.niafikra.meddela.data.Notification

import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.CodingTabSheet

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com,bonifacechacha@niafikra.com>
 * Date: 8/26/12
 * Time: 2:18 PM
 */
class TemplateCodeUI extends CodingTabSheet{

    Notification notification
    TemplateCodeUI(Notification notification,boolean isNew){
        super(notification)
        this.notification = notification
        this.sqlArea = new TemplateSQLUI()
        this.groovyArea = new TemplateGroovyCodeArea()
        build()
        load()
      /*  if(!isNew) {
            execute()
            messageUI.load()
        }  */
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

    def getTemplateVariables(){
        def result = sqlArea.getTemplateVariables(notification)
        if(result==null)
            result = groovyArea.getTemplateVariables(notification)
        return  result
    }

    def commit(){
        notification.template.sqls = sqlArea.getCode()
        notification.template.groovyCode = groovyArea.getCode()
    }
}
