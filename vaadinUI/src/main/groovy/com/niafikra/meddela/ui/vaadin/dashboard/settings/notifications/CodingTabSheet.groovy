package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.TabSheet
import com.vaadin.ui.Button
import org.vaadin.peter.buttongroup.ButtonGroup
import com.vaadin.ui.TextArea
import com.vaadin.ui.Alignment

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com,bonifacechacha@niafikra.com>
 * Date: 8/25/12
 * Time: 11:31 AM
 *
 * UI for meddela code input for its notifications setups
 * contains place holder for groovy scripts and SQL code alongside the execute and ouput area
 *
 */
abstract class CodingTabSheet extends VerticalLayout implements Button.ClickListener{

    TabSheet codeSheet;
    Button testButton,popButton;
    TextArea outputArea;

    NotificationCodeArea sqlArea,groovyArea

    CodingTabSheet(){
        codeSheet = new TabSheet()
        testButton = new Button("Execute")
        popButton = new Button("Popup")
        outputArea = new TextArea()
    }

    def build(){
        codeSheet.setHeight("320px")
        testButton.setWidth("200px")
        popButton.setWidth("200px")
        outputArea.setWidth("100%")
        outputArea.setRows(4)

        addComponent(codeSheet)
        ButtonGroup footer = new ButtonGroup()
        footer.addButton(testButton)
        footer.addButton(popButton)
        addComponent(footer)
        setComponentAlignment(footer,Alignment.MIDDLE_CENTER)
        addComponent(outputArea)

        sqlArea.setSizeFull()
        groovyArea.setSizeFull()
//        println(sqlArea.toString() +" \n"+groovyArea.toString())
        codeSheet.addTab(sqlArea,"SQL")
        codeSheet.addTab(groovyArea,"GROOVY")

        testButton.addListener(this)
    }

    @Override
    void buttonClick(Button.ClickEvent clickEvent) {
        Button pressed = clickEvent.getButton()
        if(pressed.equals(testButton))
         onExecute()
        else
         popUp()
    }

    void popUp() {

    }

    abstract void onExecute();
}

