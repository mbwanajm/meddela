package com.niafikra.meddela.data;

import java.util.Set;

/**
 * An sms template describes how the SMS should be composed
 *
 * @author mbwana jaffari mbura
 *         Date: 18/07/12
 *         Time: 17:34
 */
public class SmsTemplate {
    Set<String> sqls                // the set of SQL to use to get the data
    Set<String> groovyCodes         // groovy code that returns a collection of the required data
    String joiningProperty          // the common property used for joining results returned by different groovy or sql
    String phoneNumberProperty      // the property to use as a phone number
    String template                 // the template to use to compose the sms

}
