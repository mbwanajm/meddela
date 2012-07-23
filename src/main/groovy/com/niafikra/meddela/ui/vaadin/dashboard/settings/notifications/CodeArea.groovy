package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.Component

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/21/12
 * Time: 5:54 PM
 */
public interface CodeArea extends Component{
    
    String getType();

    Object getCode()

    void setCode(Object code);

}