package com.niafikra.meddela.services.xchange

import com.google.gson.Gson
import com.niafikra.meddela.meddela
import org.neodatis.odb.ODB
import com.niafikra.meddela.data.Notification
import org.apache.log4j.Logger
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import com.niafikra.meddela.data.DataSource
import com.niafikra.meddela.services.ObjectDatabase

/**
 * @author mbwana jaffari mbura
 * Date: 01/08/12
 * Time: 15:11
 */
class XChangeService {
    private Gson gson;
    private static final Logger log = Logger.getLogger(XChangeService)

    XChangeService() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Exports all notifications to their JSON representations
     *
     * @return json representation of the notifications
     */
    def exportAllNotifications() {
        Collection notifications
        meddela.database.runDbQuery {ODB odb ->
            notifications = odb.getObjects(Notification)
        }

        if (!notifications) return null

        log.info('exporting ${notifications.size()} notifications..')
        String jsonRepresentation = gson.toJson(notifications)
        log.info(jsonRepresentation)

        return jsonRepresentation;
    }

    /**
     * Given JSON containing an array of notifications it will
     * import those notifications to the database provided they
     * don't exist yet. also it automatically imports the datasources
     *
     * @param notifications
     */
    def importNotifications(String notificationsJSON) {
        Type collectionType = new TypeToken<Collection<Notification>>() {}.getType();
        def notifications = gson.fromJson(notificationsJSON, collectionType)
        if (notifications) {
            def dataSources = [:]

            // group notifications according to their datasources
            for (notification in notifications) {
                if (!dataSources[notification.dataSource]) dataSources[notification.dataSource] = []
                dataSources[notification.dataSource] << notification
            }

            return meddela.database.runDbAction {ODB odb ->
                dataSources.each {dataSource, nots ->
                    // if datasource exists in the database then update and use it instead
                    def results = meddela.database.getObjectsByProperty(DataSource, 'name', dataSource.name)
                    if (!results?.isEmpty()) {
                        DataSource dbDataSource = results.iterator().next()
                        ObjectDatabase.deepCopy(dataSource, dbDataSource)
                        dataSource = dbDataSource
                    }

                    // save the notifications, updating them if they exist in db
                    notifications.each {notification ->
                        notification.dataSource = dataSource
                        results = meddela.database.getObjectsByProperty(Notification, 'name', notification.name)
                        if (results?.isEmpty()) {
                            meddela.notificationManager.addNotification(notification)
                        } else {
                            meddela.notificationManager.updateNotification(notification)
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }


}
