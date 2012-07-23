package com.niafikra.meddela.auth;


import org.apache.log4j.Logger
import org.jasypt.util.password.ConfigurablePasswordEncryptor
import com.niafikra.meddela.data.security.Authentication
import com.niafikra.meddela.meddela
import org.neodatis.odb.ODB
import com.niafikra.meddela.data.Notification
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

    private def init() {
        passwordEncryptor.setAlgorithm("SHA-1")
        passwordEncryptor.setPlainDigest(true)
        log.info("Authentication manager started successfully")
    }
    
    def addAuthentication(Authentication auth){
        auth.setPassword(passwordEncryptor.encryptPassword(auth.password))
        if(!authExist(auth.username)) {
            meddela.database.runDbAction {ODB odb-> odb.store(auth)}   
            return true
        }else return false
    }

    def authExist(String username) {
            Collection auths= meddela.database.getObjectsByProperty(Authentication.class,"username",username)
            if(auths && !auths?.isEmpty())
                return auths.getFirst()
            else return null
    }

    def authenticate(String username,String password){
        def auth = authExist(username)
        if(auth)
            return passwordEncryptor.checkPassword(password,auth.password)
        return false
    }

    def updateAuthentication(Authentication authentication,boolean passwordModified){
        if(passwordModified)
            authentication.setPassword(passwordEncryptor.encryptPassword(authentication.password))
            meddela.database.runDbAction {ODB odb-> odb.store(authentication)}


    }
}
