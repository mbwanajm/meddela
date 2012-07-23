package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.TabSheet
import com.niafikra.meddela.data.Trigger

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 1:17 PM
 */
class TrigerSetup extends TabSheet{

    CodeArea sqlSetupView,groovySetupView
    Trigger trigger

    TrigerSetup(Trigger trigger){
        this.trigger=trigger
        sqlSetupView=new DefaultCodeArea("SQL")
        groovySetupView=new DefaultCodeArea("GROOVY")
        build()
        load()
    }

    def load() {
        sqlSetupView.setCode(trigger.getSql())
        groovySetupView.setCode(trigger.getGroovyCode())
    }

    def build() {
        setSizeFull()
        addTab(sqlSetupView,"SQL")
        addTab(groovySetupView,"GROOVY")
    }

    def commit(){
        trigger.setSql(sqlSetupView.code)
        trigger.setGroovyCode(groovySetupView.code)
    }

}
