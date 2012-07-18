package com.niafikra.meddela.data

/**
 * If a notification is sent out then it is recorded as a sent notification
 * not that sent here simply means it a trigger had occured and an Notification
 * was composed and an order was given to the transport to send it out
 *
 * transport may fail to send out the notification and this can be found out
 * by looking at the sentSuccesfully field
 *
 * @author mbwana jaffari mbura
 * Date: 18/07/12
 * Time: 17:43
 */
class SentNotification {
    Notification notification
    String receiver             // the number or id of the person who is supposed to receive the notification
    String content              // the contents of the notification
    Date timeSent               // the time the transport was ordered to send it out
    boolean sentSuccesfully     //  the status, true if succesfully sent false otherwise

}
