package com.niafikra.meddela.data

/**
 * This is a shortcut that stores only unique recepeints in
 * the database to allow for easy generation of recepients
 *
 *
 * @author mbwana jaffari mbura
 * Date: 20/07/12
 * Time: 21:35
 */
class UniqueRecepient {
    String recepient

    UniqueRecepient() {
    }

    UniqueRecepient(String recepient) {
        this.recepient = recepient
    }
}
