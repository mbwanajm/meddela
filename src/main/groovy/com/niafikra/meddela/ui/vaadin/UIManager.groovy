package com.niafikra.meddela.ui.vaadin

import com.niafikra.meddela.Controller
import com.niafikra.meddela.ui.vaadin.dashboard.Dashboard
import com.niafikra.meddela.ui.vaadin.login.LoginView
import com.vaadin.Application
import com.vaadin.ui.LoginForm

//import com.vaadin.addon.toolbar.CssToolbar

/**
 * Manages different views to be shown to the user
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/18/12
 * Time: 8:29 AM
 */
class UIManager implements LoginForm.LoginListener {

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
        application.setTheme("meddela")
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


    @Override
    void onLogin(LoginForm.LoginEvent event) {
        def result = Controller.meddela.getAuthenticationManager()
                .authenticate(event.getLoginParameter("username"), event.getLoginParameter("password"))
        if (result) {
            showDashBoard()
        }
    }


}
