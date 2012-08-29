package com.niafikra.meddela.ui.vaadin.dashboard.settings.transport

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Table
import org.vaadin.peter.buttongroup.ButtonGroup
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.data.util.IndexedContainer
import com.vaadin.data.Item
import com.vaadin.ui.TextField
import com.vaadin.ui.HorizontalLayout
import com.vaadin.data.Property

/**
 * niafikra engineering

 * Author: Boniface Chacha <boniface.chacha@niafikra.com,bonifacechacha@gmail.com>
 * Date: 8/3/12
 * Time: 7:15 PM
 */
class ConfigurationList extends VerticalLayout implements Button.ClickListener, Property.ValueChangeListener {

    Button apply
    Table configurationList
    HashMap configurations
    String currentConf

    TextField confField, valueField

    ConfigurationList(HashMap configurations) {
        this.configurations = configurations
        apply = new Button("save")

        configurationList = new Table()

        confField = new TextField()
        valueField = new TextField()

        build()
        load()
    }

    def build() {
        setSpacing(true)
        setSizeFull()

        configurationList.setSizeFull()
        configurationList.setSelectable(true)
        configurationList.addListener(this)
        configurationList.setImmediate(true)
        addComponent(configurationList)
        setExpandRatio(configurationList, 1)
        IndexedContainer container = new IndexedContainer()
        container.addContainerProperty("configuration", String, "")
        container.addContainerProperty("value", String, "")
        configurationList.setContainerDataSource(container)

        confField.setInputPrompt("configuration")
        confField.setEnabled(false)
        valueField.setInputPrompt("value")
        //confField.setWidth("200px")
        valueField.setWidth("200px")
        confField.setNullRepresentation("")
        valueField.setNullRepresentation("")
        HorizontalLayout confPanelLay = new HorizontalLayout()
        confPanelLay.addComponent(confField)
        confPanelLay.addComponent(valueField)
        confPanelLay.addComponent(apply)
        confPanelLay.setSpacing(true)
        confPanelLay.setMargin(true)
        addComponent(confPanelLay)
        setComponentAlignment(confPanelLay, Alignment.MIDDLE_CENTER)

        apply.setWidth("100px")
        apply.addListener((Button.ClickListener) this)


    }


    @Override
    void buttonClick(Button.ClickEvent event) {
        Button pressedButton = event.getButton()
        if (pressedButton.equals(apply)) applyConf()
    }

    void applyConf() {
        if (confField.getValue()) {
            configurations?.put(confField.getValue(), valueField.getValue())
            load()
            clear()
        }
    }

    void removeConf() {
        if (currentConf) {
            configurations?.remove(currentConf)
            load()
            clear()
        }
    }

    void addConf() {
        clear()
    }

    def clear() {
        confField.setReadOnly(false)
        confField.setEnabled(false)
        confField.setValue(null)
        valueField.setValue(null)
    }

    def load() {
        IndexedContainer container = configurationList.getContainerDataSource()
        container.removeAllItems()
        for (Object key: configurations?.keySet()) {
            Item item = container.addItem(key)
            item.getItemProperty("configuration").setValue(key)
            item.getItemProperty("value").setValue(configurations.get(key))
        }
    }

    def setConfigurations(HashMap configurations){
        this.configurations=configurations
        load()
    }

    @Override
    void valueChange(Property.ValueChangeEvent event) {
        String conf = event.getProperty().getValue()
        if(conf){
        currentConf = conf
        confField.setEnabled(true)
        confField.setReadOnly(false)
        confField.setValue(conf)
        confField.setReadOnly(true)
        valueField.setValue(configurations.get(conf))
        }else clear()
    }

    def commit() {
        IndexedContainer container = configurationList.getContainerDataSource()

        for (String conf: container.getContainerPropertyIds()) {
            configurations.put(conf, container.getItem(conf).getItemProperty("value").getValue())
        }
    }
}
