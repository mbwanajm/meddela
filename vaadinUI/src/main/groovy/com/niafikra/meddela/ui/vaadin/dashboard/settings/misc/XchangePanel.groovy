package com.niafikra.meddela.ui.vaadin.dashboard.settings.misc

import com.vaadin.ui.Panel
import com.vaadin.ui.Button
import com.vaadin.ui.Upload
import com.niafikra.meddela.ui.vaadin.dashboard.settings.transport.UploadReceiver
import com.niafikra.meddela.meddela

import com.vaadin.terminal.StreamResource
import com.vaadin.ui.Embedded
import com.vaadin.terminal.gwt.client.ui.dd.HorizontalDropLocation
import com.vaadin.ui.HorizontalLayout

/**
 * This panel allows the user to export and import notifications
 *
 * @author mbwana jaffari mbura
 * Date: 05/08/12
 * Time: 08:30
 */
class XchangePanel extends Panel implements Button.ClickListener, Upload.SucceededListener {
    private Button exportButton;
    private Upload uploadNotifications;
    private Embedded embedded;

    XchangePanel() {
        buildUI();
    }

    def buildUI() {
        getContent().setSpacing(true)

        exportButton = new Button("Export Notifications")
        addComponent(exportButton)
        exportButton.setWidth("200px")
        exportButton.addListener(this)

        embedded = new Embedded()
        addComponent(embedded)

        uploadNotifications = new Upload();
        uploadNotifications.setCaption("Upload notifications")
        uploadNotifications.addListener(this)
        uploadNotifications.setReceiver(new UploadReceiver())
        addComponent(uploadNotifications)
    }

    @Override
    void buttonClick(Button.ClickEvent clickEvent) {
        def json = meddela.xChangeService.exportAllNotifications()
        def streamSource = { new ByteArrayInputStream(json.bytes) } as StreamResource.StreamSource
        getWindow().open(new StreamResource(streamSource, "notifications.json", getApplication()))
    }

    @Override
    void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
        def tmpDir = System.getProperty("java.io.tmpdir")
        def path = "$tmpDir${File.separator}${succeededEvent.filename}"

        File file = new File(path)
        try {
            if(meddela.xChangeService.importNotifications(file.text)){
                getWindow().showNotification("import successful")
            }else {
                getWindow().showNotification("import failed")
            }
        } catch (Exception e){
            getWindow().showNotification(e.toString())
        }
    }
}
