package com.niafikra.meddela.utilities

/**
 * This map returns a default value whenever the key doesn't exist in the map
 * the idea for this map is to change the behaviour of the hashmap to return
 * a configurable value instead of null when the key doesn't exist in the map
 *
 * @author mbwana jaffari mbura
 * Date: 24/01/13
 * Time: 14:17
 */
class DefaultValueReturningMap extends HashMap {
    def defaultValue

    DefaultValueReturningMap(defaultValue) {
        this.defaultValue = defaultValue
    }

    public Object get(Object key){
        def value = super.get(key)
        value ? value : defaultValue
    }
}
