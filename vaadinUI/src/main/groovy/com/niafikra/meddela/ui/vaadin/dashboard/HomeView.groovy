package com.niafikra.meddela.ui.vaadin.dashboard

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Alignment
import com.niafikra.meddela.meddela
import org.neodatis.odb.ODB
import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.services.ReportService
import groovy.xml.MarkupBuilder

/**
 * The home view
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/19/12
 * Time: 5:31 PM
 */
class HomeView extends VerticalLayout {
    Label notificationsLabel = new Label()

    HomeView() {
        setSizeFull()
        setSpacing(true)
        setMargin(true)

        buildUI()
    }

    def buildUI() {

        notificationsLabel.setContentMode(Label.CONTENT_XHTML)
        addComponent(notificationsLabel)
        setComponentAlignment(notificationsLabel, Alignment.MIDDLE_CENTER)
        def notificationStats = loadNotificationStats()
        renderStats(notificationStats)
    }

    def renderStats(Collection stats) {
        if(stats){
            StringWriter writer = new StringWriter()
            def build = new MarkupBuilder(writer)

            build.table('class':'stats_table'){
                tr('class':'stats_header'){
                    td()
                    td('this week')
                    td('this month')
                    td('all')
                }

                for(stat in stats){
                    tr{
                       td(stat.name)
                       td(stat.weekCount)
                       td(stat.monthCount)
                       td(stat.allCount)
                    }
                }
            }

            notificationsLabel.value = writer.toString()

        } else {
            notificationsLabel.value = 'No notifications have been sent out'
        }

    }

    def loadNotificationStats() {
        def today = new Date();
        def notificationStats = []
        meddela.database.runDbQuery {ODB odb ->
            Collection notifications = odb.getObjects(Notification)
            notifications.each {Notification notification ->
                def details = [name: notification.name]
                details['weekCount'] = meddela.reportService.getSentNotificationCount(
                        notification.name,
                        today - 7,
                        today,
                        ReportService.RECEIVER_ALL,
                        ReportService.STATUS_DELIVERED
                )

                details ['monthCount'] = meddela.reportService.getSentNotificationCount(
                        notification.name,
                        new Date(today.year, today.month, 1),
                        today,
                        ReportService.RECEIVER_ALL,
                        ReportService.STATUS_DELIVERED
                )

                details ['allCount'] = meddela.reportService.getSentNotificationCount(
                        notification.name,
                        new Date(0),
                        today,
                        ReportService.RECEIVER_ALL,
                        ReportService.STATUS_DELIVERED
                )

                notificationStats << details
            }
        }

        notificationStats;
    }


}
