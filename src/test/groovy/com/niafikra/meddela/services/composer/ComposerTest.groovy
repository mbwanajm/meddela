package com.niafikra.meddela.services.composer

import spock.lang.Specification
import com.niafikra.meddela.data.Template

import com.niafikra.meddela.data.Notification
import com.niafikra.meddela.services.transport.Transport
import com.niafikra.meddela.services.transport.ConsoleTransport
import com.niafikra.meddela.data.SentNotification

/**
 * The composer specification
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 14:39
 */
class ComposerTest extends Specification {

    def "mergeResults feature"() {

        given:
        def composer = new Composer()
        def results = [
                [name: 'mbwana', id: 1],
                [hobby: 'programming', id: 1],
                [name: 'boniface', id: 2],
                [hobby: 'coding', id: 2]
        ]

        when: 'composer is called with an array of maps and given the key to use as the merging value'
        def mergedResult = composer.mergeResults('id', results)

        then: 'the size of the returned map is the same as the number of unique ids'
        mergedResult.size() == 2

        and: 'the map contains a map of the merged properties with the mergingValue as the key'
        mergedResult[1] == [name: 'mbwana', id: 1, hobby: 'programming']
        mergedResult[2] == [name: 'boniface', id: 2, hobby: 'coding']

    }


}
