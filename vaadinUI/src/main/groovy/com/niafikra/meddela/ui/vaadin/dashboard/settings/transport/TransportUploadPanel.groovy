package com.niafikra.meddela.ui.vaadin.dashboard.settings.transport

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Upload
import com.vaadin.ui.ListSelect
import com.niafikra.meddela.meddela
import groovy.io.FileType
import com.vaadin.ui.Window
import com.vaadin.ui.Panel
import java.nio.file.Files
import org.apache.commons.io.FileUtils
import com.niafikra.meddela.services.TransportManager
import com.vaadin.ui.themes.Reindeer
import com.vaadin.ui.Button
import org.vaadin.peter.buttongroup.ButtonGroup
import com.vaadin.ui.Alignment
import com.vaadin.data.Property
import com.vaadin.data.util.BeanItemContainer
import com.niafikra.meddela.data.TransportInfo
import com.vaadin.ui.Label

/**
 * niafikra engineering

 * Author: Boniface Chacha <boniface.chacha@niafikra.com,bonifacechacha@gmail.com>
 * Date: 8/1/12
 * Time: 6:47 AM
 */
class TransportUploadPanel extends HorizontalLayout
implements Upload.FailedListener, Upload.SucceededListener,
Property.ValueChangeListener, Button.ClickListener {
    Upload transportUpload;
    ListSelect transportList;
    ConfigurationList transportConfigs
    Label transportMessageLabel
    Button save, remove
    Panel configPanel

    public TransportUploadPanel() {
        transportUpload = new Upload("Upload Transport Plugin jar", new UploadReceiver(meddela.transportManager.transportsPath))
        transportList = new ListSelect("Available Transport Plugins")
        transportConfigs = new ConfigurationList(null)
        transportMessageLabel = new Label("No Transport Selected",Label.CONTENT_XHTML)
        save = new Button("Save")
        remove = new Button("Remove")
        build()
        load()
    }

    def build() {
        setSizeFull()
        setMargin(true)
        setSpacing(true)
        VerticalLayout transListLay = new VerticalLayout()
        transListLay.setSpacing(true)
        transListLay.addComponent(transportList)
        ButtonGroup footer = new ButtonGroup()
        save.setWidth("100px")
        remove.setWidth("100px")
        save.addListener((Button.ClickListener) this)
        remove.addListener((Button.ClickListener) this)
        footer.addButton(save)
        footer.addButton(remove)
        transListLay.addComponent(footer)
        transListLay.setComponentAlignment(footer, Alignment.BOTTOM_CENTER)
        BeanItemContainer container = new BeanItemContainer(TransportInfo)
        transportList.setContainerDataSource(container)
        transportList.setSizeFull()
        transportList.setNullSelectionAllowed(false)
        transportList.addListener(this)
        transportList.setImmediate(true)
        transListLay.setSizeFull()
        transListLay.setExpandRatio(transportList, 1)
        addComponent(transListLay)

        VerticalLayout transportInfoLay = new VerticalLayout()
        transportInfoLay.setSizeFull()
        transportInfoLay.setSpacing(true)
        configPanel = new Panel("Transport Plugin Configurations")
        configPanel.setContent(transportConfigs)
        configPanel.setStyleName(Reindeer.PANEL_LIGHT)
        configPanel.setSizeFull()
        transportInfoLay.addComponent(configPanel)

        Panel messagePanel = new Panel()
        messagePanel.addComponent(transportMessageLabel)
        transportInfoLay.addComponent(messagePanel)

        Panel uploadingPanel = new Panel("Add Transport Plugin")
        uploadingPanel.setSizeFull()
        transportUpload.setWidth("50%")
        uploadingPanel.addComponent(transportUpload)
        transportUpload.addListener((Upload.SucceededListener) this)
        transportUpload.addListener((Upload.FailedListener) this)
        transportInfoLay.addComponent(uploadingPanel)
        transportInfoLay.setExpandRatio(configPanel, 2)
        transportInfoLay.setExpandRatio(uploadingPanel, 1)
        addComponent(transportInfoLay)
        setExpandRatio(transportInfoLay, 8)
        setExpandRatio(transListLay, 2)
    }

    def load() {
        Collection transports = meddela.transportManager.listAvailableTransport()
        BeanItemContainer container = transportList.getContainerDataSource()
        container.removeAllItems()
        container.addAll(transports)
    }

    @Override
    void uploadFailed(Upload.FailedEvent event) {
        getWindow().showNotification("Transport Plugin Upload Failed!", event.reason.toString(), Window.Notification.TYPE_ERROR_MESSAGE)
    }

    @Override
    void uploadSucceeded(Upload.SucceededEvent event) {
        TransportManager transportManager = meddela.transportManager
        if (event.MIMEType.contains("java-archive")) {
            if (transportManager.installTransportPlugin(event.filename)) {
                load()
                getWindow().showNotification("Transport Plugin installed Successfully!", Window.Notification.TYPE_HUMANIZED_MESSAGE)
            }
            else {
                getWindow().showNotification("Transport Plugin Failed to be installed!", "Jar File does not contain a valid meddela transport", Window.Notification.TYPE_ERROR_MESSAGE)

            }
        } else {
            getWindow().showNotification("Transport Plugin Failed to be installed!", "File is not a valid meddela transport jar", Window.Notification.TYPE_ERROR_MESSAGE)
            FileUtils.deleteQuietly(new File(transportManager.transportsPath + File.separator + event.filename))
        }
    }

    @Override
    void valueChange(Property.ValueChangeEvent event) {
        TransportInfo transportInfo = event.getProperty().value
        configPanel.setCaption(transportInfo.toString()+" ,"+"Transport Plugin Configurations")
        transportConfigs.setConfigurations(transportInfo.configurations)
        transportMessageLabel.setValue(meddela.transportManager.getTransport(transportInfo.name)?.message)
    }

    @Override
    void buttonClick(Button.ClickEvent event) {
        TransportInfo transportInfo = transportList.getValue();
        Button pressedButton = event.getButton()
        if (pressedButton.equals(save)) {
            if (transportInfo)
                saveTransportInfo(transportInfo)
        } else {
            if (pressedButton.equals(remove)) {
                if (transportInfo) {
                    removeTransportInfo(transportInfo)
                }
            }
        }


    }

    void removeTransportInfo(TransportInfo transportInfo) {
        TransportManager transportManager = meddela.transportManager
        if (transportManager.removeTransport(transportInfo)) {
            load()
            getWindow().showNotification("Transport Plugin Configuration removed successfully")
        } else {
            getWindow().showNotification("Transport Plugin Configuration Failed to be removed",
                    Window.Notification.TYPE_ERROR_MESSAGE)
        }

    }

    void saveTransportInfo(TransportInfo transportInfo) {
        TransportManager transportManager = meddela.transportManager
        if (transportManager.saveTransportInfo(transportInfo))
            getWindow().showNotification("Transport Plugin Configuration saved successfully")
        else
            getWindow().showNotification("Transport Plugin Configuration Failed to be saved",
                    Window.Notification.TYPE_ERROR_MESSAGE)
    }
}
