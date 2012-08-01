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
import com.vaadin.data.Property
/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 3:31 PM
 */
class NotificationBasicUI extends FormLayout implements Property.ValueChangeListener{
    TextField nameField
    ComboBox datasourceSelect
    ComboBox transportSelect
    CheckBox enableSelect
    TextArea descriptionArea
    Notification notification
    boolean isNew
    NotificationBasicUI(Notification notification,isNew){
        this.isNew=isNew
        this.notification=notification
        nameField=new TextField("Name")
        datasourceSelect=new ComboBox("Data Source")
        transportSelect=new ComboBox("Transport")
        enableSelect=new CheckBox("Enable")
        descriptionArea=new TextArea("Description")
        build()
        load()
    }

    def build() {
        setMargin(true)
        setSpacing(true)
        setSizeFull()
        datasourceSelect.setImmediate(true)
        datasourceSelect.addListener(this)
        descriptionArea.setWidth("70%")

        nameField.setWidth("70%")
        datasourceSelect.setWidth("70%")
        transportSelect.setWidth("70%")
        descriptionArea.setRows(15)
        addComponent(nameField)
        addComponent(datasourceSelect)
        addComponent(transportSelect)
        addComponent(enableSelect)
        addComponent(descriptionArea)
    }

    def load(){
        Collection datasources=meddela.database.getAll(DataSource.class)
        datasourceSelect.setContainerDataSource(new BeanItemContainer(DataSource.class,datasources))

        meddela.transportManager.listAvailableTransport().each {
            transportSelect.addItem(it)
        }

        nameField.setValue(notification.getName())
        datasourceSelect.setValue(notification.getDataSource())
        transportSelect.setValue(notification.transport)
        enableSelect.setValue(notification.isEnabled())
        descriptionArea.setValue(notification.getDescription())
        nameField.setReadOnly(!isNew)     //read only set after setting the value to be shown
                                            //prevent exception
    }

    void commit(){
       notification.setDescription(descriptionArea.getValue())
       notification.setDataSource(datasourceSelect.getValue())
       notification.setName(nameField.getValue())
       notification.setTransport(transportSelect.getValue())
       notification.setEnabled(enableSelect.getValue())
    }

    @Override
    void valueChange(Property.ValueChangeEvent event) {
        DataSource dataSource=event.property.getValue() as DataSource
        if(dataSource)
            notification.setDataSource(dataSource)
    }
}
