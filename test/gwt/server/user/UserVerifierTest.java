package gwt.server.user;

import static org.junit.Assert.*;
import gwt.server.org.json.JSONArray;
import gwt.server.org.json.JSONException;
import gwt.server.org.json.JSONObject;
import gwt.server.user.UserVerifier.LogInfo;
import gwt.server.user.UserVerifier.LogInfoType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class UserVerifierTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    
    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
    
    @Test
    public void testLog1() {
        UserVerifier.Log log = new UserVerifier.Log();
        log.addLogInfo(LogInfoType.USERNAME, "", false, UserVerifier.ERR_NAME_LENGTH);
        
        assertFalse(log.isValid());
        
        try {
            String json = log.getJSON();
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            
            assertEquals(LogInfoType.USERNAME.name(), jsonObject.getString(LogInfo.TYPE));
            assertEquals("", jsonObject.getString(LogInfo.VALUE));
            assertEquals(false, jsonObject.getBoolean(LogInfo.IS_VALID));
            assertEquals(UserVerifier.ERR_NAME_LENGTH, jsonObject.getString(LogInfo.MSG));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLog2() {
        UserVerifier.Log log = new UserVerifier.Log();
        log.addLogInfo(LogInfoType.USERNAME, "wi", false, UserVerifier.ERR_NAME_LENGTH);
        log.addLogInfo(LogInfoType.PASSWORD, null, true, null);
        
        assertFalse(log.isValid());
        
        try {
            String json = log.getJSON();
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            
            assertEquals(LogInfoType.USERNAME.name(), jsonObject.getString(LogInfo.TYPE));
            assertEquals("wi", jsonObject.getString(LogInfo.VALUE));
            assertEquals(false, jsonObject.getBoolean(LogInfo.IS_VALID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLog3() {
        UserVerifier.Log log = new UserVerifier.Log();
        log.addLogInfo(LogInfoType.LOG_IN, null, true, null);
        
        assertTrue(log.isValid());
        
        try {
            String json = log.getJSON();
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            
            assertEquals(LogInfoType.LOG_IN.name(), jsonObject.getString(LogInfo.TYPE));
            assertEquals(true, jsonObject.getBoolean(LogInfo.IS_VALID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIsUsernameValid1() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isUsernameValid("", false, log));
        assertFalse(log.isValid());
        
        try {
            String json = log.getJSON();
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            
            assertEquals(LogInfoType.USERNAME.name(), jsonObject.getString(LogInfo.TYPE));
            assertEquals("", jsonObject.getString(LogInfo.VALUE));
            assertEquals(false, jsonObject.getBoolean(LogInfo.IS_VALID));
            assertEquals(UserVerifier.ERR_EMPTY, jsonObject.getString(LogInfo.MSG));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIsUsernameValid2() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isUsernameValid("we", false, log));
        assertFalse(log.isValid());
    }
    
    @Test
    public void testIsUsernameValid3() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertTrue(UserVerifier.isUsernameValid("wit789", true, log));
        assertTrue(log.isValid());
    }
    
    @Test
    public void testIsUsernameValid4() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isUsernameValid("wit@_-/", true, log));
        assertFalse(log.isValid());
    }
    
    @Test
    public void testIsUsernameValid5() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertTrue(UserVerifier.isUsernameValid("wit_", true, log));
        assertTrue(log.isValid());
    }
    
    @Test
    public void testIsUsernameValid6() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isUsernameValid("wi t_", true, log));
        assertFalse(log.isValid());
    }
    
    @Test
    public void testIsUsernameValid7() {
        UserVerifier.Log log = new UserVerifier.Log();
        
        UserManager.signUp("wit", "wit@example.com", "asdfjkl;", "asdfjkl;", log);
        
        assertFalse(UserVerifier.isUsernameValid("wit", true, log));
        assertFalse(log.isValid());
        
        UserVerifier.Log log2 = new UserVerifier.Log();
        assertFalse(UserVerifier.isUsernameValid("WIT", true, log2));
        assertFalse(log2.isValid());
    }
    
    @Test
    public void testIsEmailValid1() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isEmailValid("wi t_", false, log));
        assertFalse(log.isValid());
        
        try {
            String json = log.getJSON();
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            
            assertEquals(LogInfoType.EMAIL.name(), jsonObject.getString(LogInfo.TYPE));
            assertEquals("wi t_", jsonObject.getString(LogInfo.VALUE));
            assertEquals(false, jsonObject.getBoolean(LogInfo.IS_VALID));
            assertEquals(UserVerifier.ERR_EMAIL_SPACE, jsonObject.getString(LogInfo.MSG));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsEmailValid2() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertTrue(UserVerifier.isEmailValid("wit@hot.co.th", false, log));
        assertTrue(log.isValid());
    }
    
    @Test
    public void testIsEmailValid3() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isEmailValid("wit@co.", false, log));
        assertFalse(log.isValid());
    }
    
    @Test
    public void testIsEmailValid4() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isEmailValid("wit@co", false, log));
        assertFalse(log.isValid());
    }
    
    @Test
    public void testIsEmailValid5() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isEmailValid("wit.co.", false, log));
        assertFalse(log.isValid());
    }
    
    @Test
    public void testIsEmailValid6() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertTrue(UserVerifier.isEmailValid("wit@gmail.com", false, log));
        assertTrue(log.isValid());
    }
    
    @Test
    public void testIsEmailValid7() {
        UserVerifier.Log log = new UserVerifier.Log();
        
        UserManager.signUp("wit", "wit@example.com", "asdfjkl;", "asdfjkl;", log);
        
        assertFalse(UserVerifier.isEmailValid("wit@example.com", true, log));
        assertFalse(log.isValid());
    }
    
    @Test
    public void testIsPasswordValid1() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertTrue(UserVerifier.isPasswordValid("wit.co.", log, LogInfoType.PASSWORD));
        assertTrue(log.isValid());
    }
    
    @Test
    public void testIsPasswordValid2() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isPasswordValid("wi t.co.", log, LogInfoType.PASSWORD));
        assertFalse(log.isValid());
        
        try {
            String json = log.getJSON();
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            
            assertEquals(LogInfoType.PASSWORD.name(), jsonObject.getString(LogInfo.TYPE));
            assertEquals(false, jsonObject.getBoolean(LogInfo.IS_VALID));
            assertEquals(UserVerifier.ERR_PASSWORD_SPACE, jsonObject.getString(LogInfo.MSG));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIsPasswordValid3() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isPasswordValid("wio", log, LogInfoType.PASSWORD));
        assertFalse(log.isValid());
    }
    
    @Test
    public void testIsRepeatPasswordValid1() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertFalse(UserVerifier.isRepeatPasswordValid("asdf", "wio", log));
        assertFalse(log.isValid());
        
        try {
            String json = log.getJSON();
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            
            assertEquals(LogInfoType.REPEAT_PASSWORD.name(), jsonObject.getString(LogInfo.TYPE));
            assertEquals(false, jsonObject.getBoolean(LogInfo.IS_VALID));
            assertEquals(UserVerifier.ERR_REPEAT_PASSWORD, jsonObject.getString(LogInfo.MSG));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIsRepeatPasswordValid2() {
        UserVerifier.Log log = new UserVerifier.Log();
        assertTrue(UserVerifier.isRepeatPasswordValid("asdf", "asdf", log));
        assertTrue(log.isValid());
    }
}
