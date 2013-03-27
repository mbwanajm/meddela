package com.niafikra.meddela.utilities

import spock.lang.Specification

/**
 * Specification for the GStringUtilClass
 *
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 10:15
 */
class GStringUtilTest extends Specification{

    def "evaluateSqlAsGString() specification"(){

        def dates = setUpDates()

        when: 'called with text containing todaysDate, firstDayOfMonth and lastDayOfMonth with a $ prefix'
        def result = GStringUtil.evaluateSqlAsGString('$todaysDate, $firstDayOfMonth, $lastDayOfMonth')

        then: 'occurences of the above variables are replaced with their values'
        result == "$dates.todaysDate, $dates.firstDayOfMonth, $dates.lastDayOfMonth"

    }


    /**
     * Helper method that calculates today's date, first day of Month and last day of month
     *
     * @return a map containing the calculated dates
     */
    def setUpDates(){
        // evaluate todays date
        def todaysDate = new Date()
        todaysDate.clearTime()

        // evaluate the first date of the month
        def firstDayOfMonth = new Date()
        firstDayOfMonth.year = todaysDate.year
        firstDayOfMonth.month = todaysDate.month
        firstDayOfMonth.date = 1
        firstDayOfMonth.clearTime()
        firstDayOfMonth = firstDayOfMonth.format('yyyy-MM-dd')

        // evaluate the last date of the month by taking the day before the first day of next month
        def lastDayOfMonth = new Date()
        lastDayOfMonth.year = todaysDate.year
        lastDayOfMonth.month = todaysDate.month + 1
        lastDayOfMonth.date = 1
        lastDayOfMonth -= 1
        lastDayOfMonth.clearTime()
        lastDayOfMonth = lastDayOfMonth.format('yyyy-MM-dd')

        todaysDate = todaysDate.format('yyyy-MM-dd')

        return [todaysDate:todaysDate, firstDayOfMonth: firstDayOfMonth, lastDayOfMonth: lastDayOfMonth]
    }
}
