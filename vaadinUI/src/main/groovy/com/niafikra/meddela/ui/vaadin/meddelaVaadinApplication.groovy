package com.niafikra.meddela.ui.vaadin;


import com.vaadin.Application
import com.vaadin.terminal.Terminal
import com.vaadin.ui.Window
import org.apache.log4j.Logger
import com.niafikra.meddela.data.security.Authentication

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class meddelaVaadinApplication extends Application {
    private UIManager uiManager
    private Logger logger=Logger.getLogger(meddelaVaadinApplication)
    Authentication currentUser

    @Override
    public void init() {
        uiManager = new UIManager(this);
        uiManager.showLogin();
    }

    @Override
    void terminalError(Terminal.ErrorEvent event) {
        getMainWindow().showNotification("Please review your request or contact administrator",
                                        Window.Notification.TYPE_ERROR_MESSAGE)
        logger.error("meddela has uncaught exception",event.throwable)
    }
    
    

}
