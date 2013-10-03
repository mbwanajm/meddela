cd ..
mvn -Pcompile-widgetset gwt:clean
mvn -Pcompile-widgetset vaadin:update-widgetset
mvn -Pcompile-widgetset gwt:compile
