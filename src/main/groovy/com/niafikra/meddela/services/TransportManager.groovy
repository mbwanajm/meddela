package com.niafikra.meddela.services

import com.niafikra.meddela.data.SentNotification
import com.niafikra.meddela.services.transport.Transport
import com.niafikra.meddela.services.transport.ConsoleTransport
import com.niafikra.meddela.meddela
import org.neodatis.odb.ODB
import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.data.UniqueRecepient

/**
 * This class coordinates the delivering of notifications and saves
 * the results of the action
 *
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 15:52
 */
class TransportManager {
    Transport transport;

    TransportManager() {
        transport = new ConsoleTransport()
    }

    /**
     * Send out the specified notification
     *
     * Note that this will use the composer to get all the notifications that are to be sent out
     * then it will send each one of them saving the status to the db
     *
     * @param notification
     */
    def sendNotification(Notification notification) {
        def notificationsToSend = meddela.composer.compose(notification)

        for (SentNotification sentNotification in notificationsToSend) {
            sentNotification.time= new Date()
            sentNotification.delivered = transport.sendNotification(sentNotification)

            meddela.database.runDbAction { ODB odb ->
                // store the sent notification
                odb.store(sentNotification)

                // if the recepient is not in the unique recepients list add him
                def recepients = meddela.database
                        .getObjectsByProperty(UniqueRecepient, 'recepient', sentNotification.receiver)

                if(recepients?.isEmpty()){
                    odb.store(new UniqueRecepient(sentNotification.receiver))
                }
            }

        }
    }
}
