package com.niafikra.meddela.ui.vaadin.dashboard.settings.notifications

import com.vaadin.ui.Window
import org.vaadin.aceeditor.AceEditor
import com.vaadin.ui.Button
import com.vaadin.ui.themes.Reindeer
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import org.vaadin.aceeditor.gwt.ace.AceMode
import com.niafikra.meddela.utilities.SqlUtil

import groovy.sql.Sql
import groovy.sql.GroovyRowResult
import com.niafikra.meddela.utilities.GStringUtil
import com.vaadin.ui.Window.CloseEvent
import com.vaadin.ui.Window.CloseListener
import com.niafikra.meddela.data.Notification
import com.vaadin.ui.VerticalLayout

/**
 * Pops out a window that allows the user to edit and test his code
 *
 * @author mbwana jaffari mbura
 * Date: 14/08/12
 * Time: 14:44
 */
class CodeAreaPopupWindow extends Window implements Button.ClickListener, CloseListener{
    private AceEditor aceEditor;
    private Button testButton
    private Label resultsLabel;
    private AceEditor originalEditor
    Notification notification

    CodeAreaPopupWindow(AceEditor originalEditor, Notification notification) {
        this.notification = notification

        setCaption("edit code")
        setWidth("705px")
        setHeight("600px")
        setModal(true)
        setContent(new VerticalLayout())
        getContent().setSizeFull()

        addListener((CloseListener) this)

        this.originalEditor = originalEditor
        aceEditor = new AceEditor()
        aceEditor.setWidth("700px")
        aceEditor.setHeight("350px")
        aceEditor.setMode(originalEditor.mode)
        aceEditor.setValue(originalEditor.value)
        addComponent(aceEditor)

        testButton = new Button("test")
        testButton.setStyleName(Reindeer.BUTTON_SMALL)
        testButton.setWidth("100%")
        testButton.addListener(this)
        addComponent(testButton)

        Panel resultsPanel = new Panel()
        resultsPanel.setSizeFull()
        resultsLabel = new Label()
        resultsLabel.setSizeFull()
        resultsPanel.addComponent(resultsLabel)
        addComponent(resultsPanel)
        getContent().setExpandRatio(resultsPanel, 1)

    }

    @Override
    void buttonClick(Button.ClickEvent clickEvent) {

        SqlUtil.runWithSqlConnection(notification) { Sql sql, notification ->
            try {
                def results
                if (originalEditor.mode == AceMode.sql) {
                    results = sql.rows(aceEditor.value)

                } else {
                    def bindings =  GStringUtil.setUpDates()
                    bindings[sql] = sql
                    results = GStringUtil.evaluateAsGString(aceEditor.value, bindings)

                }
                renderResults(results)

            } catch (Exception e) {
                resultsLabel.setContentMode(Label.CONTENT_PREFORMATTED)
                resultsLabel.setValue(e)
            }
        }

    }

    def renderResults(results) {
        if (results instanceof Collection && results) {
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

            resultsLabel.setContentMode(Label.CONTENT_XHTML)
            resultsLabel.setValue(content)
        } else {
            resultsLabel.setValue("Executed well!\nempty result set returned")
        }
    }

    @Override
    void windowClose(CloseEvent closeEvent) {
        originalEditor.setValue(aceEditor.value)
    }
}
