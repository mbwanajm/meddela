package com.niafikra.meddela.ui.vaadin.dashboard.settings.misc

import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout

/**
 * @author mbwana jaffari mbura
 * Date: 01/08/12
 * Time: 18:26
 */
class MiscPanel extends Panel{

    MiscPanel() {
        VerticalLayout content = new VerticalLayout()
        content.setSpacing(true)
        content.setMargin(true)
        setContent(content)
        setSizeFull()

        content.addComponent(new ThemesPanel())
        content.addComponent(new XchangePanel())
        content.addComponent(new PasswordPanel())
    }
}
