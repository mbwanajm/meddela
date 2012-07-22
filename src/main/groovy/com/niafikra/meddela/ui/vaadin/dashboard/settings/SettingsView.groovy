package com.niafikra.meddela.ui.vaadin.dashboard.settings

import com.vaadin.ui.TabSheet
import com.vaadin.terminal.ThemeResource

import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.NotificationManagementUI

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/19/12
 * Time: 5:30 PM
 */
class SettingsView extends TabSheet{

    SettingsView() {
        build()
    }

    def build() {
        setSizeFull()

       addTab(new NotificationManagementUI(),"Notification Setup",new ThemeResource("notset.png"))
    }
}
