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
    DataSource dataSource = new DataSource()  // the data source this notification belongs too
    Trigger trigger = new Trigger()        // the trigger to check on the datasource, if it is satisfied then
    Template template =new Template()       // compose a message using this template
    String schedulerId      // the Task id used for this notification trigger by the scheduler
    boolean enabled         // the status of the notification if its enabled or disabled

    boolean equals(Notification notification) {
        if (this.is(notification)) return true
        if (getClass() != notification.class) return false

        Notification that = (Notification) notification

        if (name != that.name) return false

        return true
    }

    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }


}
