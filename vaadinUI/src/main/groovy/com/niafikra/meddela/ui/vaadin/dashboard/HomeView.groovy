package com.niafikra.meddela.ui.vaadin.dashboard

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Alignment
import com.niafikra.meddela.meddela
import org.neodatis.odb.ODB
import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.services.ReportService
import groovy.xml.MarkupBuilder
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.GridLayout
import com.vaadin.ui.Embedded
import com.vaadin.terminal.ThemeResource
import com.vaadin.ui.Button
import com.vaadin.ui.themes.Reindeer
import com.vaadin.ui.Panel

/**
 * The home view
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/19/12
 * Time: 5:31 PM
 */
class HomeView extends VerticalLayout {
    Label notificationsLabel = new Label()
    Button reloadButton;

    HomeView() {
        setSpacing(true)
        setMargin(true)

        buildUI()
    }

    def buildUI() {
        HorizontalLayout topLayout = new HorizontalLayout()
        topLayout.setWidth('100%')
        addComponent(topLayout)
        setComponentAlignment(topLayout, Alignment.TOP_CENTER)

        Panel statsLayout = new Panel()
        statsLayout.setStyleName(Reindeer.PANEL_LIGHT)
        statsLayout.content.setSpacing(true)
        statsLayout.setWidth('645px')
        topLayout.addComponent(statsLayout)
        topLayout.setComponentAlignment(statsLayout, Alignment.TOP_LEFT )

        notificationsLabel.setContentMode(Label.CONTENT_XHTML)
        statsLayout.addComponent(notificationsLabel)

        reloadButton = new Button('get latest stats')
        reloadButton.setStyleName(Reindeer.BUTTON_SMALL)
        reloadButton.addListener({ renderStats(loadNotificationStats())} as Button.ClickListener)
        statsLayout.addComponent(reloadButton)
        statsLayout.content.setComponentAlignment(reloadButton, Alignment.BOTTOM_RIGHT)

        Embedded logo = new Embedded()
        logo.setSource(new ThemeResource('../meddela/images/logo.png'))
        topLayout.addComponent(logo)
        topLayout.setComponentAlignment(logo, Alignment.TOP_RIGHT)


        def notificationStats = loadNotificationStats()
        renderStats(notificationStats)
    }

    def renderStats(Collection stats) {
        if (stats) {
            StringWriter writer = new StringWriter()
            def build = new MarkupBuilder(writer)

            build.table('class': 'stats_table') {
                tr('class': 'stats_header') {
                    td()
                    td('this week')
                    td('this month')
                    td('all')
                }

                for (stat in stats) {
                    tr {
                        td('class': 'stats_name', stat.name)
                        td('class': 'stats_detail', stat.weekCount)
                        td('class': 'stats_detail', stat.monthCount)
                        td('class': 'stats_detail', stat.allCount)
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

                details['monthCount'] = meddela.reportService.getSentNotificationCount(
                        notification.name,
                        new Date(today.year, today.month, 1),
                        today,
                        ReportService.RECEIVER_ALL,
                        ReportService.STATUS_DELIVERED
                )

                details['allCount'] = meddela.reportService.getSentNotificationCount(
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
