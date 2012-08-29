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
sender: ${sentNotification.notification.getTransportConfigurationValue('sender')}
receiver: $sentNotification.receiver
content :
$sentNotification.content
+++++ ${sentNotification.notification.transport.configurations['footer message']}
        """

        log.info(msg)
        return true

    }

    @Override
    Map getGlobalConfigurations() {
        ['footer message': 'powered by meddela']
    }

    @Override
    Map getNotificationConfigurations() {
        ['sender': 'meddela']
    }

    @Override
    String getMessage() {
        return "<b>Thanks For Using Console Tranport ....</b><br/><i>niafikra engineering<i>"
    }
}
