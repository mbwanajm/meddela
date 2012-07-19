package com.niafikra.meddela.services

/**
 * This file contains methods that make it easy to convert normal text
 * into GStrings, which can be evaluated
 *
 * @author mbwana jaffari mbura
 * Date: 19/07/12
 * Time: 10:16
 */
class GStringUtil {

    /**
     * This methods evaluates the given text as a GString passing the variables
     *
     * todaysDate,
     * firstDayOfMonth
     * lastDayOfMonth
     *
     * Whenever any of the above text is preceded with $ or contained in ${} it is replaced with
     * the actual value of the dates in the formated yyyy-MM-dd
     *
     * @param text the text to be evaluated
     * @return  the evaluated text
     */
    static String evaluateSqlAsGString(String text){
        // setup the dates
        def dates = setUpDates()

        // bind the above as variables that can be used in the sql GString template
        def binding = [
                todaysDate : dates.todaysDate.format('yyyy-MM-dd'),
                firstDayOfMonth  : dates.firstDayOfMonth.format('yyyy-MM-dd'),
                lastDayOfMonth : dates.lastDayOfMonth.format('yyyy-MM-dd'),
        ]

        // replace every occurence of the binded variables in text with the values passed
        def engine = new groovy.text.GStringTemplateEngine()
        engine.createTemplate(text)
                .make(binding)
                .toString()

    }

    static def setUpDates(){
        // evaluate todays date
        def todaysDate = new Date()
        todaysDate.clearTime()

        // evaluate the first date of the month
        def firstDayOfMonth = new Date()
        firstDayOfMonth.year = todaysDate.year
        firstDayOfMonth.month = todaysDate.month
        firstDayOfMonth.date = 1

        // evaluate the last date of the month by taking the day before the first day of next month
        def firstDayOfNextMonth = new Date()
        firstDayOfNextMonth.year = todaysDate.year
        firstDayOfNextMonth.month = todaysDate.month + 1
        firstDayOfNextMonth.date = 1
        firstDayOfNextMonth.clearTime()

        def lastDayOfMonth = firstDayOfNextMonth - 1

        return [todaysDate: todaysDate, firstDayOfMonth: firstDayOfMonth, lastDayOfMonth: lastDayOfMonth]

    }
}
