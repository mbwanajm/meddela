package com.niafikra.meddela.ui.vaadin

import com.vaadin.Application
import com.vaadin.ui.Label
import com.vaadin.ui.Window

/**
 * Manages different views to be shown to the user
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/18/12
 * Time: 8:29 AM
 */
class UIManager {
    Application application

    public UIManager(Application app) {
        application = app
    }

    void showLogin() {
        Window mainWindow = new Window("meddela Welcome You")
        mainWindow.addComponent(new Label("Login Window"))
        application.setMainWindow(mainWindow)
    }
}
