package com.niafikra.meddela.data

/**
 * A data source specifies the details on the source of data
 * for running the scheduled queries and getting the notifications from
 *
 * @author mbwana jaffari mbura
 * Date: 18/07/12
 * Time: 17:28
 */
class DataSource {
    String name = ''
    String description = ''                             // a short description about the datasource
    String username = ''
    String password = ''
    String url = 'jdbc:mysql://127.0.0.1:3306/myDB'     // the url to use for the jdbc connection
    String driver = 'com.mysql.jdbc.Driver'             // the name of the driver to use for the connection

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof DataSource)) return false

        DataSource that = (DataSource) o

        if (name != that.name) return false

        return true
    }

    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }

    @Override
    public String toString() {
        return "$name: $url"
    }
}
