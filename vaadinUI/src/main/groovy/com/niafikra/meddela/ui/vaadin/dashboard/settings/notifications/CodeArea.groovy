package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.meddela

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/22/12
 * Time: 8:13 PM
 */
class CodeArea extends AbstractCodeArea{

    CodeArea(String type) {
        super(type)
    }

    @Override
    def execute(com.niafikra.meddela.data.Notification notification) {

        switch(getType().toUpperCase()){
            case "GROOVY" :
                notification.trigger.groovyCode=getCode()
                return meddela.triggerCheck.checkWithGroovy(notification)

            case "SQL":
                notification.trigger.sql=getCode()
                return meddela.triggerCheck.checkWithSQL(notification)
        }
    }
}
