package com.niafikra.meddela.data

/**
 * Author: Boniface Chacha <boniface.chacha@niafikra.com,bonifacechacha@gmail.com>
 * Date: 8/4/12
 * Time: 12:52 PM
 */
class TransportInfo {
    String name
    HashMap configurations = new HashMap()

    boolean equals(o) {
        if (o==null) return false
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        TransportInfo that = (TransportInfo) o

        if (name != that.name) return false

        return true
    }

    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }



    String toString(){
        return name
    }
}
