package com.niafikra.meddela.ui.vaadin.dashboard.settings.misc

import com.vaadin.ui.ComboBox
import com.vaadin.ui.VerticalLayout
import com.vaadin.data.Property

import com.vaadin.data.util.IndexedContainer
import com.niafikra.meddela.ui.vaadin.Controller
import com.vaadin.ui.Panel

/**
 * This class allows the user to change the misc
 *
 * @author mbwana jaffari mbura
 * Date: 21/07/12
 * Time: 12:26
 */
class ThemesPanel extends Panel implements Property.ValueChangeListener {
    ComboBox themeComboBox

    ThemesPanel() {
        buildUI()
    }

    void buildUI() {
        themeComboBox = new ComboBox('theme')
        themeComboBox.addListener(this)
        themeComboBox.setImmediate(true)
        addComponent(themeComboBox)

        loadThemes()
    }

    /**
     * load misc from misc directory
     */
    void loadThemes() {
        def sep = File.separator
        def path = Controller.appPath + sep + 'VAADIN' + sep + 'themes'
        File file = new File(path)

        def themes = []
        file.eachFile { File theme ->
            themes << theme.name
        }

        themeComboBox.setContainerDataSource(new IndexedContainer(themes))
        //   themeComboBox.setValue(Controller.getApplication().getTheme())
    }

    @Override
    void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.property.value) {
            getApplication().setTheme(themeComboBox.value)
        }
    }
}
