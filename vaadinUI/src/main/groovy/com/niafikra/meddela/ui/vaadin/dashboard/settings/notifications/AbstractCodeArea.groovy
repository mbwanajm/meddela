package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.TextArea
import org.vaadin.aceeditor.AceEditor
import org.vaadin.aceeditor.gwt.ace.AceMode
import com.vaadin.ui.Button
import com.vaadin.ui.themes.Reindeer
import com.vaadin.ui.Alignment
import com.niafikra.meddela.data.Notification

/**
 *
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 6:10 PM
 */
abstract class AbstractCodeArea extends VerticalLayout implements NotificationCodeArea {

    AceEditor codeArea
    String type

    AbstractCodeArea(String type){
        super()
        this.type=type
        codeArea=new AceEditor()
        build()
    }

    def build() {

        setSpacing(true)
        setMargin(true)
        codeArea.setWidth("100%")
        codeArea.setSizeFull()
       // codeArea.setHeight("500px")

        if(type.equalsIgnoreCase("SQL"))
            codeArea.setMode(AceMode.sql)
        else if(type.equalsIgnoreCase("GROOVY"))
            codeArea.setMode(AceMode.groovy)

        addComponent(codeArea)
        setSizeFull()
    }


    @Override
    String getType() {
        return this.type
    }

    @Override
    Object getCode() {
        return this.codeArea.getValue().toString().trim()
    }

    @Override
    void setCode(Object code) {
        codeArea.setValue(code)
    }



}
