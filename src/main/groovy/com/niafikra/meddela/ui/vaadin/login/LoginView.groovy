package com.niafikra.meddela.ui.vaadin.login

import com.vaadin.ui.GridLayout
import com.vaadin.ui.LoginForm
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Alignment
import com.vaadin.ui.TextField
import com.vaadin.ui.PasswordField
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/19/12
 * Time: 4:26 PM
 */
class LoginView extends VerticalLayout{

    LoginForm loginForm
    LoginForm.LoginListener listener

    LoginView(LoginForm.LoginListener listener) {
        this.listener=listener
        build()
    }

    def build() {
        loginForm=new LoginForm()
        loginForm.setHeight("300px")
        loginForm.setWidth("200px")
        loginForm.addListener(listener)
        addComponent(loginForm)
        setComponentAlignment(loginForm,Alignment.MIDDLE_CENTER)
        setSizeFull()
     /*   addComponent(new TextField("Username"),4,4,6,4)
        addComponent(new PasswordField("Password"),4,5,6,5)
        addComponent(new Button("login"),5,6)
        setSizeFull()       */
    }

}
