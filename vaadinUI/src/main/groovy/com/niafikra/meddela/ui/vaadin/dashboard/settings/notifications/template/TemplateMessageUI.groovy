package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.template

import com.niafikra.meddela.data.Notification
import com.vaadin.ui.*

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 5:23 PM
 */
class TemplateMessageUI extends VerticalLayout implements Button.ClickListener{
    ComboBox joiningBox, receiverBox
    TextArea messageBox
    Notification notification
    TemplateCodeUI codeUI

    Button loadButton
    TemplateMessageUI(Notification notification, boolean isNew,TemplateCodeUI codeUI) {
        this.notification = notification
        joiningBox = new ComboBox("Joining Property")
        receiverBox = new ComboBox("Receiver Field")
        messageBox = new TextArea("Message Template")
        this.codeUI = codeUI
        loadButton = new Button("Load Selectables")
        build()
        if(!isNew) load()//fill the required values eg SQL code for test
    }

    def load() {
        reloadSelectables()
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
       // setSpacing(true)
        FormLayout formLayout = new FormLayout()
        formLayout.addComponent(joiningBox)
        formLayout.addComponent(receiverBox)
        joiningBox.setWidth("80%")
        receiverBox.setWidth("80%")
        messageBox.setWidth("80%")
        messageBox.rows = 10
        formLayout.addComponent(messageBox)
        formLayout.setSpacing(true)
        addComponent(loadButton)
        loadButton.setWidth("200px")
        setComponentAlignment(loadButton,Alignment.TOP_CENTER)
        loadButton.addListener(this)
        addComponent(formLayout)
        setExpandRatio(formLayout,1)
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

    @Override
    void buttonClick(Button.ClickEvent clickEvent) {
        reloadSelectables()
    }

    def reloadSelectables() {
        def results= codeUI.getTemplateVariables()
        fillSelectables(results)
    }
}
