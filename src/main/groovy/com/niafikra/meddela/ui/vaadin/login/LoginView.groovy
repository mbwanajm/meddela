package com.niafikra.meddela.ui.vaadin.login

import com.vaadin.ui.GridLayout
import com.vaadin.ui.LoginForm
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Alignment
import com.vaadin.ui.TextField
import com.vaadin.ui.PasswordField
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Component
import com.niafikra.meddela.ui.vaadin.UIManager
import com.vaadin.event.ShortcutAction

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/19/12
 * Time: 4:26 PM
 */
class LoginView extends VerticalLayout implements Button.ClickListener{

    Component loginForm
    TextField userField
    PasswordField passwordField
    Button loginButton
    UIManager  uiManager

    LoginView(UIManager uiManager) {
        this.uiManager=uiManager
        build()
    }

    def build() {

        loginForm=getLoginForm()
        addComponent(loginForm)
        setComponentAlignment(loginForm,Alignment.MIDDLE_CENTER)
        setSizeFull()

    }

    def getLoginForm(){
        VerticalLayout loginForm=new VerticalLayout()
        loginForm.setWidth("400px")
        loginForm.setHeight("120px")
        loginForm.setStyleName("loginForm")
        loginForm.setSpacing(true)
        loginForm.setMargin(true)

        HorizontalLayout userBoxLay =new HorizontalLayout()
        Label userLabel=new Label("username")
        userLabel.setStyleName("loginLabel")
        userField=new TextField()
        userField.setStyleName("loginBox")
        userBoxLay.addComponent(userLabel)
        userBoxLay.addComponent(userField)
        userBoxLay.setSpacing(true)

        HorizontalLayout passBoxLay=new HorizontalLayout()
        Label passLabel=new Label("password")
        passLabel.setStyleName("loginLabel")
        passwordField=new PasswordField()
        passwordField.setStyleName("loginBox")
        passBoxLay.addComponent(passLabel)
        passBoxLay.addComponent(passwordField)
        passBoxLay.setSpacing(true)

        loginButton=new Button("login")
        loginButton.setWidth("130px")
        loginButton.setHeight("35px")
        loginButton.setStyleName("loginButton")
        loginButton.addListener(this)
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER)
        loginButton.addStyleName("primary")
        
        loginForm.addComponent(userBoxLay)
        loginForm.addComponent(passBoxLay)
        loginForm.addComponent(loginButton)
        loginForm.setComponentAlignment(loginButton,Alignment.BOTTOM_RIGHT)

        return loginForm
    }

    @Override
    void buttonClick(Button.ClickEvent event) {
        uiManager.login(userField.getValue().toString(),passwordField.getValue().toString())
    }
}
