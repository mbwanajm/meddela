package com.niafikra.meddela.services.transport

import org.apache.log4j.Logger
import com.niafikra.meddela.data.SentNotification

/**
 * The console transport simply prints the notification to send out
 * on the console or log file
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 15:40
 */
class ConsoleTransport implements Transport {
    private static final Logger log = Logger.getLogger(ConsoleTransport)

    @Override
    boolean sendNotification(SentNotification sentNotification) {
        def msg = """
        #### notification sent #############################
            receiver: $sentNotification.receiver
            content :
            $sentNotification.content
        """

        log.info(msg)
        return true

    }
}
