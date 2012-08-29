package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.TabSheet
import com.vaadin.ui.Button
import org.vaadin.peter.buttongroup.ButtonGroup
import com.vaadin.ui.TextArea
import com.vaadin.ui.Alignment
import com.vaadin.ui.Panel
import com.vaadin.ui.Label
import com.niafikra.meddela.data.Notification

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com,bonifacechacha@niafikra.com>
 * Date: 8/25/12
 * Time: 11:31 AM
 *
 * UI for meddela code input for its notifications setups
 * contains place holder for groovy scripts and SQL code alongside the execute and ouput area
 *
 */
class CodingTabSheet extends VerticalLayout implements Button.ClickListener{

    TabSheet codeSheet
    Button testButton;
    Label outputArea
    Panel outputPanel
    Notification notification

    NotificationCodeArea sqlArea,groovyArea

    CodingTabSheet(Notification notification){
        this.notification = notification
        codeSheet = new TabSheet()
        testButton = new Button("Execute")
        outputArea = new Label("",Label.CONTENT_XHTML)
        outputPanel=new Panel()
    }

    def build(){
       // codeSheet.setHeight("320px")
        codeSheet.setSizeFull()
        testButton.setWidth("200px")
        outputPanel.setWidth("100%")
        outputPanel.setHeight("100px")

        addComponent(codeSheet)
        /*ButtonGroup footer = new ButtonGroup()
        footer.addButton(testButton)
        footer.addButton(popButton) */
        addComponent(testButton)
        setComponentAlignment(testButton,Alignment.MIDDLE_CENTER)
        outputPanel.addComponent(outputArea)
        addComponent(outputPanel)

   //     sqlArea.setSizeFull()
     //   groovyArea.setSizeFull()
//        println(sqlArea.toString() +" \n"+groovyArea.toString())
        codeSheet.addTab(sqlArea,"SQL")
        codeSheet.addTab(groovyArea,"GROOVY")

        testButton.addListener(this)
        setExpandRatio(codeSheet,1)
        setSizeFull()
    }

    @Override
    void buttonClick(Button.ClickEvent clickEvent) {
        Button pressed = clickEvent.getButton()
        if(pressed.equals(testButton))  {
         outputArea.setValue("Please wait...")
         onExecute()
        }
        else
         popUp()
    }

    void popUp() {

    }

    void onExecute() {
        def result = execute()
        /*  if (result)
            getWindow().showNotification("Code Executes Well", Window.Notification.TYPE_HUMANIZED_MESSAGE)
        else
            getWindow().showNotification("Code Fails to execute", Window.Notification.TYPE_WARNING_MESSAGE)  */
        def formattedResult = formatOutput(result)
        outputArea.setValue(formattedResult)
    }

    def formatOutput(Map result) {
        StringBuffer out = new StringBuffer("")

        if (!result) return "There is no any code to execute"
        if (result.size() == 2) out.append("<br/>Warning:YOU HAVE BOTH SQL AND GROOVY CODES ONLY SQL CODES WILL BE USED<br/>")

        if (result["sql"])
            out.append("SQL:<br/>").append(getMessageToShow(result.get("sql")))
        if (result["groovy"])
            out.append("<br/>GROOVY:<br/>").append(getMessageToShow(result.get("groovy")))
        return out

    }

    def execute() {
        Map results = [:]
        // def result;
        def sqlResult = sqlArea.execute(notification)
        // if (!result)
        def groovyResult = groovyArea.execute(notification)
        if (sqlResult != null)
            results.put("sql", getResult(sqlResult))

        if (groovyResult != null)
            results.put("groovy", getResult(groovyResult))
        return results
    }

    def getMessageToShow(Object result) {
        if (result instanceof ArrayList)
            return getTableToShow(result)
        else return result
    }

    def getTableToShow(Object results) {
        def content = "<table><tr>"
        def columnNames = results[0].keySet()
        columnNames.each {
            content += "<td> $it&nbsp&nbsp</td>"
        }
        content += "</tr>"

        results.each { result ->
            content += "<tr>"
            columnNames.each { columnName ->
                content += "<td> ${result[columnName]}&nbsp&nbsp</td>"
            }
            content += "</tr>"
        }
        content += "</table>"
    }

    def getResult(def result) {
        if(result instanceof ArrayList){
            if(result.isEmpty())
                return "No Rows returned"
            else return result
        }
        return result
    }


}