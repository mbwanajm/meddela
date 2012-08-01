package com.niafikra.meddela.services.transport

/**
 * niafikra engineering
 
 * Author: Boniface Chacha <boniface.chacha@niafikra.com,bonifacechacha@gmail.com>
 * Date: 8/1/12
 * Time: 6:34 AM
 */
class TransportLoader extends URLClassLoader{

    TransportLoader(URL[] urls,ClassLoader classLoader) {
        super(urls,classLoader)

    }

    def addTransportURL(URL url){
        addURL(url)
    }

}
