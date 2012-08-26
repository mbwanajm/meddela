package com.niafikra.meddela.services

import spock.lang.Specification

/**
 * Test the object database
 *
 * @author mbwana jaffari mbura
 * Date: 26/08/12
 * Time: 12:47
 */
class ObjectDatabaseTest extends Specification {

    /**
     * This method performs a deepCopy
     * @return
     */
    def 'deep copy an obect'(){
        def address = new Address(physical:true, city:'dar es salaam')
        def contact = new Contact(phone:'0713155270', address:address)
        def person = new Person(name:'Kurenje Mbura', age:12, contact:contact)
        person.relatives = [one: 'one']

        def anotherPerson = new Person()
        def anotherContact = new Contact()
        anotherPerson.contact = anotherContact
       // anotherPerson.relatives = ['rd']

        when: 'we do a deep copy of an object'
        ObjectDatabase.deepCopy(person, anotherPerson)

        then: 'all primitive types are copied(even inner ones)'
        anotherPerson.name == person.name
        anotherPerson.age == person.age
        anotherPerson.contact.phone == person.contact.phone
        anotherPerson.contact.address.city == person.contact.address.city
        anotherPerson.contact.address.physical == anotherPerson.contact.address.physical
        anotherPerson.relatives.is(person.relatives)

        and:'non primitive object that exist in destination are not overwritten'
        anotherPerson.contact.is(anotherContact)

        and:'non primitive objects that didnt exist are copied from source object'
        anotherPerson.contact.address.is(person.contact.address)
    }

}

class Person{
    String name
    int age
    Contact contact
    def relatives

}

class Contact{
    String phone
    Address address
}

class Address{
    boolean physical
    String city
}


