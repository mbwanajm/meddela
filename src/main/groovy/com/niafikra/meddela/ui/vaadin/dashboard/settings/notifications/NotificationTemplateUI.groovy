package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.data.Notification
import com.vaadin.ui.*

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 5:23 PM
 */
class NotificationTemplateUI extends VerticalLayout implements Button.ClickListener {
    ComboBox joiningBox, receiverBox
    TextArea messageBox
    NotificationCodeArea sqlArea, groovyArea
    Button test
    Notification notification

    NotificationTemplateUI(Notification notification, boolean isNew) {
        this.notification = notification
        joiningBox = new ComboBox("Joining Property")
        receiverBox = new ComboBox("Receiver Field")
        messageBox = new TextArea("Message Template")
        test = new Button("Test")
        sqlArea = new TemplateSQLUI()
        groovyArea = new TemplateGroovyCodeArea()
        build()
        load()//fill the required values eg SQL code for test
        if (!isNew) {
            test()
            load() //fill all the required values
        }
    }

    def load() {
        messageBox.setValue(notification.getTemplate().template)
        sqlArea.setCode(notification.template.sqls)
        groovyArea.setCode(notification.template.groovyCode)
        joiningBox.setValue(notification.template.joiningProperty)
        receiverBox.setValue(notification.template.receiverProperty)
    }

    def commit() {
        notification.template.groovyCode = groovyArea.getCode()
        notification.template.sqls = sqlArea.getCode()
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
        formLayout.addComponent(messageBox)
        formLayout.setSpacing(true)
        addComponent(formLayout)

        TabSheet codeSheet = new TabSheet()
        codeSheet.addTab(sqlArea, "SQL")
        codeSheet.addTab(groovyArea, "GROOVY")
        codeSheet.setSizeFull()

        addComponent(codeSheet)
        test.addListener(this)
        addComponent(test)

        setExpandRatio(codeSheet, 1)
    }


    @Override
    void buttonClick(Button.ClickEvent event) {
        if (test()) {
            getWindow().showNotification("Code Executes Well ", Window.Notification.TYPE_HUMANIZED_MESSAGE)
        } else
            getWindow().showNotification("Code Fails Executes", Window.Notification.TYPE_WARNING_MESSAGE)
    }

    boolean test() {
        def results
        results = sqlArea.execute(notification)
        if (!results)
            results = groovyArea.execute(notification)

        if (results) {
            fillSelectables(results)
            return true
        } else
            return false
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
        for (Object result in results) {
            // columns << result[0].keySet()
            for (Object column in result[0].keySet()) {
                columns << column
            }
        }
        return columns
    }
}
