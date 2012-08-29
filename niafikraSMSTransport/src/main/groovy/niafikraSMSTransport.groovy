/**
 * copyright niafikra engineering
 */

import com.niafikra.meddela.services.transport.Transport
import com.niafikra.meddela.data.SentNotification
import org.apache.log4j.Logger

/**
 * An SMS Transport for meddela that uses niafikra sms facilities
 *
 *
 * @author mbwana jaffari mbura
 * Date: 05/08/12
 * Time: 23:06
 */
class niafikraSMSTransport implements Transport {
    Logger log = Logger.getLogger(niafikraSMSTransport.class)

    /**
     * Send out notifications as sms
     * @param sentNotification
     * @return
     */
    @Override
    boolean sendNotification(SentNotification sentNotification) {
        if (sentNotification.receiver == null) {
            log.warn("Can't send SMS \n$sentNotification.content\n receiver phone is not specified")
            return false
        }

        def sender = sentNotification.notification.getTransportConfigurationValue('sender name')
        def smsURL = getSendSMSURL(sender , sentNotification.receiver, sentNotification.content)
        def connection = new URL(smsURL).openConnection();

        if (connection.responseCode == 200) {
            def responseTerms = connection.content.text.tokenize('|')
            if (responseTerms[0] == '1701') {
                return true

            } else {
                log.warn("failed to send \n$sentNotification.content\n"
                        + "to $sentNotification.receiver\ngateway response:${responseTerms[0]}")
                return false

            }
        }

        log.warn("failed to send \n$content to $phoneNumber because\n${connection.responseMessage}")
        return false
    }

    @Override
    Map getGlobalConfigurations() {
        return new HashMap()
    }

    @Override
    Map getNotificationConfigurations() {
        return ['sender name': 'niafikra']
    }

    @Override
    String getMessage() {
        return "<b>niafikra SMS Tranport ....</b><br/><i>niafikra engineering<i>"
    }

    def getSendSMSURL(String source, String phoneNumber, String content) {
        content = URLEncoder.encode(content)
        source = URLEncoder.encode(source)
        phoneNumber = cleanPhoneNumber(phoneNumber)

        def smsUrl = 'http://121.241.242.114:8080/bulksms/bulksms?username=nct-mbwanai&password=m1b2cx&type=5&dlr=0'
        smsUrl += "&destination=$phoneNumber&source=$source&message=$content"

        return smsUrl
    }

    /**
     * Cleans a phoneNumber i.e it makes sure a phone number is in the format 255399304
     * @param phoneNumber
     * @return a properly formatted phone number
     */
    String cleanPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.trim()
        if (phoneNumber.startsWith('+')) return phoneNumber.replace('+', '')
        if (phoneNumber.startsWith('0')) return phoneNumber.replaceFirst('0', '255')

        phoneNumber
    }
}
