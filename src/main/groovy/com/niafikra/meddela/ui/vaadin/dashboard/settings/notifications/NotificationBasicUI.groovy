package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.FormLayout
import com.vaadin.ui.ComboBox
import com.vaadin.ui.TextField
import com.vaadin.ui.CheckBox
import com.vaadin.ui.TextArea
import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.meddela
import com.niafikra.meddela.data.DataSource
import com.vaadin.data.util.BeanItemContainer
/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 3:31 PM
 */
class NotificationBasicUI extends FormLayout{
    TextField nameField
    ComboBox datasourceSelect
    CheckBox enableSelect
    TextArea descriptionArea
    Notification notification

    NotificationBasicUI(Notification notification){
        this.notification=notification
        nameField=new TextField("Name")
        datasourceSelect=new ComboBox("Data Source")
        enableSelect=new CheckBox("Enable")
        descriptionArea=new TextArea("Description")
        build()
        load()
    }

    def build() {
        setMargin(true)
        setSpacing(true)
        setSizeFull()
        descriptionArea.setWidth("70%")
        addComponent(nameField)
        addComponent(datasourceSelect)
        addComponent(enableSelect)
        addComponent(descriptionArea)
    }

    def load(){
        Collection datasources=meddela.database.getAll(DataSource.class)
        datasourceSelect.setContainerDataSource(new BeanItemContainer(DataSource.class,datasources))
        nameField.setValue(notification.getName())
        datasourceSelect.setValue(notification.getDataSource())
        enableSelect.setValue(notification.isEnabled())
        descriptionArea.setValue(notification.getDescription())
    }

    void commit(){
       notification.setDescription(descriptionArea.getValue())
       notification.setDataSource(datasourceSelect.getValue())
       notification.setName(nameField.getValue())
       notification.setEnabled(enableSelect.getValue())
    }
}
