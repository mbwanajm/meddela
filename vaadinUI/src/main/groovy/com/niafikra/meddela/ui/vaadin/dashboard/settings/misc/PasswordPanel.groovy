package com.niafikra.meddela.ui.vaadin.dashboard.settings.misc

import com.vaadin.ui.Panel
import com.vaadin.ui.Form
import com.vaadin.ui.TextField
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Alignment
import com.vaadin.data.validator.AbstractStringValidator
import com.vaadin.ui.PasswordField
import com.vaadin.ui.Window
import com.niafikra.meddela.ui.vaadin.meddelaVaadinApplication as MeddelaVaadinApplication
import com.niafikra.meddela.meddela as Meddela

/**
 * Author: Boniface Chacha <boniface.chacha@niafikra.com,bonifacechacha@gmail.com>
 * Date: 8/6/12
 * Time: 12:16 PM
 */
class PasswordPanel extends Panel implements Button.ClickListener {

    TextField username
    PasswordField password
    PasswordField rPassword
    Button update

    PasswordPanel() {
        super("Change Login Credentials")
        username = new TextField("Username")
        password = new PasswordField("Password")
        rPassword = new PasswordField("Repeat Password")
        update = new Button("Change Password")
        build()
        //load()
    }

    def build() {
        Form authForm = new Form()

        password.setImmediate(true)
        rPassword.setImmediate(true)
        password.setRequired(true)
        rPassword.setRequired(true)
        // authForm.addField("username", username)
        authForm.addField("password", password)
        authForm.addField("repeat password", rPassword)

        rPassword.addValidator(new AbstractStringValidator("passwords does not match") {
            @Override
            protected boolean isValidString(String value) {
                return (!value.trim().equalsIgnoreCase("")) && value.equalsIgnoreCase(password.getValue())
            }
        })

        username.addValidator(new AbstractStringValidator("incorrect username") {
            @Override
            protected boolean isValidString(String value) {
                return !value.isEmpty()
            }
        })

        HorizontalLayout footer = new HorizontalLayout()
        update.addListener(this)
        update.setWidth("200px")
        footer.addComponent(update)
        footer.setComponentAlignment(update, Alignment.BOTTOM_CENTER)
        authForm.setFooter(footer)
        addComponent(authForm)
    }

    @Override
    void buttonClick(Button.ClickEvent event) {
        if (rPassword.isValid()) {
            changePassword()
            getWindow().showNotification("Password Successfully changed")
        }
        else
            getWindow().showNotification("Please enter passwords correctly", Window.Notification.TYPE_WARNING_MESSAGE)
    }

    def load() {
        MeddelaVaadinApplication app = getApplication()
        username.setValue(app.currentUser.username)
    }

    void changePassword() {
        //temporary implimentation
        MeddelaVaadinApplication app = getApplication()
        // app.currentUser.setUsername(username.getValue())
        app.currentUser.setPassword(password.getValue())
        Meddela.authenticationManager.updateAuthentication(app.currentUser, true)

    }
}
