package com.niafikra.meddela.ui.vaadin.dashboard.reports

import com.vaadin.ui.VerticalLayout

import com.vaadin.ui.ComboBox
import com.vaadin.ui.DateField
import com.vaadin.ui.Panel
import com.vaadin.ui.HorizontalLayout

import com.vaadin.ui.Label
import com.vaadin.ui.Table
import com.vaadin.data.Property
import com.vaadin.data.util.IndexedContainer
import com.niafikra.meddela.meddela
import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.data.UniqueRecepient
import com.vaadin.data.util.BeanItemContainer
import com.niafikra.meddela.data.SentNotification
import org.neodatis.odb.ODB
import org.neodatis.odb.core.query.criteria.Where
import org.neodatis.odb.core.query.criteria.And
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery
import com.vaadin.ui.Window

/**
 * This class allows the use to view the sent out notifications
 * and filter them to his will
 *
 * @author mbwana jaffari mbura
 * Date: 20/07/12
 * Time: 20:00
 */
class SentNotificationReportPanel extends VerticalLayout implements Property.ValueChangeListener {
    private DateField startDateField;
    private DateField endDateField;
    private ComboBox notificationComboBox;
    private ComboBox statusComboBox;
    private ComboBox recepientComboBox;
    private Table resultsTable;
    private Label spacer
    private static final def visibleProperties = ['time', 'notification', 'receiver', 'delivered'].toArray()

    SentNotificationReportPanel() {
        setSizeFull()
        setMargin(true)
        setSpacing(true)

        buildUI()
    }

    void buildUI() {
        Panel filterPanel = new Panel()
        HorizontalLayout filterLayout = new HorizontalLayout()
        filterLayout.setMargin(true)
        filterLayout.setSpacing(true)
        filterLayout.setSizeFull()
        filterPanel.setContent(filterLayout)

        addComponent(filterPanel)

        startDateField = buildDateFields("start date")
        endDateField = buildDateFields("end date")
        filterPanel.addComponent(startDateField)
        filterPanel.addComponent(endDateField)

        spacer = new Label()
        spacer.setStyleName('reportsLabel')
        spacer.setContentMode(Label.CONTENT_XHTML)
        spacer.setSizeFull()
        filterPanel.addComponent(spacer)
        filterLayout.setExpandRatio(spacer, 1)

        notificationComboBox = new ComboBox("notification")
        notificationComboBox.addListener(this)
        notificationComboBox.setImmediate(true)
        filterPanel.addComponent(notificationComboBox)

        recepientComboBox = new ComboBox("recepient")
        recepientComboBox.addListener(this)
        recepientComboBox.setImmediate(true)
        filterPanel.addComponent(recepientComboBox)

        statusComboBox = new ComboBox("status")
        statusComboBox.setImmediate(true)
        statusComboBox.addListener(this)
        filterPanel.addComponent(statusComboBox)

        buildResultsTable()
        loadComboBoxes()

    }

    void buildResultsTable() {
        resultsTable = new Table()
        resultsTable.setContainerDataSource(new BeanItemContainer(SentNotification))
        resultsTable.setVisibleColumns(visibleProperties)
        resultsTable.setSizeFull()
        resultsTable.setSelectable(true)
        resultsTable.setImmediate(true)
        resultsTable.addListener(this)

        addComponent(resultsTable)
        setExpandRatio(resultsTable, 1)

    }

    void loadComboBoxes() {
        // load the notifications combobox
        def notificationsInDb = meddela.database.getODB().getObjects(Notification)

        def notifications = ['all']
        if (notificationsInDb) {
            for (notification in notificationsInDb) {
                notifications << notification.name
            }
        }

        notificationComboBox.setContainerDataSource(new IndexedContainer(notifications))
        notificationComboBox.setValue('all')

        // load the recepients combobox
        def recepientsInDb = meddela.database.getODB().getObjects(UniqueRecepient)
        def recepients = ['all']
        if (recepientsInDb) {
            for (recepient in recepientsInDb) {
                recepients << recepient.recepient
            }
        }

        recepientComboBox.setContainerDataSource(new IndexedContainer(recepients))
        recepientComboBox.setValue('all')

        // load the status combobox
        statusComboBox.setContainerDataSource(new IndexedContainer(['all', 'delivered', 'failed']))
        statusComboBox.setValue('all')

    }

    private DateField buildDateFields(caption) {
        DateField dateField = new DateField(caption)
        dateField.setWidth("150px")
        dateField.setResolution(DateField.RESOLUTION_DAY)
        dateField.addListener(this)
        dateField.setImmediate(true)
        return dateField
    }

    @Override
    void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.property != resultsTable) {
            reload()
        } else {
            showSentNotificationDetails(valueChangeEvent.property.value)
        }
    }

    void showSentNotificationDetails(SentNotification sentNotification) {
        if(!sentNotification) return

        Window notificationDetailsWindow = new Window("Sent Notification: $sentNotification.notification.name")
        notificationDetailsWindow.setModal(true)
        notificationDetailsWindow.setWidth("400px")

        Label detailsLabel = new Label()
        detailsLabel.setContentMode(Label.CONTENT_XHTML)
        detailsLabel.setValue("""
        <b>recepient:</b> ${sentNotification.receiver}<br>
        <b>time sent:</b> ${sentNotification.time.format('dd-MMM-yyyy hh:mm')}<br>
        <b>delivered:</b> ${sentNotification.delivered? 'yes' : 'failed'}
        <br><hr><br>
        ${sentNotification.content.replace('\n', '<br>')}

        """)
        notificationDetailsWindow.content.setMargin(true)
        notificationDetailsWindow.addComponent(detailsLabel)
        getWindow().addWindow(notificationDetailsWindow)

    }

    void reload() {
        if (!endDateField.getValue() || !startDateField.getValue()) {
            spacer.setValue('please select a date range')
            return
        }

        // Query db using the given filters
        And andCriteria = Where.and()
        andCriteria
                .add(Where.ge('time', startDateField.getValue().clearTime()))
                .add(Where.le('time', endDateField.getValue().clearTime()))

        if (!notificationComboBox.getValue().equals('all'))
            andCriteria.add(Where.equal('notification.name', notificationComboBox.getValue()))

        if (!recepientComboBox.getValue().equals('all'))
            andCriteria.add(Where.equal('receiver', recepientComboBox.getValue()))

        if (!statusComboBox.getValue().equals('all'))
            andCriteria.add(Where.equal('delivered', statusComboBox.getValue().equals('delivered') ? true : false))

        Collection results
        meddela.database.runDbAction {odb ->
            results = odb.getObjects(new CriteriaQuery(SentNotification, andCriteria))
        }

        // reload the table
        BeanItemContainer container = resultsTable.containerDataSource
        container.removeAllItems()
        if (!results?.isEmpty()) {
            container.addAll(results)
            spacer.setValue("${results.size()} notifications found")
        } else {
            spacer.setValue('no sent notifications found')
        }
    }


}
