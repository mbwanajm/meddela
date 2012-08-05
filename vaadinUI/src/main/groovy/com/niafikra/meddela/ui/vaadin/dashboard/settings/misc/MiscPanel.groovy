package com.niafikra.meddela.ui.vaadin.dashboard.settings.misc

import com.vaadin.ui.Panel

/**
 * @author mbwana jaffari mbura
 * Date: 01/08/12
 * Time: 18:26
 */
class MiscPanel extends Panel{

    MiscPanel() {
        setSizeFull()

        addComponent(new ThemesPanel())
        addComponent(new XchangePanel())
    }
}
