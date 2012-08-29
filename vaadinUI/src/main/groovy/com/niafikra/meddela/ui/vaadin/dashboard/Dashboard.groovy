package com.niafikra.meddela.ui.vaadin.dashboard

import com.vaadin.ui.Window
import com.vaadin.ui.VerticalLayout

import com.vaadin.ui.Component
import com.niafikra.meddela.ui.vaadin.UIManager
import com.niafikra.meddela.ui.vaadin.dashboard.reports.ReportView
import com.niafikra.meddela.ui.vaadin.dashboard.settings.SettingsView

/**
 * Base Vaadin UI for managing the task of the meddela app
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/18/12
 * Time: 8:14 PM
 */
class Dashboard extends Window {

    Toolbar toolbar
    UIManager uiManager
    Component settingsView, reportsView, homeView
    VerticalLayout mainLayout

    Dashboard(UIManager uiManager) {
        this.uiManager = uiManager
        setCaption("meddela dashboard")
        setName("dashboard")
    }

    def build() {
        this.settingsView = new SettingsView();
        reportsView = new ReportView()
        homeView = new HomeView()

        this.toolbar = new Toolbar(this)
        mainLayout = new VerticalLayout()
        mainLayout.setSizeFull();
        setContent(mainLayout)
        mainLayout.addComponent(toolbar)
        mainLayout.addComponent(homeView)
        mainLayout.setExpandRatio(homeView, 1)
    }

    def showSettingsView() {
        showView(settingsView)
    }

    def showReportsView() {
        showView(reportsView)
    }

    def showHomeView() {
        showView(homeView)
    }

    def showView(Component view) {
        mainLayout.removeComponent(mainLayout.getComponent(1))
        mainLayout.addComponent(view)
        mainLayout.setExpandRatio(view, 1)
    }

}
