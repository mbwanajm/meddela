package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.Component
import com.niafikra.meddela.data.DataSource
import com.niafikra.meddela.data.Notification

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 5:54 PM
 */
public interface NotificationCodeArea extends Component{
    
    String getType();

    Object getCode()

    void setCode(Object code);

    def execute(Notification notification);
}