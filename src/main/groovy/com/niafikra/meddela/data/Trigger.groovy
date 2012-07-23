package com.niafikra.meddela.data;

/**
 * A trigger specifies the information for time and database
 * condition that if it occurs then as certain notification should
 * be sent out
 *
 * @author mbwana jaffari mbura
 *         Date: 18/07/12
 *         Time: 17:32
 */
public class Trigger {
    String schedule     // a CRON string describing when to run this trigger
    String sql = ''         // sql to run at the scheduled time
    String groovyCode = ''   // groovyCode to run at the scheduled time, only sql or groovyCode can be specified for a trigger not both

}
