package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.meddela
import com.vaadin.ui.Button

import com.vaadin.ui.TabSheet
import com.vaadin.ui.VerticalLayout

import com.vaadin.ui.Window

import org.vaadin.peter.buttongroup.ButtonGroup
import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.basic.NotificationBasicUI
import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.trigger.NotificationTrigerUI
import com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.template.NotificationsTemplateUI

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 4:18 PM
 */
class NotificationUI extends VerticalLayout implements Button.ClickListener {

    NotificationBasicUI basicUI
    NotificationTrigerUI trigerUI
    NotificationsTemplateUI templateUI
    NotificationManagementUI managementUI
    Button save, delete
    boolean isNew
    Notification notification

    NotificationUI(Notification notification, boolean isNew, NotificationManagementUI managementUI) {
        this.isNew = isNew
        this.notification = notification
        this.managementUI = managementUI
        basicUI = new NotificationBasicUI(notification, isNew)
        trigerUI = new NotificationTrigerUI(notification,isNew)
        templateUI = new NotificationsTemplateUI(notification,isNew)

        save = new Button("Save")
        delete = new Button("Delete")
        build()
    }

    def build() {
        setSpacing(true)
        setMargin(true)
        setSizeFull()
        TabSheet uiHolder = new TabSheet()
        uiHolder.addTab(basicUI, "Basics")
        uiHolder.addTab(trigerUI, "Trigger")
        uiHolder.addTab(templateUI, "Template")
        uiHolder.setSizeFull()

        ButtonGroup footer = new ButtonGroup()
        save.setWidth("150px")
        delete.setWidth("150px")
        delete.setVisible(!isNew)
        save.addListener(this)
        delete.addListener(this)

        footer.addButton(save)
        footer.addButton(delete)
        //  footer.setSpacing(true)

        addComponent(uiHolder)
        addComponent(footer)
        setExpandRatio(uiHolder, 1)
    }

    def commit() {
        basicUI.commit()
        trigerUI.commit()
        templateUI.commit()
    }

    @Override
    void buttonClick(Button.ClickEvent event) {
        if (event.getButton().equals(save)) {
            if (saveNotification())
                getWindow().showNotification("notification saved successfully", Window.Notification.TYPE_HUMANIZED_MESSAGE)
            else getWindow().showNotification("notification failed to be saved", Window.Notification.TYPE_ERROR_MESSAGE)
        } else if (event.getButton().equals(delete)) {
            if (deleteNotification())
                getWindow().showNotification("notification deleted successfully", Window.Notification.TYPE_HUMANIZED_MESSAGE)
            else getWindow().showNotification("notification failed to be deleted", Window.Notification.TYPE_ERROR_MESSAGE)
        }
        managementUI.loadNotificationList()
    }

    def deleteNotification() {
        return meddela.notificationManager.deleteNotification(notification)
    }

    def saveNotification() {
        commit()
        if (isNew)
            return meddela.notificationManager.addNotification(notification)
        else
            return meddela.notificationManager.updateNotification(notification)
    }
}
