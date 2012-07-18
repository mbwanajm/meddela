package com.niafikra.meddela.data

/**
 * A data source specifies the details on the source of data
 * for running the scheduled queries
 *
 * @author mbwana jaffari mbura
 * Date: 18/07/12
 * Time: 17:28
 */
class DataSource {
    String name
    String description      // a short description about the datasource
    String username
    String password
    String url              // the url to use for the jdbc connection
    String driver           // the name of the driver to use for the connection

}
