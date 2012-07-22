package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.*
import com.niafikra.meddela.data.Notification

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 5:23 PM
 */
class NotificationTemplateUI extends VerticalLayout {
    ComboBox joiningBox, receiverBox
    TextArea messageBox
    CodeArea sqlArea, groovyArea
    Button test
    Notification notification

    NotificationTemplateUI(Notification notification) {
        this.notification=notification
        joiningBox = new ComboBox("Joining Property")
        receiverBox = new ComboBox("Receiver Field")
        messageBox = new TextArea("Message Template")
        test = new Button("Test")
        sqlArea = new DefaultCodeArea("SQL")
        groovyArea = new DefaultCodeArea("GROOVY")
        build()
        load()
    }

    def load() {
        messageBox.setValue(notification.getTemplate().template)
        sqlArea.setCode(notification.template.sqls)
        groovyArea.setCode(notification.template.groovyCode)

    }

    def commit(){
        notification.template.groovyCode=groovyArea.getCode()
        notification.template.sqls=sqlArea.getCode()
        notification.template.template=messageBox.getValue()
        notification.template.joiningProperty=joiningBox.getValue()
        notification.template.receiverProperty=receiverBox.getValue()
    }

    def build() {
        setSizeFull()
        setMargin(true)
        setSpacing(true)
        FormLayout formLayout=new FormLayout()
        formLayout.addComponent(joiningBox)
        formLayout.addComponent(receiverBox)
        messageBox.setWidth("80%")
        formLayout.addComponent(messageBox)
        formLayout.setSpacing(true)
        addComponent(formLayout)

        TabSheet codeSheet = new TabSheet()
        codeSheet.addTab(sqlArea, "SQL")
        codeSheet.addTab(groovyArea, "GROOVY")
        codeSheet.setSizeFull()

        addComponent(codeSheet)
        addComponent(test)

        setExpandRatio(codeSheet, 1)
    }


}
