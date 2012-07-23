package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.meddela
/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/22/12
 * Time: 8:16 PM
 */
class TemplateGroovyCodeArea extends AbstractCodeArea{

    TemplateGroovyCodeArea(){
        super("GROOVY")
    }
    @Override
    def execute(com.niafikra.meddela.data.Notification notification) {
         notification.template.groovyCode=getCode()
         meddela.composer.runGroovyScript(notification)
    }
}
