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

/**
 * niafikra engineering

 * Author: Boniface Chacha <boniface.chacha@niafikra.com,bonifacechacha@gmail.com>
 * Date: 8/1/12
 * Time: 6:47 AM
 */
class TransportUploadPanel extends HorizontalLayout implements Upload.FailedListener, Upload.SucceededListener {
    Upload transportUpload;
    ListSelect transportList;

    public TransportUploadPanel() {
        transportUpload = new Upload("Upload Transport jar", new UploadReceiver(meddela.transportManager.transportsPath))
        transportList = new ListSelect("Available Transport")
        build()
        load()
    }

    def build() {
        setSizeFull()
        setMargin(true)
        setSpacing(true)
        addComponent(transportList)
        transportList.setSizeFull()
        Panel uploadingPanel = new Panel("Add Transport")
        uploadingPanel.setSizeFull()
        uploadingPanel.addComponent(transportUpload)
        transportUpload.addListener((Upload.SucceededListener) this)
        transportUpload.addListener((Upload.FailedListener) this)
        addComponent(uploadingPanel)
        setExpandRatio(uploadingPanel, 8)
        setExpandRatio(transportList, 2)
    }

    def load() {
        meddela.transportManager.listAvailableTransport().each {
            transportList.addItem(it)
        }
    }

    @Override
    void uploadFailed(Upload.FailedEvent event) {
        getWindow().showNotification("Transport Upload Failed!", event.reason.toString(), Window.Notification.TYPE_ERROR_MESSAGE)
    }

    @Override
    void uploadSucceeded(Upload.SucceededEvent event) {
        TransportManager transportManager = meddela.transportManager
        if (event.MIMEType.contains("java-archive")) {
            transportManager.addTransport(new File(transportManager.transportsPath + File.separator + event.filename))
            if (transportManager.testTransport(event.filename.replace(".jar", "")))
                getWindow().showNotification("Transport Upload Succeded!", Window.Notification.TYPE_HUMANIZED_MESSAGE)
            else {
                getWindow().showNotification("Transport Upload Failed!", "Jar File does not contain a valid meddela transport", Window.Notification.TYPE_ERROR_MESSAGE)
                FileUtils.deleteQuietly(new File(transportManager.transportsPath+File.separator+event.filename))
            }
        } else {
            getWindow().showNotification("Transport Upload Failed!", "File is not a valid meddela transport jar", Window.Notification.TYPE_ERROR_MESSAGE)
            FileUtils.deleteQuietly(new File(transportManager.transportsPath+File.separator+event.filename))
        }
    }
}
