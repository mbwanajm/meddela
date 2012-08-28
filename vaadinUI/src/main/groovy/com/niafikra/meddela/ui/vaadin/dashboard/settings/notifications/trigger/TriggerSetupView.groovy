package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications.trigger

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.TextArea
import com.vaadin.ui.Button

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 1:25 PM
 */
class TriggerSetupView extends VerticalLayout{
    TextArea codeArea
    Button testButton
    String type

    TriggerSetupView(String type){
        super()
        this.type=type
        codeArea=new TextArea()
        testButton=new Button("TEST")
        build()
    }

    def build() {
        setSpacing(true)
        setMargin(true)
        codeArea.setWidth("100%")
        codeArea.setHeight("500px")
        addComponent(codeArea)
        addComponent(testButton)
       // setComponentAlignment(testButton,Alignment.MIDDLE_RIGHT)
    }


}
