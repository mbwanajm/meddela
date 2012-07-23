package com.niafikra.meddela.services

import org.neodatis.odb.ODBServer
import org.neodatis.odb.ODBFactory
import org.apache.log4j.Logger
import org.neodatis.odb.ODB
import org.neodatis.odb.OdbConfiguration
import org.neodatis.odb.ODBRuntimeException
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery
import org.neodatis.odb.core.query.IQuery
import org.neodatis.odb.core.query.criteria.Or
import org.neodatis.odb.core.query.criteria.And
import org.neodatis.odb.core.query.criteria.Where

/**
 * ObjectDatabase provides a data store for storing and retrieving objects from
 * the permanent storage
 *
 * @author mbwana jaffari mbura
 * Date: 18/07/12
 * Time: 18:29
 */
class ObjectDatabase {
    ODBServer odbServer;
    Logger log = Logger.getLogger(ObjectDatabase)
    ThreadLocal odbStore;
    ThreadLocal dbActionCounts; // can be used to check if its ok to close/commit odb

    /**
     * The constructor when this creates the object database server
     *
     */
    ObjectDatabase() {
        init()
    }

    /**
     * Initialize the object database
     * @return
     */
    void init() {

        // create and open server on port 2012
        odbServer = ODBFactory.openServer(2012)
        odbServer.addBase("meddela", "meddela")

        // Automatically reconnect objects in a session
        OdbConfiguration.autoReconnectObjectsToSession = true;

        odbServer.startServer(true)
        log.info("Succesfully started object database")

        odbStore = new ThreadLocal()
    }

    /**
     * Close the object database
     * @return
     */
    void close() {
        try {
            odbServer.close()
            log.info("succesfully closed object database")

        } catch (Exception ex) {
            log.error("failed to close server", ex)

        }
    }

    /**
     * get an odb that you can use to store and query items
     * from the database
     * @return
     */
    ODB getODB() {
        // when using the Same VM client server mode, you can not open 2 connections in the
        // same thread, each connection must be opened in its own thread. this helps to ensure that
        // for each thread there is only one open connection
        ODB odb = odbStore.get()

        if (!odb || odb?.isClosed()) {
            odb = odbServer.openClient("meddela")
            odbStore.set(odb)
        }

        return odb;
    }

    /**
     * This method performs a database queries for data retrieval safely making sure
     * all the clean up is done properly
     *
     * note that the closure must accept one parameter which is the ODB it can use
     * and there is no roll back, incase you need to rollback on failure use runDbAction
     * @param the closure to execute the closure should return
     *
     */
    boolean runDbQuery(Closure dbAction) {
        ODB odb = getODB()
        def actionCount = dbActionCounts.get()
        if (!actionCount) actionCount = 0
        actionCount ++
        try {
            return dbAction(odb)

        } catch (ODBRuntimeException ex) {
            log.error("failed to execute a db action", ex)
            return null

        } finally {
            actionCount --
            if(actionCount == 0)  odb.close()

        }
    }

    /**
     * This method performs a database action safely making sure
     * all the clean up is done properly
     *
     * note that the closure must accept one parameter which is the ODB it can use
     *
     * @param the closure to execute the closure should return
     *
     */
    boolean runDbAction(Closure dbAction) {
        ODB odb = getODB()
        def actionCount = dbActionCounts.get()
        if (!actionCount) actionCount = 0
        actionCount ++
        try {
            dbAction(odb)
            if(actionCount == 1) odb.commit()
            return true
        } catch (ODBRuntimeException ex) {
            odb.rollback()
            log.error("failed to execute a db action", ex)
            return false
        } finally {
            actionCount --
            if(actionCount == 0) odb.close()
        }
    }

    /**
     * Save each of the objects in the collection
     *
     * @param objects
     * @return
     */
    def storeEach(Collection objects) {
        ODB odb = getODB();
        for (Object object : objects) {
            odb.store(object);
        }
    }

    /**
     * Delete each item in the collection, note each item
     * must have been read via neodatis once before
     *
     * @param objects
     * @return
     */
    def deleteEach(Collection objects) {
        ODB odb = getODB()
        for (Object object : objects) {
            odb.delete(object);
        }
    }

    /**
    * Returns the first object which has the given value for the selected property
    *
    * @param objectClass the class of the object
    * @param property the property to check
    * @param value the value of the property to match
    * @return
    */
    public Object getObjectByProperty(Class objectClass, String property, Object value) {
        IQuery query = new CriteriaQuery(objectClass, Where.equal(property, value));
        query.setPolymorphic(true);

        return getODB().getObjects(query).getFirst();
    }

   /**
    * Returns a collection of objects which have the given value for the selected property
    *
    * @param objectClass the class of the object
    * @param property the property to check
    * @param value the value of the property to match
    * @return
    */
    public Collection getObjectsByProperty(Class objectClass, String property, Object value) {

        return getODB().getObjects(new CriteriaQuery(objectClass,
                Where.equal(property, value)));

    }

   /**
    * Similar to SQL "in" statement, it returns all objects that have the given property
    * with the value equal to atleast one of the objects in the collection in
    *
    * @param objectsClass
    * @param property the property name
    * @param inValues the possible values for an object
    * @return
    */
    public Collection getObjectsWithPropertyIn(Class objectsClass, String property, Collection inValues) {
        Or orCriteria = Where.or();
        for (Object object : inValues) {
            orCriteria.add(Where.equal(property, object));
        }

        return getODB().getObjects(new CriteriaQuery(objectsClass, orCriteria));

    }

   /**
    * Returns a collection of objects that have all the given properties with the
    * specified values,
    * the properties and values are specified as a map where
    * key-> property
    * value -> the value the property should have
    * The query is performed using AND, thus the returned collection
    * has objects which have all the properties
    *
    * @param objectsClass
    * @param propertiesToCheck
    * @return
    */
    public Collection getObjectsByPropertiesAND(Class objectsClass, Map<String, Object> propertiesToCheck) {

        And criteria = Where.and();
        Collection<String> properties = propertiesToCheck.keySet();
        for (String property : properties) {
            criteria.add(Where.equal(property, propertiesToCheck.get(property)));
        }

        return getODB().getObjects(new CriteriaQuery(objectsClass, criteria));

    }

   /**
    * Returns a collection of objects that have on of the given properties with the
    * specified values,
    * the properties and values are specified as a map where
    * key-> property
    * value -> the value the property should have
    * The query is performed using OR, thus the returned collection
    * has objects which have at least on of the properties with the given value
    *
    * @param objectsClass
    * @param propertiesToCheck
    * @return
    */
    public Collection getObjectsByPropertiesOR(Class objectsClass, Map<String, Object> propertiesToCheck) {

        And criteria = Where.and();
        Collection<String> properties = propertiesToCheck.keySet();
        for (String property : properties) {
            criteria.add(Where.equal(property, propertiesToCheck.get(property)));
        }

        return getODB().getObjects(new CriteriaQuery(objectsClass, criteria));
    }

   /**
    * Use this method to get objects which contain other objects stored in neodatis
    * and the comparison object is another object stored in neodatis
    *
    * @param objectClass
    * @param property
    * @param value
    * @return
    */
    public Collection getObjectsByPropertyExt(Class objectClass, String property, Object value) {

        IQuery query = getODB().criteriaQuery(objectClass, Where.equal(property, value));
        return getODB().getObjects(query);

    }

   /**
    * Get a list of all object for a class strored in a DB
    * @param clazz
    * @return
    */
    public Collection getAll(Class clazz) {
        ODB odb = getODB()
        try {
            return odb.getObjects(clazz)
        }
        finally {
            odb.close()
        }
    }


}
