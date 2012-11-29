package gwt.server.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gwt.Reflector;
import gwt.server.org.json.JSONArray;
import gwt.server.org.json.JSONException;
import gwt.server.org.json.JSONObject;
import gwt.server.user.UserVerifier.LogInfo;
import gwt.server.user.UserVerifier.LogInfoType;
import gwt.server.user.model.Session;
import gwt.server.user.model.User;
import gwt.server.user.model.UserData;
import gwt.server.user.model.UserGrp;
import gwt.shared.NotLoggedInException;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.mail.MailServicePb.MailMessage;
import com.google.appengine.api.mail.dev.LocalMailService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.tools.development.ApiProxyLocal;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMailServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.apphosting.api.ApiProxy;

public class UserManagerTest {

    public static void assertLogInfoType(UserVerifier.Log log, LogInfoType type,
            String value, boolean isValid, String msg) {
        try {
            String json = log.getJSON();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (type.name().equals(jsonObject.getString(LogInfo.TYPE))
                        && isValid == jsonObject.getBoolean(LogInfo.IS_VALID)) {
                    // If value is null, no this property in JsonObject.
                    if (value == null) {
                        try {
                            jsonObject.getString(LogInfo.VALUE);
                            fail();
                        } catch (JSONException e) { }
                    } else {
                        assertEquals(value, jsonObject.getString(LogInfo.VALUE));
                    }

                    if (msg == null) {
                        try {
                            jsonObject.getString(LogInfo.MSG);
                            fail();
                        } catch (JSONException e) { }
                    } else {
                        assertEquals(msg, jsonObject.getString(LogInfo.MSG));
                    }
                    return;
                }
            }
            fail();
        } catch (JSONException e) {
            fail();
        }
    }
    
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig(),
            new LocalMemcacheServiceTestConfig(),
            new LocalMailServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testSignUp1() {

        // User1
        UserVerifier.Log log1 = new UserVerifier.Log();
        User user1 = UserManager.signUp("wit", "wit@g.c", "asdfjkl;",
                "asdfjkl;", log1);
        assertEquals("wit", user1.getUsername());
        assertEquals("wit@g.c", user1.getEmail());
        assertEquals(true, BCrypt.checkpw("asdfjkl;", user1.getHashPassword()));
        assertEquals(true, log1.isValid());

        // User2
        UserVerifier.Log log2 = new UserVerifier.Log();
        User user2 = UserManager.signUp("wit", "wit_t@g.c", "asdfjkl;",
                "asdfjkl;", log2);
        assertNull(user2);
        assertEquals(false, log2.isValid());
        assertLogInfoType(log2, LogInfoType.USERNAME, "wit", false,
                UserVerifier.ERR_NAME_TAKEN);

        // User3
        UserVerifier.Log log3 = new UserVerifier.Log();
        User user3 = UserManager.signUp("Manee", "manee@g.c", "asdf1234",
                "asdf1234", log3);
        assertEquals("Manee", user3.getUsername());
        assertEquals("manee@g.c", user3.getEmail());
        assertEquals(true, BCrypt.checkpw("asdf1234", user3.getHashPassword()));
        assertEquals(true, log3.isValid());

        // User4
        UserVerifier.Log log4 = new UserVerifier.Log();
        User user4 = UserManager.signUp("Som", "manee@g.c", "asdfjkl;",
                "asdfjkl;", log4);
        assertNull(user4);
        assertEquals(false, log4.isValid());
        assertLogInfoType(log4, LogInfoType.EMAIL, "manee@g.c", false,
                UserVerifier.ERR_EMAIL_TAKEN);

        // User5
        UserVerifier.Log log5 = new UserVerifier.Log();
        User user5 = UserManager.signUp("Som2", "maidee@g.c", "a123",
                "a123;", log5);
        assertNull(user5);
        assertEquals(false, log5.isValid());
        assertLogInfoType(log5, LogInfoType.PASSWORD, null, false,
                UserVerifier.ERR_PASSWORD_LENGTH);
    }

    @Test
    public void testDeleteAccount1() {

        UserVerifier.Log log1 = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log1);

        UserVerifier.Log log2 = new UserVerifier.Log();
        UserManager.deleteAccount(user, "raknaja", log2);
        assertFalse(log2.isValid());
        assertLogInfoType(log2, LogInfoType.PASSWORD, null, false,
                UserVerifier.ERR_PASSWORD);

        UserVerifier.Log log3 = new UserVerifier.Log();
        UserManager.deleteAccount(user, "raknaja2", log3);
        assertTrue(log3.isValid());
        assertLogInfoType(log3, LogInfoType.DELETE_ACCOUNT, null, true,
                null);
    }

    @Test
    public void testCheckUsernameOrEmailAndPassword1() {

        UserVerifier.Log log = new UserVerifier.Log();
        UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);

        // Login1
        UserVerifier.Log log1 = new UserVerifier.Log();
        UserManager.checkUsernameOrEmailAndPassword("wit", "raknaja2",
                log1);
        assertEquals(true, log1.isValid());
        assertLogInfoType(log1, LogInfoType.LOG_IN, null, true, null);

        // Login2
        UserVerifier.Log log2 = new UserVerifier.Log();
        UserManager.checkUsernameOrEmailAndPassword("wit@gmail.c", "raknaja2",
                log2);
        assertEquals(true, log2.isValid());
        assertLogInfoType(log2, LogInfoType.LOG_IN, null, true, null);

        // Login3
        UserVerifier.Log log3 = new UserVerifier.Log();
        UserManager.checkUsernameOrEmailAndPassword("wit@gmil.c", "raknaja2",
                log3);
        assertEquals(false, log3.isValid());
        assertLogInfoType(log3, LogInfoType.LOG_IN, null, false, UserVerifier.ERR_LOG_IN);

        // Login4
        UserVerifier.Log log4 = new UserVerifier.Log();
        UserManager.checkUsernameOrEmailAndPassword("Wit", "raknaja2",
                log4);
        assertEquals(false, log4.isValid());
        assertLogInfoType(log4, LogInfoType.LOG_IN, null, false, UserVerifier.ERR_LOG_IN);
    }

    @Test
    public void testCheckLoggedInAndGetUser1() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        Session session = UserManager.addLoginSession(user.getKey());

        // Check1
        try {
            User user1 = UserManager.checkLoggedInAndGetUser(session.getKeyString(),
                    session.getSessionID());
            assertEquals(user.getKeyString(), user1.getKeyString());
        } catch (NotLoggedInException e) {
            fail();
        }

        // Check2
        try {
            UserManager.checkLoggedInAndGetUser(session.getKeyString(),
                    "asdfjkl");
            fail();
        } catch (NotLoggedInException e) {

        }

        // Check3
        try {
            UserManager.checkLoggedInAndGetUser("asdkjfl_jksd",
                    session.getSessionID());
            fail();
        } catch (NotLoggedInException e) {

        }
    }

    @Test
    public void testIsLoggedIn1() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        Session session = UserManager.addLoginSession(user.getKey());

        assertTrue(UserManager.isLoggedIn(session.getKeyString(),
                session.getSessionID()));
        assertFalse(UserManager.isLoggedIn("session",
                session.getSessionID()));
        assertFalse(UserManager.isLoggedIn(session.getKeyString(),
                "session"));

    }

    @Test
    public void testChangeUsername1() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);

        // Change1
        UserVerifier.Log log1 = new UserVerifier.Log();
        UserManager.changeUsername(user, "wit2", "raknaja", log1);
        assertFalse(log1.isValid());
        assertLogInfoType(log1, LogInfoType.PASSWORD, null, false,
                UserVerifier.ERR_PASSWORD);

        // Change2
        UserVerifier.Log log2 = new UserVerifier.Log();
        UserManager.changeUsername(user, "wit2", "raknaja2", log2);
        assertTrue(log2.isValid());
        assertLogInfoType(log2, LogInfoType.CHANGE_USERNAME, null, true,
                null);

        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {String.class};
        Object[] argObjects2 = {user.getKeyString()};
        User user2 = (User)Reflector.invokePrivateMethod(UserManager.class, "getUserFromKeyString",
                argClasses2, null, argObjects2);
        assertEquals("wit2", user2.getUsername());
    }

    @Test
    public void testChangeEmail1() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);

        // Change1
        UserVerifier.Log log1 = new UserVerifier.Log();
        UserManager.changeEmail(user, "wit2", "raknaja2", log1);
        assertFalse(log1.isValid());
        assertLogInfoType(log1, LogInfoType.EMAIL, "wit2", false,
                UserVerifier.ERR_EMAIL_FORMAT);

        // Change2
        UserVerifier.Log log2 = new UserVerifier.Log();
        UserManager.changeEmail(user, "wi@gm.com", "raknaja2", log2);
        assertTrue(log2.isValid());
        assertLogInfoType(log2, LogInfoType.CHANGE_EMAIL, null, true,
                null);

        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {String.class};
        Object[] argObjects2 = {user.getKeyString()};
        User user2 = (User)Reflector.invokePrivateMethod(UserManager.class, "getUserFromKeyString",
                argClasses2, null, argObjects2);
        assertEquals("wi@gm.com", user2.getEmail());
    }

    @Test
    public void testChangePassword() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        Session session = UserManager.addLoginSession(user.getKey());

        // Change1
        UserVerifier.Log log1 = new UserVerifier.Log();
        UserManager.changePassword(user, session.getKeyString(), "asdfjkl;",
                "asdfjkl;", "raknajaa", log1);
        assertFalse(log1.isValid());
        assertLogInfoType(log1, LogInfoType.PASSWORD, null, false,
                UserVerifier.ERR_PASSWORD);

        // Change2
        UserVerifier.Log log2 = new UserVerifier.Log();
        UserManager.changePassword(user, session.getKeyString(), "iamcool99",
                "iamcool99", "raknaja2", log2);
        assertTrue(log2.isValid());
        assertLogInfoType(log2, LogInfoType.CHANGE_PASSWORD, null, true,
                null);

        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {String.class};
        Object[] argObjects2 = {user.getKeyString()};
        User user2 = (User)Reflector.invokePrivateMethod(UserManager.class, "getUserFromKeyString",
                argClasses2, null, argObjects2);
        assertTrue(BCrypt.checkpw("iamcool99", user2.getHashPassword()));
    }

    @Test
    public void testForgotPassword() {
        UserVerifier.Log log = new UserVerifier.Log();
        UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);

        // Forgot1
        UserVerifier.Log log1 = new UserVerifier.Log();
        UserManager.forgotPassword("witter", log1);
        assertFalse(log1.isValid());
        assertLogInfoType(log1, LogInfoType.FORGOT_PASSWORD, "witter",
                false, UserVerifier.ERR_USER_NOT_FOUND);

        // Forgot2
        UserVerifier.Log log2 = new UserVerifier.Log();
        UserManager.forgotPassword("wit", log2);
        assertTrue(log2.isValid());
        assertLogInfoType(log2, LogInfoType.FORGOT_PASSWORD, "wit",
                true, UserManager.FORGOT_RESULT);
    }

    @Test
    public void testGenForgotUrl() {
        @SuppressWarnings("rawtypes")
        Class[] argClasses = {String.class, String.class};
        Object[] argObjects = {"test1", "test2"};
        String url = (String)Reflector.invokePrivateMethod(UserManager.class, "genForgotUrl",
                argClasses, null, argObjects);
        assertEquals("http://thai-accounting.appspot.com/forgot?ssid=test1&fid=test2", url);
    }

    @Test
    public void testResetPassword() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);

        @SuppressWarnings("rawtypes")
        Class[] argClasses = {};
        Object[] argObjects = {};
        String sessionID = (String)Reflector.invokePrivateMethod(UserManager.class, "genSessionID",
                argClasses, null, argObjects);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses1 = {int.class, Key.class, String.class};
        Object[] argObjects1 = {Session.FORGOT_PASSWORD, user.getKey(), sessionID};
        Session session = (Session)Reflector.invokePrivateMethod(UserManager.class, "addSession",
                argClasses1, null, argObjects1);

        UserVerifier.Log log1 = new UserVerifier.Log();
        UserManager.resetPassword(session.getKeyString(), session.getSessionID(),
                "12345jkl;", "12345jkl;", log1);
        assertTrue(log1.isValid());
        assertLogInfoType(log1, LogInfoType.RESET_PASSWORD, null, true,
                UserManager.RESET_PASSWORD_SUCCESS);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {String.class};
        Object[] argObjects2 = {user.getKeyString()};
        User user2 = (User)Reflector.invokePrivateMethod(UserManager.class, "getUserFromKeyString",
                argClasses2, null, argObjects2);
        assertTrue(BCrypt.checkpw("12345jkl;", user2.getHashPassword()));
    }
    
    @Test
    public void testSendEmailConfirm() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        UserManager.sendEmailConfirm(user, log);
        
        assertTrue(log.isValid());
        assertLogInfoType(log, UserVerifier.LogInfoType.SEND_EMAIL_CONFIRM,
                null, true, null);
    }
    
    @Test
    public void testGenEmailConfirmUrl() {
        @SuppressWarnings("rawtypes")
        Class[] argClasses = {String.class, String.class};
        Object[] argObjects = {"test1", "test2"};
        String url = (String)Reflector.invokePrivateMethod(UserManager.class, "genEmailConfirmUrl",
                argClasses, null, argObjects);
        assertEquals("http://thai-accounting.appspot.com/emailconfirm?ssid=test1&vid=test2", url);
    }
    
    @Test
    public void testReceiveEmailConfirm() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);

        @SuppressWarnings("rawtypes")
        Class[] argClasses = {};
        Object[] argObjects = {};
        String sessionID = (String)Reflector.invokePrivateMethod(UserManager.class, "genSessionID",
                argClasses, null, argObjects);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses1 = {int.class, Key.class, String.class};
        Object[] argObjects1 = {Session.EMAIL_CONFIRM, user.getKey(), sessionID};
        Session session = (Session)Reflector.invokePrivateMethod(UserManager.class, "addSession",
                argClasses1, null, argObjects1);
        
        assertTrue(UserManager.receiveEmailConfirm(session.getKeyString(),
                session.getSessionID()));
        
        UserData userData = UserManager.getUserData(user.getKey());
        assertTrue(userData.didEmailComfirm());
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {String.class};
        Object[] argObjects2 = {session.getKeyString()};
        Session session2 = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses2, null, argObjects2);
        assertNull(session2);
    }
    
    @Test
    public void testSendEmail() {
        @SuppressWarnings("rawtypes")
        Class[] argClasses = {String.class, String.class, String.class, String.class,
                String.class, String.class};
        Object[] argObjects = {"bob@mail.com", "Bob", "sara@mail.com", "Sara",
                "Let's go wild!", "Read the header."};
        Reflector.invokePrivateMethod(UserManager.class, "sendEmail",
                argClasses, null, argObjects);

        ApiProxyLocal proxy = (ApiProxyLocal) ApiProxy.getDelegate();
        LocalMailService mailService =
                (LocalMailService)proxy.getService(LocalMailService.PACKAGE);
        List<MailMessage> mailMessageList = mailService.getSentMessages();
        MailMessage mailMessage = mailMessageList.get(0);
        assertEquals("Sara <sara@mail.com>", mailMessage.getTo(0));
        assertEquals("Bob <bob@mail.com>", mailMessage.getSender());
        assertEquals("Let's go wild!", mailMessage.getSubject());
        assertEquals("Read the header.", mailMessage.getTextBody());
    }
    
    @Test
    public void testChangeLang() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        UserVerifier.Log log1 = new UserVerifier.Log();
        UserManager.changeLang(user, "mb", log1);
        assertTrue(log1.isValid());
        assertLogInfoType(log1, LogInfoType.CHANGE_LANG, null, true,
                null);

        UserData userData = UserManager.getUserData(user.getKey());
        assertEquals("mb", userData.getLang());
    }
    
    @Test
    public void testAddLoginSession() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        Session session = UserManager.addLoginSession(user.getKey());
        
        assertEquals(Session.LOG_IN, session.getType());
        assertEquals(user.getKey(), session.getUserKey());
        assertEquals(user.getKeyString(), session.getUserKeyString());
        assertNotNull(session.getSessionID());
        
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();

        assertTrue(ms.contains(session.getKeyString()));
        Session session2 = (Session)ms.get(session.getKeyString());
        assertEquals(session.getType(), session2.getType());
        assertEquals(session.getSessionID(), session2.getSessionID());
    }
    
    @Test
    public void testDeleteLoginSession() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        Session session = UserManager.addLoginSession(user.getKey());
        
        assertTrue(UserManager.deleteLoginSession(session.getKeyString(),
                session.getSessionID()));
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {String.class};
        Object[] argObjects2 = {session.getKeyString()};
        Session session2 = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses2, null, argObjects2);
        assertNull(session2);
        
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        assertFalse(ms.contains(session.getKeyString()));
    }
    
    @Test
    public void testDeleteLoginSessions() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        Session session1 = UserManager.addLoginSession(user.getKey());
        Session session2 = UserManager.addLoginSession(user.getKey());
        Session session3 = UserManager.addLoginSession(user.getKey());
        Session session4 = UserManager.addLoginSession(user.getKey());
        Session session5 = UserManager.addLoginSession(user.getKey());
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses0 = {Key.class, String.class};
        Object[] argObjects0 = {user.getKey(), session2.getKeyString()};
        Reflector.invokePrivateMethod(UserManager.class, "deleteLoginSessions", argClasses0, null, argObjects0);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses = {String.class};
        Object[] argObjects1 = {session1.getKeyString()};
        Session session1_tmp = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses, null, argObjects1);
        assertNull(session1_tmp);
        
        Object[] argObjects2 = {session2.getKeyString()};
        Session session2_tmp = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses, null, argObjects2);
        assertNotNull(session2_tmp);
        
        Object[] argObjects3 = {session3.getKeyString()};
        Session session3_tmp = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses, null, argObjects3);
        assertNull(session3_tmp);
        
        Object[] argObjects4 = {session4.getKeyString()};
        Session session4_tmp = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses, null, argObjects4);
        assertNull(session4_tmp);
        
        Object[] argObjects5 = {session5.getKeyString()};
        Session session5_tmp = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses, null, argObjects5);
        assertNull(session5_tmp);
        
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        assertFalse(ms.contains(session1.getKeyString()));
        assertTrue(ms.contains(session2.getKeyString()));
        assertFalse(ms.contains(session3.getKeyString()));
        assertFalse(ms.contains(session4.getKeyString()));
        assertFalse(ms.contains(session5.getKeyString()));
    }
    
    @Test
    public void testGetSession() {
        
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses = {int.class, Key.class, String.class};
        Object[] argObjects = {Session.EMAIL_CONFIRM, user.getKey(), "ddeik329kdianf"};
        Session session = (Session)Reflector.invokePrivateMethod(UserManager.class, "addSession",
                argClasses, null, argObjects);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {String.class};
        Object[] argObjects2 = {session.getKeyString()};
        Session session2 = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses2, null, argObjects2);
        
        assertEquals(Session.EMAIL_CONFIRM, session2.getType());
        assertEquals(user.getKeyString(), session2.getUserKeyString());
        assertEquals("ddeik329kdianf", session2.getSessionID());
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses3 = {String.class, int.class, String.class};
        Object[] argObjects3 = {session.getKeyString(), Session.EMAIL_CONFIRM,
                "ddeik329kdianf"};
        Session session3 = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses3, null, argObjects3);
        
        assertEquals(Session.EMAIL_CONFIRM, session3.getType());
        assertEquals(user.getKeyString(), session3.getUserKeyString());
        assertEquals("ddeik329kdianf", session3.getSessionID());
    }
    
    @Test
    public void testAddSession() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses = {int.class, Key.class, String.class};
        Object[] argObjects = {Session.EMAIL_CONFIRM, user.getKey(), "ddeik329kdianf"};
        Session session = (Session)Reflector.invokePrivateMethod(UserManager.class, "addSession",
                argClasses, null, argObjects);
        
        assertEquals(Session.EMAIL_CONFIRM, session.getType());
        assertEquals(user.getKeyString(), session.getUserKeyString());
        assertEquals("ddeik329kdianf", session.getSessionID());
        
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        Session session_tmp = (Session)ms.get(session.getKeyString());
        assertEquals(Session.EMAIL_CONFIRM, session_tmp.getType());
        assertEquals(user.getKeyString(), session_tmp.getUserKeyString());
        assertEquals("ddeik329kdianf", session_tmp.getSessionID());
    }
    
    @Test
    public void testDeleteSession() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses1 = {int.class, Key.class, String.class};
        Object[] argObjects1 = {Session.EMAIL_CONFIRM, user.getKey(), "ddeik329kdianf"};
        Session session = (Session)Reflector.invokePrivateMethod(UserManager.class, "addSession",
                argClasses1, null, argObjects1);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {Key.class};
        Object[] argObjects2 = {session.getKey()};
        Reflector.invokePrivateMethod(UserManager.class, "deleteSession", argClasses2,
                null, argObjects2);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses3 = {String.class};
        Object[] argObjects3 = {session.getKeyString()};
        Session session3 = (Session)Reflector.invokePrivateMethod(UserManager.class, "getSession",
                argClasses3, null, argObjects3);
        assertNull(session3);
        
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        assertFalse(ms.contains(session.getKeyString()));
    }
    
    @Test
    public void testGetUserFromUsername() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        UserVerifier.Log log1 = new UserVerifier.Log();
        @SuppressWarnings("rawtypes")
        Class[] argClasses1 = {String.class, UserVerifier.Log.class};
        Object[] argObjects1 = {"wit", log1};
        User user1 = (User)Reflector.invokePrivateMethod(UserManager.class,
                "getUserFromUsername", argClasses1, null, argObjects1);
        
        assertEquals(user.getUsername(), user1.getUsername());
        assertEquals(user.getEmail(), user1.getEmail());
        assertEquals(user.getHashPassword(), user1.getHashPassword());
    }
    
    @Test
    public void testGetUserFromEmail() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        UserVerifier.Log log1 = new UserVerifier.Log();
        @SuppressWarnings("rawtypes")
        Class[] argClasses1 = {String.class, UserVerifier.Log.class};
        Object[] argObjects1 = {"wit@gmail.c", log1};
        User user1 = (User)Reflector.invokePrivateMethod(UserManager.class,
                "getUserFromEmail", argClasses1, null, argObjects1);
        
        assertEquals(user.getUsername(), user1.getUsername());
        assertEquals(user.getEmail(), user1.getEmail());
        assertEquals(user.getHashPassword(), user1.getHashPassword());
    }
    
    @Test
    public void testGetUserFromKeyString() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses1 = {String.class};
        Object[] argObjects1 = {user.getKeyString()};
        User user1 = (User)Reflector.invokePrivateMethod(UserManager.class,
                "getUserFromKeyString", argClasses1, null, argObjects1);
        
        assertEquals(user.getUsername(), user1.getUsername());
        assertEquals(user.getEmail(), user1.getEmail());
        assertEquals(user.getHashPassword(), user1.getHashPassword());
    }
    
    @Test
    public void testSetUserPassword() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses1 = {User.class, String.class};
        Object[] argObjects1 = {user, "1212312121"};
        Reflector.invokePrivateMethod(UserManager.class, "setUserPassword", argClasses1,
                null, argObjects1);
        
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        User user1 = (User)ms.get(user.getKeyString());
        assertEquals("wit", user1.getUsername());
        assertEquals("wit@gmail.c", user1.getEmail());
        assertTrue(BCrypt.checkpw("1212312121", user1.getHashPassword()));
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {String.class};
        Object[] argObjects2 = {user.getKeyString()};
        User user2 = (User)Reflector.invokePrivateMethod(UserManager.class,
                "getUserFromKeyString", argClasses2, null, argObjects2);
        assertEquals("wit", user2.getUsername());
        assertEquals("wit@gmail.c", user2.getEmail());
        assertTrue(BCrypt.checkpw("1212312121", user2.getHashPassword()));
    }
    
    @Test
    public void testGetUserData() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        UserData userData = UserManager.getUserData(user.getKey());
        
        assertEquals(user.getKey().getId(), userData.getKey().getId());
        assertFalse(userData.didEmailComfirm());
    }
    
    @Test
    public void testSetUserData() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user1 = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses2 = {Key.class, boolean.class};
        Object[] argObjects2 = {user1.getKey(), true};
        Reflector.invokePrivateMethod(UserManager.class,
                "setUserData", argClasses2, null, argObjects2);
        
        UserData userData1 = UserManager.getUserData(user1.getKey());
        
        assertEquals(user1.getKey().getId(), userData1.getKey().getId());
        assertTrue(userData1.didEmailComfirm());
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses3 = {Key.class, String.class};
        Object[] argObjects3 = {user1.getKey(), "th"};
        Reflector.invokePrivateMethod(UserManager.class,
                "setUserData", argClasses3, null, argObjects3);
        
        UserData userData2 = UserManager.getUserData(user1.getKey());
        
        assertEquals(user1.getKey().getId(), userData2.getKey().getId());
        assertTrue(userData2.didEmailComfirm());
    }
    
    @Test
    public void testGenSessionID() {
        @SuppressWarnings("rawtypes")
        Class[] argClasses = {};
        Object[] argObjects = {};
        String sessionID = (String)Reflector.invokePrivateMethod(UserManager.class,
                "genSessionID", argClasses, null, argObjects);
        
        assertNotNull(sessionID);
        assertEquals(32, sessionID.length());
    }
    
    @Test
    public void testGetUserGrpKey() {
        Key key = UserManager.getUserGrpKey();
        assertNotNull(key);
        assertEquals(UserGrp.USER_GRP_KEY_NAME, key.getName());
    }
}
