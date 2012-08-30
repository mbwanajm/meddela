package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.trigger;


import com.niafikra.meddela.data.Notification

import com.vaadin.ui.VerticalLayout

import com.vaadin.ui.Accordion

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/20/12
 * Time: 9:42 PM
 */
class NotificationTrigerUI extends VerticalLayout{

    ScheduleForm scheduleForm
    TriggerSetup trigerSetup
    Notification notification

    NotificationTrigerUI(Notification notification,boolean isNew) {
        this.notification = notification
        scheduleForm = new ScheduleForm(notification.trigger,isNew)
        trigerSetup = new TriggerSetup(notification)
        build()
    }

    void build() {
        setSpacing(true)
       // setMargin(true)

        Accordion holder =new Accordion()
        holder.addTab(scheduleForm,"Schedule")
        holder.addTab(trigerSetup,"Trigger")
        addComponent(holder)
        holder.setSizeFull()
        setSizeFull()
    }

    void commit() {
        scheduleForm.commit()
        trigerSetup.commit()
    }

}
