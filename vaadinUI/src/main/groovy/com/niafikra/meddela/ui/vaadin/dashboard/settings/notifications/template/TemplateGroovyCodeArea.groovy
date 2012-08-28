package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.template

import com.niafikra.meddela.meddela
import com.niafikra.meddela.utilities.SqlUtil
import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.AbstractCodeArea
import com.niafikra.meddela.data.Notification
/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/22/12
 * Time: 8:16 PM
 */
class TemplateGroovyCodeArea extends AbstractCodeArea {

    TemplateGroovyCodeArea() {
        super("GROOVY")
    }

    @Override
    def execute(com.niafikra.meddela.data.Notification notification) {
        notification.template.groovyCode = getCode()
        SqlUtil.runWithSqlConnection(notification, meddela.composer.runGroovyScript)
    }

    def getTemplateVariables(Notification notification){
        return execute(notification)
    }
}
