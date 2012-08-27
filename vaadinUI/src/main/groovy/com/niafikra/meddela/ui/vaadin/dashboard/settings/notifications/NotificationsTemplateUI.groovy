package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.data.Notification
import com.vaadin.ui.Accordion
import com.vaadin.ui.VerticalLayout

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com,bonifacechacha@niafikra.com>
 * Date: 8/26/12
 * Time: 2:27 PM
 */
class NotificationsTemplateUI extends VerticalLayout{

    TemplateCodeUI codeUI
    TemplateMessageUI messageUI
    boolean isNew

    NotificationsTemplateUI(Notification notification,boolean isNew){
        messageUI = new TemplateMessageUI(notification,isNew)
        codeUI = new TemplateCodeUI(notification,isNew,messageUI)
        this.isNew = isNew
        build()
    }

    def build() {
        setSizeFull()
        Accordion holder = new Accordion()
        holder.addTab(codeUI,"Template Variables")
        holder.addTab(messageUI,"Template Message")
        addComponent(holder)
        holder.setSizeFull()
    }

    def commit(){
        codeUI.commit()
        messageUI.commit()
    }
}