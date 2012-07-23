package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.ListSelect
import com.vaadin.data.Property

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 11:14 AM
 */
class ScheduleField extends ListSelect implements Property.ValueChangeListener{

    HashMap values

    ScheduleField(String name,HashMap values){
          setCaption(name)
          values.put(name,"*")
          this.values=values
          build()
    }

    def build() {
        setImmediate(true)
        setMultiSelect(true)
        setHeight("150px")
        setWidth("100px")
        addListener((Property.ValueChangeListener)this)
    }

    void valueChange(Property.ValueChangeEvent event){
        Collection currentValues=event.getProperty().value
        StringBuffer value=new StringBuffer()
        int counter=0;
        currentValues.each {
            counter++
            if(counter!=1) value.append(",")
            value.append(getCRONValue(it))
        }

        values.put(getCaption(),value)
    }

    Object getCRONValue(Object val) {
       if (val instanceof Integer)
           return (val-1)
       else return val
    }
}
