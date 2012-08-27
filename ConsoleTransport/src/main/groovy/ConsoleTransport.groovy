/**
 * copyright niafikra engineering
 */

import com.niafikra.meddela.services.transport.Transport
import com.niafikra.meddela.data.SentNotification
import org.apache.log4j.Logger

/**
 * The console transport simply prints the notification to send out
 * on the console or log file
 *
 * @author mbwana jaffari mbura
 * Date: 05/08/12
 * Time: 23:06
 */
class ConsoleTransport implements Transport {
    Logger log = Logger.getLogger(ConsoleTransport.class)

    @Override
    boolean sendNotification(SentNotification sentNotification) {
        if(sentNotification.receiver == null)   return false
        def msg = """
        #### notification sent #############################
            receiver: $sentNotification.receiver
            content :
            $sentNotification.content
        """

        log.info(msg)
        return true

    }

    @Override
    Map globalConfigurations() {
        return new HashMap()
    }

    @Override
    Map notificationConfigurations() {
        return new HashMap()
    }
}
