package com.niafikra.meddela.ui.vaadin.dashboard.settings.themes

import com.vaadin.ui.ComboBox
import com.vaadin.ui.VerticalLayout
import com.vaadin.data.Property
import com.niafikra.meddela.meddela
import com.vaadin.data.util.IndexedContainer
import com.niafikra.meddela.ui.vaadin.Controller

/**
 * This class allows the user to change the themes
 *
 * @author mbwana jaffari mbura
 * Date: 21/07/12
 * Time: 12:26
 */
class ThemesPanel extends VerticalLayout implements Property.ValueChangeListener {
    ComboBox themeComboBox

    ThemesPanel() {
        setSizeFull()
        setMargin(true)
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
     * load themes from themes directory
     */
    void loadThemes() {
        def sep = File.separator
        def path = Controller.appPath + sep + 'VAADIN' + sep + 'themes'
        File file = new File(path)

        def themes = []
        file.eachFile{ File theme ->
            themes <<  theme.name
        }

        themeComboBox.setContainerDataSource(new IndexedContainer(themes))
     //   themeComboBox.setValue(Controller.getApplication().getTheme())
    }

    @Override
    void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent.property.value){
            getApplication().setTheme(themeComboBox.value)
        }
    }
}
