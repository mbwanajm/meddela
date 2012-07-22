package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications;


import com.niafikra.meddela.data.Notification
import com.vaadin.ui.Button
import com.vaadin.ui.VerticalLayout

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/20/12
 * Time: 9:42 PM
 */
class NotificationTrigerUI extends VerticalLayout {

    ScheduleForm scheduleForm
    TrigerSetup trigerSetup
    Button testButton
    Notification notification

    NotificationTrigerUI(Notification notification) {
        this.notification = notification
        scheduleForm = new ScheduleForm(notification.trigger)
        trigerSetup = new TrigerSetup(notification.trigger)
        testButton = new Button("TEST")
        build()
    }

    void build() {
        setSpacing(true)
        setMargin(true)
        addComponent(scheduleForm)
        addComponent(trigerSetup)
        addComponent(testButton)
        setSizeFull()
        setExpandRatio(trigerSetup, 1)
    }

    void commit() {
        scheduleForm.commit()
        trigerSetup.commit()
    }

}
