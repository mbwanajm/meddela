package com.niafikra.meddela.ui.vaadin.dashboard.settings

import com.vaadin.ui.TabSheet

import com.niafikra.meddela.ui.vaadin.dashboard.settings.datasources.DataSourcePanel
import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.NotificationManagementUI

import com.niafikra.meddela.ui.vaadin.dashboard.settings.transport.TransportUploadPanel
import com.niafikra.meddela.ui.vaadin.dashboard.settings.misc.MiscPanel

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/19/12
 * Time: 5:30 PM
 */
class SettingsView extends TabSheet{
    private MiscPanel miscPanel = new MiscPanel()
    private DataSourcePanel dataSourcePanel= new DataSourcePanel()
    private NotificationManagementUI notificationManagementUI = new NotificationManagementUI();
    private TransportUploadPanel transportUploadPanel=new TransportUploadPanel()

    SettingsView() {
        setSizeFull()

        addTab(notificationManagementUI,"notifications")
        addTab(dataSourcePanel, 'data sources')
        addTab(transportUploadPanel,"transport")
        addTab(miscPanel, 'misc')
    }


    def build() {
        setSizeFull()
    }
}
