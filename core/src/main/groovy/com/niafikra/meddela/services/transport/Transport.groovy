package com.niafikra.meddela.services.transport

import com.niafikra.meddela.data.SentNotification

/**
 * A transport can be used does the actual delivering of the notification
 *
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 15:37
 */
interface Transport {

    /**
     * deliver the notification to where its supposed to go
     *
     * @param sentNotification
     * @return true if succesful false otherwise
     */
    boolean sendNotification(SentNotification sentNotification)
}
