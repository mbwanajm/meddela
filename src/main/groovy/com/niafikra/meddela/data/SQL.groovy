package com.niafikra.meddela.data

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/22/12
 * Time: 4:22 PM
 */
class SQL {
    String SQL
    String name

    SQL(String SQL, String name) {
        this.SQL = SQL
        this.name = name
    }

    @Override
    public String toString() {
        return SQL
    }

}
