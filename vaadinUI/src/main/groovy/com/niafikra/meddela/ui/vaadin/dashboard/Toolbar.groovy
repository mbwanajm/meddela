package com.niafikra.meddela.ui.vaadin.dashboard

import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Embedded
import com.vaadin.ui.HorizontalLayout

/**
 * Manages the basic task which user can perform in the system
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/18/12
 * Time: 8:16 PM
 */
class Toolbar extends HorizontalLayout implements Button.ClickListener {

    Button home, settings, reports, logout
    Dashboard dashboard

    Toolbar(Dashboard dashboard) {
        this.dashboard = dashboard
        build()
    }

    /**
     * build the UI look
     */
    def build() {
        setWidth("100%")

        home = new Button("Home")
        home.setIcon(new ThemeResource("images/home.png"))
        settings = new Button("Settings")
        settings.setIcon(new ThemeResource("images/settings.png"))
        reports = new Button("Reports")
        reports.setIcon(new ThemeResource("images/reports.png"))
        logout = new Button("Logout")
        logout.setIcon(new ThemeResource("images/logout.png"))

        home.addListener(this)
        settings.addListener(this)
        reports.addListener(this)
        logout.addListener(this)

        addComponent(home)
        addComponent(reports)
        addComponent(settings)

        Embedded logo = new Embedded()
        addComponent(logo)
        setExpandRatio(logo, 1)
        setComponentAlignment(logo, Alignment.TOP_RIGHT)
        addComponent(logout)
        setStyleName("toolbar")

    }


    @Override
    void buttonClick(Button.ClickEvent event) {
        Button pressedButton = event.getButton()

        if (pressedButton.equals(logout)) {
            dashboard.getUiManager().showLogin()
        }
        else if (pressedButton.equals(home)) {
            dashboard.showHomeView()
        }
        else if (pressedButton.equals(settings)) {
            dashboard.showSettingsView()
        }
        else if (pressedButton.equals(reports)) {
            dashboard.showReportsView()
        }

    }
}
