package com.niafikra.meddela.data;

/**
 * An sms template describes how the SMS should be composed
 *
 * @author mbwana jaffari mbura
 *         Date: 18/07/12
 *         Time: 17:34
 */
public class Template {
    Set<SQL> sqls               // the set of SQL to use to get the data
    String groovyCode = ''      // groovy code that returns a collection of the required data
    String joiningProperty      // the common property used for joining results returned by different groovy or sql
    String receiverProperty     // the property to use as a phone number
    String template =''         // the template to use to compose the sms

}
