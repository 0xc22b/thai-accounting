package gwt.server.user;

import gwt.server.user.UserVerifier.Log;
import gwt.server.user.UserVerifier.LogInfoType;
import gwt.server.user.model.Session;
import gwt.server.user.model.User;
import gwt.server.user.model.UserData;
import gwt.server.user.model.UserGrp;
import gwt.shared.NotLoggedInException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class UserManager {
    
    public static final String WEB_SAFE_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    
    public static final String PARAMETER_USERNAME = "username";
    public static final String PARAMETER_EMAIL = "email";
    public static final String PARAMETER_PASSWORD = "password";
    public static final String PARAMETER_REPEAT_PASSWORD = "repeatPassword";
    public static final String PARAMETER_NEW_PASSWORD = "newPassword";
    public static final String PARAMETER_LANG = "lang";
    public static final String PARAMETER_SSID = "ssid";
    public static final String PARAMETER_SID = "sid";
    public static final String PARAMETER_FID = "fid";
    public static final String PARAMETER_VID = "vid";
    
    public static final String FROM_EMAIL = "admin@thai-accounting.appspot.com";
    public static final String FROM_NAME = "Thai-Accounting Admin";
    
    public static final String FORGOT_RESULT = "Please check your email to accept confirmation link for changing password.";
    public static final String RESET_PASSWORD_SUCCESS = "Your password has been reset. Please login.";
    public static final String RESET_PASSWORD_FAILURE = "Could not find your account."
            + " Please request to reset password again.";
    
    public static final String CONCURRENT_MODIFICATION_EXCEPTION = "The servers are really busy right now. Please try again in minutes.";
    
    public static User signUp(String username, String email,
            String password, String repeatPassword, Log log) {

        username = username.trim();
        email = email.trim();
        password = password.trim();
        repeatPassword = repeatPassword.trim();
        
        // Email always be lowercase.
        email = email.toLowerCase();
        
        // Checking for duplicate will be done in the transaction
        // in signUpAttempt.
        UserVerifier.isUsernameValid(username, false, log);
        UserVerifier.isEmailValid(email, false, log);
        UserVerifier.isPasswordValid(password, log, LogInfoType.PASSWORD);
        UserVerifier.isRepeatPasswordValid(password, repeatPassword, log);
        if (!log.isValid()) {
            return null;
        }
        
        // In order to have unique username and email,
        // 1. All users need to be in the same entity group.
        // 2. Use transaction to check if exists and if not, insert.
        // 3. Check existing with get() or ancestor query to make sure of
        //    getting the most update.
        //
        // With entity group and transaction, only one transaction can touch
        // this entity group at a time from beginning to commiting and will
        // throw java.util.ConcurrentModificationException if entity group was
        // modified by other thread (that why need to have for loop to try several time).
        // 
        // Because of ConcurrentModificationException, this table is really critical.
        //
        // http://blog.broc.seib.net/2011/06/unique-constraint-in-appengine.html
        //
        // Keep the transaction time minimum as possible.
        // Prepare everything before hand.
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        
        // Create a new user object.
        String lowerCaseUsername = username.toLowerCase();
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(getUserGrpKey(), username,
                lowerCaseUsername, email, hashPassword);
        
        Query usernameQuery = new Query(User.class.getSimpleName());
        usernameQuery.setAncestor(getUserGrpKey());
        usernameQuery.setFilter(new FilterPredicate(User.LOWER_CASE_USER_NAME,
                Query.FilterOperator.EQUAL, lowerCaseUsername));
        
        Query emailQuery = new Query(User.class.getSimpleName());
        emailQuery.setAncestor(getUserGrpKey());
        emailQuery.setFilter(new FilterPredicate(User.EMAIL,
                Query.FilterOperator.EQUAL, email));
        
        for (int i = 0; i < 10; i++) {
            try {
                Transaction txn = ds.beginTransaction();
                try {
                    // Check if username is duplicate. Compare it with lowercase.
                    Entity entity1 = ds.prepare(txn, usernameQuery).asSingleEntity();
                    if ( entity1 != null ) {
                        txn.commit();
                        log.addLogInfo(LogInfoType.USERNAME, username, false,
                                UserVerifier.ERR_NAME_TAKEN);
                        return null;
                    } else {
                        // Check if email is duplicate. Always be lowercase.
                        Entity entity2 = ds.prepare(txn, emailQuery).asSingleEntity();
                        if ( entity2 != null ) {
                            txn.commit();
                            log.addLogInfo(LogInfoType.EMAIL, email, false,
                                    UserVerifier.ERR_EMAIL_TAKEN);
                            return null;
                        } else {
                            ds.put(txn, user.getEntity());
                            // Throws java.util.ConcurrentModificationException
                            // if entity group was modified by other thread
                            txn.commit();
                            
                            UserData userData = new UserData(user.getKey());
                            ds.put(userData.getEntity());
                            
                            // Verify the email!
                            sendEmailConfirm(user, log);
                            
                            log.addLogInfo(LogInfoType.SIGN_UP, null, true, null);
                            return user;
                        }
                    }
                } finally {
                    if (txn.isActive()) {
                        txn.rollback();
                    }
                }
            } catch (ConcurrentModificationException e) {
                // stay in the loop and try again.
            } 
            // you could use another backoff algorithm here rather than 100ms each time.
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
        log.addLogInfo(LogInfoType.SIGN_UP, null, false,
                CONCURRENT_MODIFICATION_EXCEPTION);
        return null;
    }

    public static void deleteAccount(User user, String password, Log log) {
        
        password = password.trim();
        
        // validate password
        UserVerifier.isPasswordValid(password, log, LogInfoType.PASSWORD);
        if (!log.isValid()) {
            return ;
        }
        
        if (!BCrypt.checkpw(password, user.getHashPassword())) {
            log.addLogInfo(LogInfoType.PASSWORD, null, false,
                    UserVerifier.ERR_PASSWORD);
            return;
        }

        Key userDataKey = UserData.createKey(user.getKey().getId());

        // Delete user and userData
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();        
        ds.delete(user.getKey(), userDataKey);
        
        // Delete all user's login sessions
        deleteLoginSessions(user.getKey(), null);
        
        log.addLogInfo(LogInfoType.DELETE_ACCOUNT, null, true, null);
    }
    
    public static User checkUsernameOrEmailAndPassword(String usernameOrEmail,
            String password, Log log){
        
        User user;
        usernameOrEmail = usernameOrEmail.trim();
        
        if (usernameOrEmail.contains("@")) {
            
            // Email always be lowercase.
            String email = usernameOrEmail.toLowerCase();
            
            UserVerifier.isEmailValid(email, false, log);
            UserVerifier.isPasswordValid(password, log, LogInfoType.PASSWORD);
            if (!log.isValid()) {
                return null;
            }
            user = getUserFromEmail(email, log);
        } else {
            
            String username = usernameOrEmail;
            
            UserVerifier.isUsernameValid(username, false, log);
            UserVerifier.isPasswordValid(password, log, LogInfoType.PASSWORD);
            if (!log.isValid()) {
                return null;
            }
            user = getUserFromUsername(username, log);
        }
        
        if (user != null && BCrypt.checkpw(password, user.getHashPassword())) {
            log.addLogInfo(LogInfoType.LOG_IN, null, true, null);
            return user;
        }
        
        log.addLogInfo(LogInfoType.LOG_IN, null, false, UserVerifier.ERR_LOG_IN);
        return null;
    }
    
    public static User checkLoggedInAndGetUser(String sessionKeyString,
            String sessionID) throws NotLoggedInException{
        
        Session session = getSession(sessionKeyString, Session.LOG_IN, sessionID);
        if (session == null) {
            throw new NotLoggedInException();
        }
        
        User user = getUserFromKeyString(session.getUserKeyString());
        if (user == null) {
            throw new RuntimeException();
        }
        
        return user;
    }
    
    public static boolean isLoggedIn(String sessionKeyString, String sessionID){
        Session session = getSession(sessionKeyString, Session.LOG_IN, sessionID);
        return session == null ? false : true;
    }
    
    public static void changeUsername(User user, String newUsername,
            String password, Log log) {

        //TODO: Warning - can't undo and the username might be taken by others.
        newUsername = newUsername.trim();
        password = password.trim();
        
        // Checking for duplicate will be done in the transaction.
        UserVerifier.isUsernameValid(newUsername, false, log);
        UserVerifier.isPasswordValid(password, log, LogInfoType.PASSWORD);
        if (!log.isValid()) {
            return;
        }
        
        if (!BCrypt.checkpw(password, user.getHashPassword())) {
            log.addLogInfo(LogInfoType.PASSWORD, null, false,
                    UserVerifier.ERR_PASSWORD);
            return;
        }
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        
        // Check if username is duplicate. Compare it with lowercase.
        String newLowerCaseNewUsername = newUsername.toLowerCase();
        
        Query usernameQuery = new Query(User.class.getSimpleName());
        usernameQuery.setAncestor(getUserGrpKey());
        usernameQuery.setFilter(new FilterPredicate(User.LOWER_CASE_USER_NAME,
                Query.FilterOperator.EQUAL, newLowerCaseNewUsername));
        
        for (int i = 0; i < 10; i++) {
            try {
                Transaction txn = ds.beginTransaction();
                try {
                    
                    Entity entity = ds.prepare(txn, usernameQuery).asSingleEntity();
                    if ( entity != null ) {
                        txn.commit();
                        log.addLogInfo(LogInfoType.USERNAME, newUsername, false,
                                UserVerifier.ERR_NAME_TAKEN);
                        return;
                    } else {
                        // Update to DB
                        user.setUsername(newUsername, newLowerCaseNewUsername);
                        ds.put(txn, user.getEntity());
                        // Throws java.util.ConcurrentModificationException
                        // if entity group was modified by other thread
                        txn.commit();
                        
                        // Update to memcache
                        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
                        syncCache.put(user.getKeyString(), user);
                        
                        log.addLogInfo(LogInfoType.CHANGE_USERNAME, null, true, null);
                        return;
                        
                    }
                } finally {
                    if (txn.isActive()) {
                        txn.rollback();
                    }
                }
            } catch (ConcurrentModificationException e) {
                // stay in the loop and try again.
            }
            // you could use another backoff algorithm here rather than 100ms each time.
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
        log.addLogInfo(LogInfoType.CHANGE_USERNAME, null, false,
                CONCURRENT_MODIFICATION_EXCEPTION);
    }

    public static void changeEmail(User user, String newEmail,
            String password, Log log) {
        
        //TODO: Warning - can't undo.
        newEmail = newEmail.trim();
        password = password.trim();
        
        newEmail = newEmail.toLowerCase();
        
        // Checking for duplicate will be done in the transaction.
        UserVerifier.isEmailValid(newEmail, false, log);
        UserVerifier.isPasswordValid(password, log, LogInfoType.PASSWORD);
        if (!log.isValid()) {
            return;
        }
        
        if (!BCrypt.checkpw(password, user.getHashPassword())) {
            log.addLogInfo(LogInfoType.PASSWORD, null, false,
                    UserVerifier.ERR_PASSWORD);
            return;
        }
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        
        Query emailQuery = new Query(User.class.getSimpleName());
        emailQuery.setAncestor(getUserGrpKey());
        emailQuery.setFilter(new FilterPredicate(User.EMAIL,
                Query.FilterOperator.EQUAL, newEmail));
        
        for (int i = 0; i < 10; i++) {
            try {
                Transaction txn = ds.beginTransaction();
                try {
                    Entity entity = ds.prepare(txn, emailQuery).asSingleEntity();
                    if ( entity != null ) {
                        txn.commit();
                        log.addLogInfo(LogInfoType.EMAIL, newEmail, false,
                                UserVerifier.ERR_EMAIL_TAKEN);
                        return;
                    } else {
                        // Update to DB.
                        user.setEmail(newEmail);
                        ds.put(txn, user.getEntity());
                        // Throws java.util.ConcurrentModificationException
                        // if entity group was modified by other thread
                        txn.commit();
        
                        // Update to memcache
                        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
                        syncCache.put(user.getKeyString(), user);
                        
                        // Reset didEmailConfirm
                        setUserData(user.getKey(), false);
                        
                        // Verify the new email!
                        sendEmailConfirm(user, log);
                        
                        log.addLogInfo(LogInfoType.CHANGE_EMAIL, null, true, null);
                        return;
                    }
                } finally {
                    if (txn.isActive()) {
                        txn.rollback();
                    }
                }
            } catch (ConcurrentModificationException e) {
                // stay in the loop and try again.
            }
            // you could use another backoff algorithm here rather than 100ms each time.
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
        log.addLogInfo(LogInfoType.CHANGE_EMAIL, null, false,
                CONCURRENT_MODIFICATION_EXCEPTION);
    }
    
    public static void changePassword(User user, String currentSessionKeyString,
            String newPassword, String repeatPassword, String password, Log log) {

        newPassword = newPassword.trim();
        repeatPassword = repeatPassword.trim();
        password = password.trim();
        
        // validate password
        UserVerifier.isPasswordValid(newPassword, log, LogInfoType.NEW_PASSWORD);
        UserVerifier.isRepeatPasswordValid(newPassword, repeatPassword, log);
        UserVerifier.isPasswordValid(password, log, LogInfoType.PASSWORD);
        if (!log.isValid()) {
            return ;
        }
        
        if (!BCrypt.checkpw(password, user.getHashPassword())) {
            log.addLogInfo(LogInfoType.PASSWORD, null, false,
                    UserVerifier.ERR_PASSWORD);
            return;
        }
        
        setUserPassword(user, newPassword);
        
        //delete user's sessions except the current one!
        deleteLoginSessions(user.getKey(), currentSessionKeyString);

        log.addLogInfo(LogInfoType.CHANGE_PASSWORD, null, true, null);
    }
    
    public static void forgotPassword(String usernameOrEmail, Log log) {
        
        User user;
        usernameOrEmail = usernameOrEmail.trim();
        
        if (usernameOrEmail.contains("@")) {
            
            // Email always be lowercase.
            String email = usernameOrEmail.toLowerCase();
            
            UserVerifier.isEmailValid(email, false, log);
            if (!log.isValid()) {
                return;
            }
            user = getUserFromEmail(email, log);
        } else {
            
            String username = usernameOrEmail;
            
            UserVerifier.isUsernameValid(username, false, log);
            if (!log.isValid()) {
                return;
            }
            user = getUserFromUsername(username, log);
        }
        
        if (user == null) {
            log.addLogInfo(LogInfoType.FORGOT_PASSWORD, usernameOrEmail, false,
                    UserVerifier.ERR_USER_NOT_FOUND);
            return;
        }
        
        // Generate forgot password session
        Session session = addSession(Session.FORGOT_PASSWORD, user.getKey(), genSessionID());

        //Send an email with a link to reset password
        String url = genForgotUrl(session.getKeyString(), session.getSessionID());
        String subject = "Reset password at thai-accounting";
        String msgBody = "<p>Please click the link below to reset password at thai-accounting.</p>"
                + "<a href='" + url + "'>Link to reset password</a>"
                + "<p>Please ignore this email if you didn't request to reset the password.</p>"
                + "<p>If the above link is not clickable, copy and paste this link into your web browser's address bar:</p>"
                + "<p>" + url + "</p>";
        sendEmail(FROM_EMAIL, FROM_NAME, user.getEmail(), user.getUsername(),
                subject, msgBody);
        
        log.addLogInfo(LogInfoType.FORGOT_PASSWORD, usernameOrEmail, true,
                FORGOT_RESULT);
    }
    
    private static String genForgotUrl(String sessionKeyString, String sessionID) {
        return "http://thai-accounting.appspot.com/forgot?" + PARAMETER_SSID + "=" + sessionKeyString
                + "&" + PARAMETER_FID + "=" + sessionID;
    }
    
    public static void resetPassword(String sessionKeyString, String sessionID,
            String newPassword, String repeatPassword, Log log) {
        
        newPassword = newPassword.trim();
        repeatPassword = repeatPassword.trim();
        
        UserVerifier.isPasswordValid(newPassword, log, LogInfoType.NEW_PASSWORD);
        UserVerifier.isRepeatPasswordValid(newPassword, repeatPassword, log);
        if (!log.isValid()) {
            return;
        }
        
        Session session = getSession(sessionKeyString, Session.FORGOT_PASSWORD,
                sessionID);                
        if (session == null) {
            // Session not found!
            log.addLogInfo(LogInfoType.RESET_PASSWORD, null, false,
                    RESET_PASSWORD_FAILURE);
            return;
        }
        
        //TODO: The session should not be too old
        
        // Set user a new password
        User user = getUserFromKeyString(session.getUserKeyString());
        setUserPassword(user, newPassword);
        
        // Delete the session
        deleteSession(session.getKey());
        
        // Delete all user's login sessions
        deleteLoginSessions(session.getUserKey(), null);
        
        log.addLogInfo(LogInfoType.RESET_PASSWORD, null, true,
                RESET_PASSWORD_SUCCESS);
        return;
    }
    
    public static void sendEmailConfirm(User user, Log log) {
        // Generate forgot password session
        Session session = addSession(Session.EMAIL_CONFIRM, user.getKey(),
                genSessionID());
        
        //Send an email with a link to reset password
        String url = genEmailConfirmUrl(session.getKeyString(), session.getSessionID());
        String subject = "Email verification - thai-accounting.appspot.com";
        String msgBody = "<p>To make sure that you can reset your password. We need to "
                + "confirm your email address. All it takes is a single click.</p>"
                + "<p><a href='" + url + "'>Link to verify your email address</a></p>"
                + "<p>If the above link is not clickable, copy and paste this link into your web browser's address bar:</p>"
                + "<p>" + url + "</p>";
                
        sendEmail(FROM_EMAIL, FROM_NAME, user.getEmail(), user.getUsername(),
                subject, msgBody);
        
        log.addLogInfo(UserVerifier.LogInfoType.SEND_EMAIL_CONFIRM, null, true, null);
    }
    
    private static String genEmailConfirmUrl(String sessionKeyString, String sessionID) {
        return "http://thai-accounting.appspot.com/emailconfirm?" + PARAMETER_SSID + "="
                + sessionKeyString + "&" + PARAMETER_VID + "=" + sessionID;
    }

    public static boolean receiveEmailConfirm(String sessionKeyString,
            String sessionID) {
        
        Session session = getSession(sessionKeyString, Session.EMAIL_CONFIRM,
                sessionID);
        if (session == null) {
            return false;
        }
        
        // Set didEmailConfirm to true
        setUserData(session.getUserKey(), true);
        
        // Delete the verified email session
        deleteSession(session.getKey());
        
        return true;
    }
    
    private static void sendEmail(String fromEmail, String fromName,
            String toEmail, String toName, String subject, String msgBody) {
        
        Properties props = new Properties();
        javax.mail.Session mailSession = javax.mail.Session.getDefaultInstance(
                props, null);

        try {
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(fromEmail, fromName));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail, toName));
            msg.setSubject(subject);
            msg.setText(msgBody);
            Transport.send(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public static void changeLang(User user, String lang, Log log) {
        setUserData(user.getKey(), lang);
        log.addLogInfo(LogInfoType.CHANGE_LANG, null, true, null);
    }
    
    public static Session addLoginSession(Key userKey) {
        return addSession(Session.LOG_IN, userKey, genSessionID());
    }
    
    public static boolean deleteLoginSession(String sessionKeyString, String sessionID) {
        Session session = getSession(sessionKeyString, Session.LOG_IN, sessionID);
        deleteSession(session.getKey());
        return true;
    }

    private static void deleteLoginSessions(Key userKey,
            String currentSessionKeyString) {

        // Keep the current session in case of changing password.
        // Delete all sessions if deleting account - give current session null.
        
        List<Key> sessionKeyList = new ArrayList<Key>();
        List<String> sessionKeyStringList = new ArrayList<String>();
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(Session.class.getSimpleName());
        CompositeFilter compositeFilter =  CompositeFilterOperator.and(
                FilterOperator.EQUAL.of(Session.TYPE, Session.LOG_IN),
                FilterOperator.EQUAL.of(Session.USER_KEY, userKey));
        query.setFilter(compositeFilter);
        Iterator<Entity> iterator = ds.prepare(query).asIterator();
        while(iterator.hasNext()){
            Entity entity = iterator.next();
            Session session = new Session(entity);
            if (!session.getKeyString().equals(currentSessionKeyString)) {
                sessionKeyList.add(session.getKey());
                sessionKeyStringList.add(session.getKeyString());
            }
        }
        
        // 1. Delete from DB.
        if (!sessionKeyList.isEmpty()) {
            ds.delete(sessionKeyList);
        }

        // 2. Delete from memcache
        if (!sessionKeyStringList.isEmpty()) {
            MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
            syncCache.deleteAll(sessionKeyStringList);
        }
    }
    
    private static Session getSession(String sessionKeyString, int type,
            String sessionID) {
        Session session = getSession(sessionKeyString);
        if (session != null) {
            if (session.getType() == type
                    && session.getSessionID().equals(sessionID)) {
                return session;
            }
        }
        return null;
    }
    
    private static Session getSession(String sessionKeyString){
        
        Session session = null;
        
        // Try to get from memcache first.
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        session = (Session)syncCache.get(sessionKeyString);
        if (session != null) {
            return session;
        }
        
        // Not found in memcache, try to get from DB.
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        try {
            Key sessionKey = KeyFactory.stringToKey(sessionKeyString);
            Entity entity = ds.get(sessionKey);
            session = new Session(entity);
            
            // Save the session in memcache. 
            syncCache.put(sessionKeyString, session);
            
            return session;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (EntityNotFoundException e) {
            return null;
        }
    }
    
    private static Session addSession(int type, Key userKey, String sessionID) {
        assert(userKey != null && sessionID != null);
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Session session = new Session(type, userKey, sessionID);
        ds.put(session.getEntity());
        
        // Save the session in memcache. 
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.put(session.getKeyString(), session);
        
        return session;
    }
    
    private static void deleteSession(Key key){
        // Delete from DB
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.delete(key);
        
        // Delete from memcache
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.delete(KeyFactory.keyToString(key));
    }
    
    private static User getUserFromUsername(String username, Log log) {
        assert(UserVerifier.isUsernameValid(username, false, log));
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(User.class.getSimpleName());
        query.setAncestor(getUserGrpKey());
        query.setFilter(new FilterPredicate(User.USER_NAME, Query.FilterOperator.EQUAL, username));
        Entity entity = ds.prepare(query).asSingleEntity();
        if (entity != null) {
            User user = new User(entity);
            
            // Save the user in memcache.
            MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
            syncCache.put(user.getKeyString(), user);
            
            return user;
        }
        return null;
    }
    
    private static User getUserFromEmail(String email, Log log) {
        assert(UserVerifier.isEmailValid(email, false, log));
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(User.class.getSimpleName());
        query.setAncestor(getUserGrpKey());
        query.setFilter(new FilterPredicate(User.EMAIL, Query.FilterOperator.EQUAL, email));
        Entity entity = ds.prepare(query).asSingleEntity();
        if (entity != null) {
            User user = new User(entity);
            
            // Save the user in memcache.
            MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
            syncCache.put(user.getKeyString(), user);
            
            return user;
        }
        return null;
    }

    private static User getUserFromKeyString(String userKeyString){
        
        User user = null;
        
        // Try to get from memcache first.
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        user = (User)syncCache.get(userKeyString);
        if (user != null) {
            return user;
        }
        
        // Not found in memcache, try to get from DB.
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        try {
            Key userKey = KeyFactory.stringToKey(userKeyString);
            Entity entity = ds.get(userKey);
            user = new User(entity);
            
            // Save the user in memcache. 
            syncCache.put(userKeyString, user);
            
            return user;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (EntityNotFoundException e) {
            return null;
        }
    }
    
    private static void setUserPassword(User user, String password) {
        
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(hashPassword);
        
        // Update to DB
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();    
        ds.put(user.getEntity());
        
        // Update to memcache
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.put(user.getKeyString(), user);
    }
    
    public static UserData getUserData(Key userKey) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key userDataKey = UserData.createKey(userKey.getId());
        try {
            Entity entity = ds.get(userDataKey);
            return new UserData(entity);
        } catch (EntityNotFoundException e) {
            // Could not be null
            throw new IllegalArgumentException();
        }
    }
    
    private static void setUserData(Key userKey, boolean didEmailConfirm) {
        UserData userData = getUserData(userKey);
        userData.setEmailConfirm(didEmailConfirm);
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.put(userData.getEntity());
    }
    
    private static void setUserData(Key userKey, String lang) {
        UserData userData = getUserData(userKey);
        userData.setLang(lang);
        
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.put(userData.getEntity());
    }
    
    private static String genSessionID() {
        // a-z, A-Z, 0-9, -, _
        String s = "";
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            s += WEB_SAFE_CHARACTERS.charAt(random.nextInt(WEB_SAFE_CHARACTERS.length()));
        }
        return s;
    }
    
    private static Key userGrpKey;
    
    protected static Key getUserGrpKey() {
        if (userGrpKey == null) {
            DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
            UserGrp userGrp = new UserGrp();
            ds.put(userGrp.getEntity());
            userGrpKey = userGrp.getEntity().getKey();
        }
        return userGrpKey;
    }   
}
