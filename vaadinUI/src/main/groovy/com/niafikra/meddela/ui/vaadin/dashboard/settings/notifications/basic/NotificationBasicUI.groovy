package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.basic

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
import com.niafikra.meddela.ui.vaadin.dashboard.settings.transport.ConfigurationList
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.themes.Reindeer
import com.vaadin.ui.Alignment
import com.vaadin.ui.Window
import com.niafikra.meddela.data.TransportInfo
/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 3:31 PM
 */
class NotificationBasicUI extends FormLayout implements
Property.ValueChangeListener, Button.ClickListener {

    TextField nameField
    ComboBox datasourceSelect
    ComboBox transportSelect
    CheckBox enableSelect
    TextArea descriptionArea
    Notification notification
    Button configButton
    boolean isNew

    NotificationBasicUI(Notification notification, isNew) {
        this.isNew = isNew
        this.notification = notification
        nameField = new TextField("Name")
        datasourceSelect = new ComboBox("Data Source")
        transportSelect = new ComboBox("Transport")
        enableSelect = new CheckBox("Enable")
        descriptionArea = new TextArea("Description")
        configButton = new Button("configure")
        build()
        load()
    }

    def build() {
        setMargin(true)
        setSpacing(true)
        setSizeFull()
        datasourceSelect.setImmediate(true)
        datasourceSelect.addListener((Property.ValueChangeListener) this)
        descriptionArea.setWidth("70%")

        nameField.setWidth("70%")
        datasourceSelect.setWidth("70%")
        descriptionArea.setRows(15)
        addComponent(nameField)
        addComponent(datasourceSelect)

        HorizontalLayout trasportLay = new HorizontalLayout()
        trasportLay.setWidth("70%")
        trasportLay.setSpacing(true)
        trasportLay.addComponent(transportSelect)
        configButton.addStyleName(Reindeer.BUTTON_SMALL)
        trasportLay.addComponent(configButton)
        configButton.addListener((Button.ClickListener) this)
        trasportLay.setExpandRatio(transportSelect, 1)
        transportSelect.setWidth("100%")
        trasportLay.setComponentAlignment(configButton, Alignment.BOTTOM_RIGHT)
        addComponent(trasportLay)
        addComponent(enableSelect)
        addComponent(descriptionArea)
    }

    def load() {
        Collection datasources = meddela.database.getAll(DataSource.class)
        datasourceSelect.setContainerDataSource(new BeanItemContainer(DataSource, datasources))

        transportSelect.setContainerDataSource(
                new BeanItemContainer(TransportInfo,meddela.transportManager.listAvailableTransport()))

        nameField.setValue(notification.getName())
        datasourceSelect.setValue(notification.getDataSource())
        transportSelect.setValue(notification.transport)
        enableSelect.setValue(notification.isEnabled())
        descriptionArea.setValue(notification.getDescription())
        nameField.setReadOnly(!isNew)     //read only set after setting the value to be shown
        //prevent exception
    }

    void commit() {
        notification.setDescription(descriptionArea.getValue())
        notification.setDataSource(datasourceSelect.getValue())
        notification.setName(nameField.getValue())
        notification.setTransport(transportSelect.getValue())
        notification.setEnabled(enableSelect.getValue())
    }

    @Override
    void valueChange(Property.ValueChangeEvent event) {
        Property selectedField = event.getProperty()
        if (selectedField == datasourceSelect) {
            DataSource dataSource = event.property.getValue() as DataSource
            if (dataSource)
                notification.setDataSource(dataSource)
        } else if (selectedField == transportSelect) {
            TransportInfo transport = transportSelect.getValue()
            if (transport)
                notification.setTransport(transport)
        }
    }

    @Override
    void buttonClick(Button.ClickEvent event) {
        TransportInfo transport = transportSelect.getValue()
        if (transport) {
            Window configWindow = new Window()
            configWindow.setModal(true)
            configWindow.center()
            configWindow.setWidth("40%")
            configWindow.setHeight("50%")
            ConfigurationList confList = new ConfigurationList(notification.getTansportCofiguration(transport.name))
            configWindow.setContent(confList)
            getWindow().addWindow(configWindow)
        }
    }
}
