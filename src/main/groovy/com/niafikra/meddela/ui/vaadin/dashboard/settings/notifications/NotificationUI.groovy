package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.meddela
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.TabSheet
import com.vaadin.ui.VerticalLayout
import org.neodatis.odb.ODB

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 4:18 PM
 */
class NotificationUI extends VerticalLayout implements Button.ClickListener {

    NotificationBasicUI basicUI
    NotificationTrigerUI trigerUI
    NotificationTemplateUI templateUI
    Button save, delete
    boolean isNew
    Notification notification

    NotificationUI(Notification notification, boolean isNew) {
        this.isNew = isNew
        this.notification = notification
        basicUI = new NotificationBasicUI(notification)
        trigerUI = new NotificationTrigerUI(notification)
        templateUI = new NotificationTemplateUI(notification, isNew)

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

        HorizontalLayout footer = new HorizontalLayout()
        save.setWidth("150px")
        delete.setWidth("150px")
        delete.setVisible(!isNew)
        save.addListener(this)
        delete.addListener(this)

        footer.addComponent(save)
        footer.addComponent(delete)
        footer.setSpacing(true)

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
            saveNotification()
        } else if (event.getButton().equals(delete)) {
            deleteNotification()
        }
    }

    void deleteNotification() {
        meddela.notificationManager.deleteNotification(notification)
    }

    void saveNotification() {
        commit()
        if (isNew)
            meddela.notificationManager.addNotification(notification)
        else meddela.notificationManager.updateNotification(notification)
    }
}
