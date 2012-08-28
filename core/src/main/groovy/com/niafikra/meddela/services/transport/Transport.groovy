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

    /**
     * A set a configurations of which are to be shared on sending
     * different notifications
     * @return  a map of config name againsts its default value which
     * can be changed by user
     */
    Map globalConfigurations()

    /**
     * A set a configurations of which are to be used on sending
     * a specific notifications
     * @return  a map of config name againsts its default value which
     * can be changed by user
     */
    Map notificationConfigurations()
}
