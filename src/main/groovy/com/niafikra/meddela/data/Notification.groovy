package com.niafikra.meddela.data

/**
 * A comprises of all the information that can lead up to the
 * sending out of a notification
 *
 * @author mbwana jaffari mbura
 * Date: 18/07/12
 * Time: 17:41
 */
class Notification {
    String name
    String description
    DataSource dataSource   // the data source this notification belongs too
    Trigger trigger         // the trigger to check on the datasource, if it is satisfied then
    SmsTemplate template    // compose a message using this template
}
