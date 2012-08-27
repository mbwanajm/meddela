package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.data.Template
import com.niafikra.meddela.data.SQL
import com.vaadin.data.Property
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.ui.*
import com.niafikra.meddela.meddela

import org.vaadin.peter.buttongroup.ButtonGroup
import com.niafikra.meddela.data.Notification

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/22/12
 * Time: 4:18 PM
 */
class TemplateSQLUI extends HorizontalLayout implements NotificationCodeArea, Button.ClickListener, Property.ValueChangeListener {

    ListSelect sqlSelect
    Template template
    CodeArea  codeArea
    Button add, delete, save
    TextField SQLNameField
    HashMap SQLSet = new HashMap()

    TemplateSQLUI() {
        sqlSelect = new ListSelect("SQL")
        codeArea = new CodeArea('SQL')   //SQL Code
       // codeArea.setCaption("SQL Code")
        SQLNameField = new TextField()
        SQLNameField.setInputPrompt("Enter Name of SQL")
        //SQLNameField.setWidth("150px")
        add = new Button("+")
        delete = new Button("-")
        save = new Button("save")
        build()
    }

    def build() {
        setWidth("100%")
        setHeight("100%")
        setSpacing(true)
        setMargin(true)
        VerticalLayout leftLay = new VerticalLayout()
        leftLay.setSpacing(true)
        sqlSelect.setWidth("100px")
        sqlSelect.setImmediate(true)
        sqlSelect.setRows(12)
        leftLay.addComponent(sqlSelect)
        ButtonGroup footer = new ButtonGroup()
        footer.addButton(add)
        footer.addButton(delete)
        leftLay.addComponent(footer)
        sqlSelect.setItemCaptionMode(ListSelect.ITEM_CAPTION_MODE_EXPLICIT)
        sqlSelect.setNullSelectionAllowed(false)
        //   leftLay.setHeight("100%")

        add.addListener(this as Button.ClickListener)
        delete.addListener(this as Button.ClickListener)
        save.addListener(this as Button.ClickListener)
        sqlSelect.addListener(this as Property.ValueChangeListener)
        VerticalLayout rightLay = new VerticalLayout()
        rightLay.setSpacing(true)
        //  Panel panel = new Panel("SQL Code")
        rightLay.setHeight("100%")
        //panel.addComponent(codeArea)
       // codeArea.setMode(AceMode.sql)
        codeArea.setWidth("100%")
        codeArea.setHeight("200px")
      //  codeArea.setTheme(AceTheme.tomorrow_night_eighties)
        rightLay.addComponent(codeArea)
        HorizontalLayout rightFooter = new HorizontalLayout()
        rightFooter.addComponent(SQLNameField)
        rightFooter.addComponent(save)
        rightFooter.setSpacing(true)
        rightLay.addComponent(rightFooter)
        rightLay.setWidth("100%")
        rightLay.setSizeFull()
        leftLay.setSizeFull()
        addComponent(leftLay)
        addComponent(rightLay)
        setExpandRatio(leftLay, 1)
        setExpandRatio(rightLay, 3)

    }

    @Override
    String getType() {
        return "SQL"
    }

    @Override
    Object getCode() {
        return SQLSet.values()
    }

    @Override
    void setCode(Object code) {
        Collection<SQL> SQLs=code as Collection
        for(SQL sql in SQLs)  {
            SQLSet.put(sql.name,sql)
        }
        sqlSelect.setContainerDataSource(new BeanItemContainer(SQL.class, SQLSet.values()))
        sqlSelect.setItemCaptionPropertyId("name")
    }

    @Override
    def execute(com.niafikra.meddela.data.Notification notification) {
        HashSet sqlSet=new HashSet()
        sqlSet.addAll(SQLSet.values())
        notification.template.sqls =sqlSet
        return meddela.composer.evaluateSQL(notification)
    }

    @Override
    void valueChange(Property.ValueChangeEvent event) {
        SQL theSQL = event.getProperty().value
        if (theSQL) {
            codeArea.setCode(theSQL.SQL)
            SQLNameField.setValue(theSQL.name)
        }
    }

    @Override
    void buttonClick(Button.ClickEvent event) {
        Button pressed = event.getButton()

        if (pressed.equals(add))
            addSQL()
        else if (pressed.equals(delete))
            removeSQL()
        else if (pressed.equals(save))
            saveSQL()
    }

    void saveSQL() {
        String SQLCode = codeArea.getCode()
        String SQLName = SQLNameField.getValue()

        if (!SQLCode.isEmpty()) {
            SQL sql = new SQL(SQLCode, SQLName)

            SQLSet.put(sql.name, sql)
            sqlSelect.setContainerDataSource(new BeanItemContainer(SQL.class, SQLSet.values()))
            sqlSelect.setItemCaptionPropertyId("name")
        }
        clearFields()
    }

    void removeSQL() {
        SQL sql = sqlSelect.getValue()
        SQLSet.remove(sql.name)
        sqlSelect.setContainerDataSource(new BeanItemContainer(SQL.class, SQLSet.values()))
        sqlSelect.setItemCaptionPropertyId("name")
    }

    void addSQL() {
        clearFields()
    }

    void clearFields() {
        codeArea.setCode("")
        SQLNameField.setValue("")
    }
}
