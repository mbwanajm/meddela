package com.niafikra.meddela.utilities

import com.niafikra.meddela.data.Notification
import groovy.sql.Sql
import org.apache.log4j.Logger

/**
 * This class contains utilities allow automatic injection of sql
 *
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 13:20
 */
class SqlUtil {
    private static final Logger log = Logger.getLogger(SqlUtil)

    /**
     * Opens an SQL connection and executes the given closure
     * passing it the sql connection and the notification object
     *
     * @param notification
     * @param closure
     * @return
     */
    static def runWithSqlConnection(Notification notification, Closure closure) {
        Sql sql
        try {
            sql = Sql.newInstance(
                    notification.dataSource.url,
                    notification.dataSource.username,
                    notification.dataSource.password,
                    notification.dataSource.driver,
            )
            return closure(sql, notification )


        } catch (Exception ex) {
            log.error("failed to connect to ${notification.dataSource.name} datasource", ex)
            return ex.getMessage()

        } finally {
            sql?.close()
        }
    }
}
