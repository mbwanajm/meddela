package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.data.Notification
import java.sql.ResultSet

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com,bonifacechacha@niafikra.com>
 * Date: 8/26/12
 * Time: 2:18 PM
 */
class TemplateCodeUI extends CodingTabSheet{

    Notification notification
    TemplateMessageUI messageUI
    TemplateCodeUI(Notification notification,boolean isNew,TemplateMessageUI messageUI){
        super()
        this.notification = notification
        this.sqlArea = new TemplateSQLUI()
        this.messageUI = messageUI
        this.groovyArea = new TemplateGroovyCodeArea()
        build()
        load()
        if(!isNew) {
            execute()
            messageUI.load()
        }
    }

    def load() {
        sqlArea.setCode(notification.template.sqls)
        groovyArea.setCode(notification.template.groovyCode)
    }

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

    }

    def commit(){
        notification.template.sqls = sqlArea.getCode()
        notification.template.groovyCode = groovyArea.getCode()
    }
}
