package com.niafikra.meddela.ui.vaadin

import com.niafikra.meddela.ui.vaadin.dashboard.Dashboard
import com.niafikra.meddela.ui.vaadin.login.LoginView
import com.vaadin.Application

import com.vaadin.ui.Window

//import com.vaadin.addon.toolbar.CssToolbar

/**
 * Manages different views to be shown to the user
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/18/12
 * Time: 8:29 AM
 */
class UIManager {

    Application application
    Dashboard dashboard
    LoginView loginView

    public UIManager(Application app) {
        application = app
        start();
    }

    /**
     * Start different views for the app
     */
    def start() {
        application.setTheme("meddela_dark")
        dashboard = new Dashboard(this)
        loginView = new LoginView(this)
        application.setMainWindow(dashboard)
    }
    /**
     * show login view for the app
     */
    void showLogin() {
        dashboard.removeAllComponents()
        dashboard.setContent(new LoginView(this))
    }

    /**
     * show meddella dashboard
     */
    void showDashBoard() {
        dashboard.removeAllComponents()
        dashboard.build()
    }


    void login(String username,String password) {
        def result = Controller.meddela.getAuthenticationManager()
                .authenticate(username,password)
        if (result) {
            showDashBoard()
        } else{
            dashboard.showNotification("Incorrect Username or Password!",Window.Notification.TYPE_WARNING_MESSAGE)
        }
    }


}
