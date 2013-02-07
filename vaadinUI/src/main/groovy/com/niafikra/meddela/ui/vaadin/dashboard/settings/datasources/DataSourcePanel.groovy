package com.niafikra.meddela.ui.vaadin.dashboard.settings.datasources

import com.vaadin.ui.GridLayout
import com.vaadin.ui.Button
import com.vaadin.ui.Form
import com.vaadin.ui.Panel
import com.vaadin.data.util.BeanItem
import com.niafikra.meddela.data.DataSource
import com.vaadin.ui.ListSelect
import com.vaadin.ui.HorizontalLayout
import org.vaadin.peter.buttongroup.ButtonGroup
import com.vaadin.ui.VerticalLayout
import com.niafikra.meddela.meddela
import com.niafikra.meddela.utilities.SqlUtil
import com.niafikra.meddela.data.Notification as meddelaNotification
import com.vaadin.ui.Window
import org.neodatis.odb.ODB
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.data.Property

/**
 * This class can be used to edit  the datasources
 * @author mbwana jaffari mbura
 * Date: 21/07/12
 * Time: 12:54
 */
class DataSourcePanel extends HorizontalLayout implements Button.ClickListener, Property.ValueChangeListener {
    ListSelect list;

    private Form dataSourceForm
    private Button testButton
    private Button saveButton
    private Button newButton
    private Button deleteButton
    private static final visibleFields = ['name', 'description', 'url', 'username', 'password', 'driver'].toArray()
    private boolean newMode = true;

    DataSourcePanel() {
        setSizeFull()
        setMargin(true)
        setSpacing(true)

        buildUI()
    }

    void buildUI() {
        list = new ListSelect();
        list.setSizeFull()
        list.setContainerDataSource(new BeanItemContainer(DataSource))
        list.setImmediate(true)
        list.addListener(this)
        addComponent(list)
        setExpandRatio(list, 2)
        reloadList()

        VerticalLayout form = new VerticalLayout()
        form.setSpacing(true)
        form.setSizeFull()
        addComponent(form)
        setExpandRatio(form, 8)

        Panel formPanel = new Panel()
        formPanel.setSizeFull()

        dataSourceForm = new Form()
        dataSourceForm.setFormFieldFactory(new DataSourceFormFieldFactory())
        dataSourceForm.setItemDataSource(new BeanItem<DataSource>(new DataSource()))

        dataSourceForm.setVisibleItemProperties(visibleFields)
        formPanel.addComponent(dataSourceForm)
        addComponent(formPanel)

        ButtonGroup buttonGroup = new ButtonGroup()
        newButton = new Button("create")
        newButton.setWidth("100px")
        newButton.addListener((Button.ClickListener) this)
        buttonGroup.addButton(newButton)

        deleteButton = new Button("delete")
        deleteButton.addListener((Button.ClickListener) this)
        deleteButton.setWidth("100px")
        buttonGroup.addButton(deleteButton)

        saveButton = new Button("save")
        saveButton.addListener((Button.ClickListener) this)
        saveButton.setWidth("100px")
        buttonGroup.addButton(saveButton)

        testButton = new Button("test")
        testButton.addListener((Button.ClickListener) this)
        testButton.setWidth("100px")
        buttonGroup.addButton(testButton)

        form.addComponent(formPanel)
        form.addComponent(buttonGroup)
        form.setExpandRatio(formPanel, 1)
    }

    @Override
    void buttonClick(Button.ClickEvent clickEvent) {
        switch (clickEvent.button) {
            case newButton:
                dataSourceForm.setItemDataSource(new BeanItem<DataSource>(new DataSource()))
                dataSourceForm.setVisibleItemProperties(visibleFields)
                newMode = true
                dataSourceForm.getField('name').setReadOnly(false)
                break

            case deleteButton:
                if (list.value){
                    def result = meddela.database.runDbAction{odb -> odb.delete(list.value)}
                    showActionResultMessage(result)
                }
                break

            case saveButton:
                saveDataSource()
                break

            case testButton:
                testDataSource()
                break
        }
    }

    def saveDataSource() {
        dataSourceForm.commit()
        DataSource dataSource = dataSourceForm.getItemDataSource().getBean()
        def result
        if (newMode) {
            result = meddela.database.runDbAction { odb -> odb.store(dataSource)}
            newMode = false
            dataSourceForm.getField('name').setReadOnly(true)

        } else {
            result = meddela.database.runDbAction { odb -> meddela.database.update(dataSource, 'name')}
        }

         showActionResultMessage(result)

    }

    def showActionResultMessage(result){
        if (result) {
            reloadList()
            getWindow().showNotification("Action succesful")
        } else {
            getWindow().showNotification("Action failed", Window.Notification.TYPE_ERROR_MESSAGE)
        }
    }


    void reloadList() {
        meddela.database.runDbAction {ODB odb ->
            Collection datasources = odb.getObjects(DataSource)
            list.containerDataSource.removeAllItems()
            if (!datasources?.isEmpty()) {
                list.containerDataSource.addAll(datasources)
            }
        }
    }

    void testDataSource() {
        def notification = new meddelaNotification()
        notification.setDataSource(dataSourceForm.getItemDataSource().getBean())

        def result = SqlUtil.runWithSqlConnection(notification) {sql, notif -> true}
        if (result instanceof Boolean) {  // if all goes well we get true
            getWindow().showNotification("Connected Succesfully")
        } else {
            getWindow().showNotification("Connection test failed", Window.Notification.TYPE_ERROR_MESSAGE)
        }
    }

    /**
     * Called when someone selects a value from the list boc
     *
     * @param valueChangeEvent
     */
    @Override
    void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        if (list.value) {
            dataSourceForm.setItemDataSource(new BeanItem(list.value))
            dataSourceForm.setVisibleItemProperties(visibleFields)
            newMode = false
            dataSourceForm.getField('name').setReadOnly(true)

        }
    }
}
