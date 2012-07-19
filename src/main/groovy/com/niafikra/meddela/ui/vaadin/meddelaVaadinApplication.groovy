package com.niafikra.meddela.ui.vaadin;


import com.vaadin.Application

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class meddelaVaadinApplication extends Application {
    private UIManager uiManager

    @Override
    public void init() {
        uiManager = new UIManager(this);
        uiManager.showLogin();
    }

}
