package com.niafikra.meddela.ui.vaadin.login

import com.niafikra.meddela.ui.vaadin.UIManager
import com.vaadin.event.ShortcutAction
import com.vaadin.ui.*
import com.vaadin.terminal.ThemeResource

/**
 * This is the view when requesting user to login
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/19/12
 * Time: 4:26 PM
 */
class LoginView extends VerticalLayout implements Button.ClickListener {

    Component loginForm
    TextField userField
    PasswordField passwordField
    Button loginButton
    UIManager uiManager
    Label loginMsgLabel

    LoginView(UIManager uiManager) {
        this.uiManager = uiManager
        build()
    }

    def build() {
        loginMsgLabel = new Label('<b><font size="2">Welcome To meddela</font></b>',Label.CONTENT_XHTML)

        HorizontalLayout centeredLayout = new HorizontalLayout()
        centeredLayout.setSpacing(true)
        addComponent(centeredLayout)
        setComponentAlignment(centeredLayout, Alignment.MIDDLE_CENTER)

        VerticalLayout logoLayout = new VerticalLayout()
        logoLayout.setHeight('170px')
        logoLayout.setStyleName('login_logo')
        Embedded logo = new Embedded()
        logo.setSource(new ThemeResource('../meddela/images/logo.png'))
        logo.setWidth('134px')
        logoLayout.addComponent(logo)
        logoLayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER)
        centeredLayout.addComponent(logoLayout)

        loginForm = getLoginForm()
        centeredLayout.addComponent(loginForm)

        setSizeFull()

    }
    /**
     * Build the login Form
     * @return
     */
    def getLoginForm() {
        VerticalLayout loginForm = new VerticalLayout()
        loginForm.setWidth("400px")
        loginForm.setHeight("170px")
        loginForm.setStyleName("loginForm")
        loginForm.setSpacing(true)
        loginForm.setMargin(true)

        HorizontalLayout userBoxLay = new HorizontalLayout()
        Label userLabel = new Label("username")
        userLabel.setStyleName("loginLabel")
        userField = new TextField()
        userField.setStyleName("loginBox")
        userBoxLay.addComponent(userLabel)
        userBoxLay.addComponent(userField)
        userBoxLay.setSpacing(true)

        HorizontalLayout passBoxLay = new HorizontalLayout()
        Label passLabel = new Label("password")
        passLabel.setStyleName("loginLabel")
        passwordField = new PasswordField()
        passwordField.setStyleName("loginBox")
        passBoxLay.addComponent(passLabel)
        passBoxLay.addComponent(passwordField)
        passBoxLay.setSpacing(true)

        loginButton = new Button("login")
        loginButton.setWidth("130px")
      //  loginButton.setHeight("35px")
      //  loginButton.setStyleName("loginButton")
        loginButton.addListener(this)
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER)
      //  loginButton.addStyleName("primary")
        loginMsgLabel.setWidth("250px")
        loginForm.addComponent(loginMsgLabel)
        loginForm.setComponentAlignment(loginMsgLabel,Alignment.MIDDLE_RIGHT)
        loginForm.addComponent(userBoxLay)
        loginForm.addComponent(passBoxLay)
        loginForm.addComponent(loginButton)
        loginForm.setComponentAlignment(loginButton, Alignment.BOTTOM_RIGHT)

        return loginForm
    }

    @Override
    void buttonClick(Button.ClickEvent event) {
        def result=uiManager.login(userField.getValue().toString(), passwordField.getValue().toString())
        if(result)
            loginMsgLabel.setValue('<font color="green">Login Successfully, Please Wait ....</font>')
        else
            loginMsgLabel.setValue('<font color="#d23838" size="1">Incorrect Username or Password!</font>')

    }
}
