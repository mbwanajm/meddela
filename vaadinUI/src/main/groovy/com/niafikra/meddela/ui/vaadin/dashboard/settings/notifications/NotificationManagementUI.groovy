package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.HorizontalSplitPanel
import com.niafikra.meddela.data.Notification

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 2:17 PM
 */
class NotificationManagementUI extends HorizontalSplitPanel{
    NotificationList notificationList


    NotificationManagementUI(){
        notificationList=new NotificationList(this)
        build()
    }

    def build() {
        setFirstComponent(notificationList)
        showNotificationDetails(new Notification(),true)
        setSplitPosition(50)
        setLocked(true)
        setSizeFull()
        setMargin(true)
    }

    def showNotificationDetails(Notification notification,boolean isNew){
        NotificationUI notificationUI = new NotificationUI(notification,isNew)
        setSecondComponent(notificationUI)
    }
}
