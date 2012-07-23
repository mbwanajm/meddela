package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.TextArea
import com.vaadin.ui.Button

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 6:10 PM
 */
class DefaultCodeArea extends VerticalLayout implements CodeArea{

    TextArea codeArea
    String type

    DefaultCodeArea(String type){
        super()
        this.type=type
        codeArea=new TextArea()
        build()
    }

    def build() {
        setSpacing(true)
        setMargin(true)
        codeArea.setWidth("100%")
        codeArea.setHeight("500px")
        addComponent(codeArea)
        // setComponentAlignment(testButton,Alignment.MIDDLE_RIGHT)
    }


    @Override
    String getType() {
        return this.type
    }

    @Override
    Object getCode() {
        return this.codeArea.getValue()
    }

    @Override
    void setCode(Object code) {
        codeArea.setValue(code)
    }

}
