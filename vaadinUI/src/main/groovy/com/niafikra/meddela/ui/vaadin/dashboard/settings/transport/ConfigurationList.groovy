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

    Button add, remove, apply
    Table configurationList
    HashMap configurations
    String currentConf

    TextField confField, valueField

    ConfigurationList(HashMap configurations) {
        this.configurations = configurations
        add = new Button("new")
        remove = new Button("remove")
        apply = new Button("add")

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
        valueField.setInputPrompt("value")
        confField.setWidth("200px")
        valueField.setWidth("200px")
        confField.setNullRepresentation("")
        valueField.setNullRepresentation("")
        HorizontalLayout confPanelLay = new HorizontalLayout()
        confPanelLay.addComponent(confField)
        confPanelLay.addComponent(valueField)
        confPanelLay.setSpacing(true)
        confPanelLay.setMargin(true)
        addComponent(confPanelLay)
        setComponentAlignment(confPanelLay, Alignment.MIDDLE_CENTER)

        add.setWidth("100px")
        remove.setWidth("100px")
        apply.setWidth("100px")

        add.addListener((Button.ClickListener) this)
        remove.addListener((Button.ClickListener) this)
        apply.addListener((Button.ClickListener) this)

        ButtonGroup footer = new ButtonGroup()
        footer.addButton(add)
        footer.addButton(remove)
        footer.addButton(apply)

        addComponent(footer)
        setComponentAlignment(footer, Alignment.MIDDLE_CENTER)

    }


    @Override
    void buttonClick(Button.ClickEvent event) {
        Button pressedButton = event.getButton()

        if (pressedButton.equals(add)) addConf()
        else if (pressedButton.equals(remove)) removeConf()
        else if (pressedButton.equals(apply)) applyConf()
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
        currentConf = conf
        confField.setValue(conf)
        valueField.setValue(configurations.get(conf))
    }

    def commit() {
        IndexedContainer container = configurationList.getContainerDataSource()

        for (String conf: container.getContainerPropertyIds()) {
            configurations.put(conf, container.getItem(conf).getItemProperty("value").getValue())
        }
    }
}
