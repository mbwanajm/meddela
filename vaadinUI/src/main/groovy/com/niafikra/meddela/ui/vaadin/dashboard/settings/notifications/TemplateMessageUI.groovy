package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.data.Notification
import com.vaadin.ui.*

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 5:23 PM
 */
class TemplateMessageUI extends VerticalLayout {
    ComboBox joiningBox, receiverBox
    TextArea messageBox
    Notification notification

    TemplateMessageUI(Notification notification, boolean isNew) {
        this.notification = notification
        joiningBox = new ComboBox("Joining Property")
        receiverBox = new ComboBox("Receiver Field")
        messageBox = new TextArea("Message Template")

        build()
        load()//fill the required values eg SQL code for test
    }

    def load() {
        messageBox.setValue(notification.getTemplate().template)
        joiningBox.setValue(notification.template.joiningProperty)
        receiverBox.setValue(notification.template.receiverProperty)
    }

    def commit() {
        notification.template.template = messageBox.getValue()
        notification.template.joiningProperty = joiningBox.getValue()
        notification.template.receiverProperty = receiverBox.getValue()
    }

    def build() {
        setSizeFull()
        setMargin(true)
        setSpacing(true)
        FormLayout formLayout = new FormLayout()
        formLayout.addComponent(joiningBox)
        formLayout.addComponent(receiverBox)
        joiningBox.setWidth("80%")
        receiverBox.setWidth("80%")
        messageBox.setWidth("80%")
        messageBox.rows = 10
        formLayout.addComponent(messageBox)
        formLayout.setSpacing(true)
        addComponent(formLayout)
    }

    void fillSelectables(Object results) {
        Collection columns = getColumns(results)

        for (String column in columns) {
            joiningBox.addItem(column)
            receiverBox.addItem(column)
        }
    }

    Collection getColumns(Object results) {
        def columns = []

        try {
            for (Object result in results) {
                // columns << result[0].keySet()
                try {
                    for (Object column in result[0].keySet()) {
                        columns << column
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return columns
    }
}
