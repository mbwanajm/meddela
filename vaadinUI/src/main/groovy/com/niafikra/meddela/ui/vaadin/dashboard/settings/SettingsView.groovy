package com.niafikra.meddela.ui.vaadin.dashboard.settings

import com.vaadin.ui.TabSheet
import com.niafikra.meddela.ui.vaadin.dashboard.settings.themes.ThemesPanel
import com.niafikra.meddela.ui.vaadin.dashboard.settings.datasources.DataSourcePanel
import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.NotificationManagementUI
import com.vaadin.terminal.ThemeResource
import com.niafikra.meddela.ui.vaadin.dashboard.settings.transport.TransportUploadPanel

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/19/12
 * Time: 5:30 PM
 */
class SettingsView extends TabSheet{
    private ThemesPanel themes = new ThemesPanel()
    private DataSourcePanel dataSourcePanel= new DataSourcePanel()
    private NotificationManagementUI notificationManagementUI = new NotificationManagementUI();
    private TransportUploadPanel transportUploadPanel=new TransportUploadPanel()

    SettingsView() {
        setSizeFull()

        addTab(notificationManagementUI,"notifications")
        addTab(dataSourcePanel, 'data sources')
        addTab(transportUploadPanel,"transport")
        addTab(themes, 'themes')
    }


    def build() {
        setSizeFull()
    }
}
