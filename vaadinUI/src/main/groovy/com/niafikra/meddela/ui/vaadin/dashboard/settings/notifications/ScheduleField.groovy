package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.ListSelect
import com.vaadin.data.Property

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 11:14 AM
 */
class ScheduleField extends ListSelect implements Property.ValueChangeListener {

    HashMap values

    ScheduleField(String name, HashMap values) {
        setCaption(name)
        values.put(name, "*")
        this.values = values
        build()
    }

    def build() {
        setImmediate(true)
        setMultiSelect(true)
        setNullSelectionAllowed(false)
        setHeight("150px")
        setWidth("100px")
        addListener((Property.ValueChangeListener) this)
    }

    void valueChange(Property.ValueChangeEvent event) {
        Collection currentValues = event.getProperty().value
        StringBuffer value = new StringBuffer()
        int counter = 0;
        currentValues.each {
            if (it) {
                counter++
                if (counter != 1) value.append(",")
                value.append(getCRONValue(it))
            }
        }

        values.put(getCaption(), value)
    }

    /**
     * CRON have defined a set of symbols
     * The symbols have been transformed in the Human readable text for readal=bility
     * This method does the conversion back to CRON text
     * @param value
     * @return
     */
    Object getCRONValue(Object value) {
        if (value.toString().equalsIgnoreCase("last")) return "L"
        if (value.toString().equalsIgnoreCase("every")) return "*"
        else return value
    }
}
