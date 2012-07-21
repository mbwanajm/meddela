package com.niafikra.meddela.ui.vaadin.dashboard.reports

import com.vaadin.ui.TabSheet

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/19/12
 * Time: 5:30 PM
 */
class ReportView extends TabSheet{
    SentNotificationReportPanel sentNotificationReportPanel;

    ReportView() {
        setSizeFull();

        sentNotificationReportPanel = new SentNotificationReportPanel()
        addTab(sentNotificationReportPanel, "Sent Notifications")
    }
}
