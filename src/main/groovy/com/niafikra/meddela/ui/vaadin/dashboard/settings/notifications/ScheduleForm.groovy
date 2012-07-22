package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.niafikra.meddela.data.Trigger
import com.vaadin.data.Property
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import java.text.DateFormatSymbols

/**
 * A form for setting up cron task schedule
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/20/12
 * Time: 10:28 PM
 */
class ScheduleForm extends VerticalLayout implements Property.ValueChangeListener {

    HorizontalLayout footer

    HashMap<String, StringBuffer> fields = new HashMap<String, StringBuffer>()
    /**
     * Selectors for the field in the scheduling string
     */
    ScheduleField month, day, dayOfWeek, hour, minute

    TextField scheduleStringField
    Trigger trigger
    /**
     * Create the form
     */
    ScheduleForm(Trigger trigger) {
        this.trigger = trigger
        scheduleStringField = new TextField("CRON Schedule String")
        footer = new HorizontalLayout()
        month = new ScheduleField("Month", fields)
        day = new ScheduleField("Day", fields)
        dayOfWeek = new ScheduleField("Day Of Week", fields)
        hour = new ScheduleField("Hour", fields)
        minute = new ScheduleField("Minute", fields)
        build()
        load()
    }

    def build() {
        HorizontalLayout fieldsLayout = new HorizontalLayout()
        scheduleStringField.setWidth("100%")

        month.addListener(this)
        new DateFormatSymbols().shortMonths.each {month.addItem(it)}
        fieldsLayout.addComponent(month)

        day.addListener(this)
        (1..31).each {day.addItem(it)}
        fieldsLayout.addComponent(day)

        dayOfWeek.addListener(this)
        new DateFormatSymbols().shortWeekdays.each {dayOfWeek.addItem(it)}
        fieldsLayout.addComponent(dayOfWeek)

        hour.addListener(this)
        (1..24).each {hour.addItem(it) }
        fieldsLayout.addComponent(hour)

        minute.addListener(this)
        (1..60).each {minute.addItem(it)}
        fieldsLayout.addComponent(minute)
        fieldsLayout.setSpacing(true)
        fieldsLayout.setMargin(true)

        addComponent(fieldsLayout)
        addComponent(scheduleStringField)

    }

    def load(){
        scheduleStringField.setValue(trigger.getSchedule())
    }

    def commit(){
        trigger.schedule=scheduleStringField .getValue()
    }

    @Override
    void valueChange(Property.ValueChangeEvent event) {
        scheduleStringField.setValue(
                fields.get("Minute") + " " + fields.get("Hour") + " " + fields.get("Day") + " " + fields.get("Month") + " " + fields.get("Day Of Week"))
    }
}
