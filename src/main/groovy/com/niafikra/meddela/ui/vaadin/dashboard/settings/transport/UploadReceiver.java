/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.niafikra.meddela.ui.vaadin.dashboard.settings.transport;
import com.vaadin.ui.Upload.Receiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * This class receives a file and writes by default to the system temporary
 * folder. You can change this behaviour by using the constructor that specifies
 * where to upload the file to
 *
 * inpired by inaya receiver
 *
 * @author mbwana jaffari mbura
 */
class UploadReceiver implements Receiver, Serializable {

    private String fileName;
    private String mtype;
    private Logger logger = Logger.getLogger(UploadReceiver.class);
    private String destination;

    /**
     * Default constructor will upload files to temporary directory
     */
    public UploadReceiver() {
        destination = System.getProperty("java.io.tmpdir");
    }

    /**
     * @param destination the directory to load the files to
     */
    public UploadReceiver(String destination) {
        this.destination = destination;
    }

    public OutputStream receiveUpload(String filename, String mimetype) {
        fileName = filename;
        mtype = mimetype;
        File tmpStore = new File(
                new StringBuilder(destination)
                        .append(File.separator)
                        .append(filename)
                        .toString());

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tmpStore);
        } catch (FileNotFoundException ex) {
            logger.error("Couldn't write to file " + destination, ex);
        }

        return out;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mtype;
    }

}

