package com.niafikra.meddela.services

import org.neodatis.odb.core.query.criteria.And
import org.neodatis.odb.core.query.criteria.Where
import com.niafikra.meddela.meddela
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery
import com.niafikra.meddela.data.SentNotification

/**
 * This class allows you to get reports on the sent notifications
 *
 * @author mbwana jaffari mbura
 * Date: 27/08/12
 * Time: 13:30
 */
class ReportService {
    static final String NOTIFICATION_ALL = 'all'
    static final String RECEIVER_ALL = 'all'
    static final String STATUS_DELIVERED = 'delivered'
    static final String STATUS_FAILED = 'failed'
    static final String STATUS_ALL = 'all'
    static final String RECEIVER_NOT_SET = 'not set'

    /**
     * Get sent notifications that match the  specified parameters
     *
     * @param notificationName
     * @param startDate
     * @param endDate
     * @param receiver
     * @param status
     * @return
     */
    def getSentNotifications(String notificationName, Date startDate, Date endDate, String receiver, String status) {
        // Query db using the given filters
        And andCriteria = Where.and()
        endDate = endDate + 1
        andCriteria
                .add(Where.ge('time', startDate.clearTime()))
                .add(Where.lt('time', endDate.clearTime()))

        if (notificationName != NOTIFICATION_ALL)
            andCriteria.add(Where.equal('notification.name', notificationName))

        if (status != STATUS_ALL)
            andCriteria.add(Where.equal('delivered', status == STATUS_DELIVERED ? true : false))

        switch (receiver) {
            case RECEIVER_ALL:
                break

            case RECEIVER_NOT_SET:
                andCriteria.add(Where.isNull('receiver'))
                break

            default:
                andCriteria.add(Where.equal('receiver', receiver))
        }

        Collection results
        meddela.database.runDbAction {odb ->
            results = odb.getObjects(new CriteriaQuery(SentNotification, andCriteria))
        }

        return results
    }
}
