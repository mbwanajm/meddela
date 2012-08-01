package com.niafikra.meddela.ui.vaadin.dashboard.settings.datasources

import com.vaadin.ui.DefaultFieldFactory
import com.vaadin.ui.FormFieldFactory
import com.vaadin.ui.Field
import com.vaadin.data.Item
import com.vaadin.ui.Component
import com.vaadin.ui.TextArea
import com.vaadin.ui.ComboBox
import com.vaadin.data.util.IndexedContainer
import com.vaadin.ui.TextField

/**
 *  The form field factory for datasource form
 *
 * @author mbwana jaffari mbura
 * Date: 21/07/12
 * Time: 14:55
 */
class DataSourceFormFieldFactory implements FormFieldFactory{


    @Override
    Field createField(Item item, Object propertyId, Component component) {
        switch(propertyId){
            case 'description':
                TextArea textArea = new TextArea("description")
                textArea.setWidth("300px")
                textArea.setRows(8)
                return textArea

            case 'driver':
                ComboBox comboBox = new ComboBox('driver')
                comboBox.setContainerDataSource(new IndexedContainer(['com.mysql.jdbc.Driver']))
                comboBox.setWidth('300px')
                return comboBox

            default:
                TextField textField = new TextField(propertyId)
                textField.setWidth('300px')
                return textField

        }
    }
}
