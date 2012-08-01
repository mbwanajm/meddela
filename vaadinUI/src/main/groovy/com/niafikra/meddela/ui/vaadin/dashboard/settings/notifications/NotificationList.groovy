package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.meddela
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout
import com.vaadin.data.Property
import org.vaadin.peter.buttongroup.ButtonGroup

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 2:18 PM
 */
class NotificationList extends VerticalLayout implements Property.ValueChangeListener,Button.ClickListener{
    Button reloadButton, addButton
    Table notificationList
    NotificationManagementUI managementUI

    NotificationList(NotificationManagementUI managementUI) {
        this.managementUI=managementUI
        notificationList = new Table()
        reloadButton = new Button("Reload")
        addButton = new Button("Add")
        build()
        load()
    }

    def build() {
        setSizeFull()
        setSpacing(true)
        setMargin(true)
        notificationList.setSizeFull()
        notificationList.selectable = true
        notificationList.setImmediate(true)
        notificationList.addListener(this)
        addComponent(notificationList)
        ButtonGroup footer = new ButtonGroup()
        addButton.setWidth("150px")
        reloadButton.setWidth("150px")
        addButton.addListener(this as Button.ClickListener)
        reloadButton.addListener(this as Button.ClickListener)
        footer.addButton(addButton)
        footer.addButton(reloadButton)
        addComponent(footer)
        setExpandRatio(notificationList, 1)
    }

    def load() {
        BeanItemContainer<Notification> notificationCont =
            new BeanItemContainer<Notification>(Notification.class, meddela.database.getAll(Notification.class))
        notificationList.setContainerDataSource(notificationCont)
        notificationList.setVisibleColumns(["name","dataSource","enabled"] as String[])
    }

    @Override
    void valueChange(Property.ValueChangeEvent event) {

        def notification=event.getProperty().getValue()
        if(notification) managementUI.showNotificationDetails(notification,false)
    }

    @Override
    void buttonClick(Button.ClickEvent event) {

        if(event.getButton().equals(addButton)){
            addNotification()
        }else if (event.getButton().equals(reloadButton)) {
            reloadNotifications()
        }
    }

    void reloadNotifications() {
        load()
    }

    void addNotification() {
        managementUI.showNotificationDetails(new Notification(),true)
    }
}
