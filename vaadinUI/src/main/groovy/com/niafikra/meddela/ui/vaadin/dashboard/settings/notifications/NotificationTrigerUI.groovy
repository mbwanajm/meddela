package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications;


import com.niafikra.meddela.data.Notification
import com.vaadin.ui.Button
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/20/12
 * Time: 9:42 PM
 */
class NotificationTrigerUI extends VerticalLayout implements Button.ClickListener{

    ScheduleForm scheduleForm
    TrigerSetup trigerSetup
    Button testButton
    Notification notification

    NotificationTrigerUI(Notification notification) {
        this.notification = notification
        scheduleForm = new ScheduleForm(notification.trigger)
        trigerSetup = new TrigerSetup(notification.trigger)
        testButton = new Button("Test")
        build()
    }

    void build() {
        setSpacing(true)
        setMargin(true)
        addComponent(scheduleForm)
        addComponent(trigerSetup)
        addComponent(testButton)
        testButton.addListener(this)
        setSizeFull()
        setExpandRatio(trigerSetup, 1)
    }

    void commit() {
        scheduleForm.commit()
        trigerSetup.commit()
    }

    def execute(){
        def result;
        result=trigerSetup.sqlSetupView.execute(notification)
        if(!result)
            result=trigerSetup.groovySetupView.execute(notification)
        return result
    }

    @Override
    void buttonClick(Button.ClickEvent event) {
        if (execute())
            getWindow().showNotification("Code Executes Well",Window.Notification.TYPE_HUMANIZED_MESSAGE)
        else
            getWindow().showNotification("Code Fails to execute",Window.Notification.TYPE_WARNING_MESSAGE)
    }
}
