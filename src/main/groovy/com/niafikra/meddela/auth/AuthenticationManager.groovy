package com.niafikra.meddela.auth;


import org.apache.log4j.Logger
import org.jasypt.util.password.ConfigurablePasswordEncryptor

/**
 * Manage user authentication on using the system
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/17/12
 * Time: 2:28 PM
 */
class AuthenticationManager {
    private static final Logger log = Logger.getLogger(AuthenticationManager.class)

    private ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor()

    public AuthenticationManager() {
        init()
    }

    def init() {
        passwordEncryptor.setAlgorithm("SHA-1")
        passwordEncryptor.setPlainDigest(true)
    }
}
