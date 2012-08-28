package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.trigger

import com.vaadin.ui.TabSheet
import com.niafikra.meddela.data.Trigger
import com.niafikra.meddela.data.Notification
import com.vaadin.ui.Window
import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.CodingTabSheet
import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.NotificationCodeArea
import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.CodeArea

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 1:17 PM
 */
class TrigerSetup extends CodingTabSheet {

    NotificationCodeArea sqlSetupView, groovySetupView

    TrigerSetup(Notification notification) {
        super(notification)
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


    def commit() {
        notification.trigger.setSql(sqlSetupView.code)
        notification.trigger.setGroovyCode(groovySetupView.code)
    }

}
