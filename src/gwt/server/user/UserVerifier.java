package gwt.server.user;

import gwt.server.org.json.JSONArray;
import gwt.server.org.json.JSONException;
import gwt.server.org.json.JSONObject;
import gwt.server.user.model.User;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;


public class UserVerifier {
    
    public enum LogInfoType {
        USERNAME,
        EMAIL,
        PASSWORD,
        REPEAT_PASSWORD,
        NEW_PASSWORD,

        SIGN_UP,
        LOG_IN,
        CHANGE_USERNAME,
        CHANGE_EMAIL,
        CHANGE_PASSWORD,
        CHANGE_LANG,
        DELETE_ACCOUNT,
        FORGOT_PASSWORD,
        RESET_PASSWORD,
        SEND_EMAIL_CONFIRM,
        
        DID_LOG_IN
    }
    
    public static class LogInfo {
        
        public static final String TYPE = "type";
        public static final String VALUE = "value";
        public static final String IS_VALID = "isValid";
        public static final String MSG = "msg";
        
        public LogInfoType type;
        public String value;
        public boolean isValid;
        public String msg;
        
        public LogInfo(LogInfoType type, String value, boolean isValid,
                String msg) {
            this.type = type;
            this.value = value;
            this.isValid = isValid;
            this.msg = msg;
        }
        
        public JSONObject getJSONObject() throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(TYPE, type);
            jsonObject.put(VALUE, value);
            jsonObject.put(IS_VALID, isValid);
            jsonObject.put(MSG, msg);
            return jsonObject;
        }
    }
    
    public static class Log {

        private ArrayList<LogInfo> logInfoList;
        
        public Log() {
            logInfoList = new ArrayList<LogInfo>();
        }
        
        public void addLogInfo(LogInfoType type, String value, boolean isValid,
                String msg) {
            logInfoList.add(new LogInfo(type, value, isValid, msg));
        }
        
        public boolean isValid() {
            for (LogInfo logInfo : logInfoList) {
                if (logInfo.isValid == false) {
                    return false;
                }
            }
            return true;
        }
        
        public String getJSON() throws JSONException {
            JSONArray jsonArray = new JSONArray();
            for (LogInfo logInfo : logInfoList) {
                jsonArray.put(logInfo.getJSONObject());
            }
            return jsonArray.toString();
        }
    }
    
    public static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
    public static final String UPPER_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public static final String ERR_LOG_IN = "Your username or password are not correct.";
    public static final String ERR_NAME_TAKEN = "Someone already has that username. Please try another.";
    public static final String ERR_EMAIL_TAKEN = "Someone already has that email. Please try another.";
    public static final String ERR_PASSWORD = "Password is not correct.";
    public static final String ERR_USER_NOT_FOUND = "This username or email is not available.";

    public static final String ERR_EMPTY = "This field is required.";
    public static final String ERR_NAME_LENGTH = "Please use between 3 and 30 characters.";
    public static final String ERR_NAME_CHARACTERS = "Please use only letters (a-z, A-Z), numbers, and _.";
    public static final String ERR_EMAIL_SPACE = "Email can not contain space.";
    public static final String ERR_EMAIL_FORMAT = "Email must contain @ and domain name i.e. @example.com";
    public static final String ERR_PASSWORD_LENGTH = "Short passwords are easy to guess. Try one with at least 7 characters.";
    public static final String ERR_PASSWORD_SPACE = "Password can not contain space.";
    public static final String ERR_REPEAT_PASSWORD = "These passwords don't match. Please try again.";
    
    public static boolean isUsernameValid(String username, boolean checkDuplicate,
            Log log) {
        
        if (username == null) {
            throw new IllegalArgumentException(username);
        }
        
        if (username.isEmpty()) {
            log.addLogInfo(LogInfoType.USERNAME, username, false, ERR_EMPTY);
            return false;
        }
        
        if (username.length() < 3 || username.length() > 30) {
            log.addLogInfo(LogInfoType.USERNAME, username, false, ERR_NAME_LENGTH);
            return false;
        }
        
        //Loop every character, allow only letters, numbers, and _. No space.
        for (int i = 0; i < username.length(); i++) {
            CharSequence s = username.subSequence(i, i + 1);
            if(!ALLOWED_CHARACTERS.contains(s)) {
                log.addLogInfo(LogInfoType.USERNAME, username, false,
                        ERR_NAME_CHARACTERS);
                return false;
            }
        }
        
        // No need to check if duplicate for logging in.
        if (checkDuplicate) {
            DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
            
            // Check if username is duplicate. Compare it with lowercase.
            String lowerCaseUsername = username.toLowerCase();
            
            Query usernameQuery = new Query(User.class.getSimpleName());
            usernameQuery.setAncestor(UserManager.getUserGrpKey());
            usernameQuery.setFilter(new FilterPredicate(User.LOWER_CASE_USER_NAME,
                    Query.FilterOperator.EQUAL, lowerCaseUsername));
            Entity entity1 = ds.prepare(usernameQuery).asSingleEntity();
            if ( entity1 != null ) {
                log.addLogInfo(LogInfoType.USERNAME, username, false, ERR_NAME_TAKEN);
                return false;
            }
        }
        
        log.addLogInfo(LogInfoType.USERNAME, username, true, null);
        return true;
    }
    
    public static boolean isEmailValid(String email, boolean checkDuplicate,
            Log log) {
        if (email == null) {
            throw new IllegalArgumentException(email);
        }
        
        if (email.isEmpty()){
            log.addLogInfo(LogInfoType.EMAIL, email, false, ERR_EMPTY);
            return false;
        }

        //No space
        if(email.contains(" ")) {
            log.addLogInfo(LogInfoType.EMAIL, email, false, ERR_EMAIL_SPACE);
            return false;
        }
        
        if (email.length() < 5) {
            log.addLogInfo(LogInfoType.EMAIL, email, false, ERR_EMAIL_FORMAT);
            return false;
        }

        //Loop every character, uppercase not allowed.
        for (int i = 0; i < email.length(); i++) {
            CharSequence s = email.subSequence(i, i + 1);
            if(UPPER_CHARACTERS.contains(s)) {
                throw new IllegalArgumentException(email);
            }
        }
        
        if (!email.contains("@")) {
            log.addLogInfo(LogInfoType.EMAIL, email, false, ERR_EMAIL_FORMAT);
            return false;
        }
        
        String x = email.substring(email.indexOf("@") + 1);
        if (!x.contains(".")){
            log.addLogInfo(LogInfoType.EMAIL, email, false, ERR_EMAIL_FORMAT);
            return false;
        }
        
        // example.com, example.co.th
        if (x.charAt(0) == '.' || x.charAt(x.length() - 1) == '.') {
            log.addLogInfo(LogInfoType.EMAIL, email, false, ERR_EMAIL_FORMAT);
            return false;
        }
        
        // No need to check if duplicate for logging in.
        if (checkDuplicate) {
            DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
            Query emailQuery = new Query(User.class.getSimpleName());
            emailQuery.setAncestor(UserManager.getUserGrpKey());
            emailQuery.setFilter(new FilterPredicate(User.EMAIL,
                    Query.FilterOperator.EQUAL, email));
            Entity entity1 = ds.prepare(emailQuery).asSingleEntity();
            if ( entity1 != null ) {
                log.addLogInfo(LogInfoType.EMAIL, email, false, ERR_EMAIL_TAKEN);
                return false;
            }
        }
        
        log.addLogInfo(LogInfoType.EMAIL, email, true, null);
        return true;
    }
    
    public static boolean isPasswordValid(String password, Log log, LogInfoType type) {
        if (password == null) {
            throw new IllegalArgumentException(password);
        }
        
        if (password.isEmpty()) {
            log.addLogInfo(type, null, false, ERR_EMPTY);
            return false;
        }
        
        //No space
        if(password.contains(" ")) {
            log.addLogInfo(type, null, false, ERR_PASSWORD_SPACE);
            return false;
        }
        
        if (password.length() < 7) {
            log.addLogInfo(type, null, false, ERR_PASSWORD_LENGTH);
            return false;
        }
        
        log.addLogInfo(type, null, true, null);
        return true;
    }
    
    public static boolean isRepeatPasswordValid(String password,
            String repeatPassword, Log log) {

        if (password == null || repeatPassword == null) {
            throw new IllegalArgumentException(password);
        }
        
        if (!password.equals(repeatPassword)) {
            log.addLogInfo(LogInfoType.REPEAT_PASSWORD, null, false, ERR_REPEAT_PASSWORD);
            return false;
        }
        
        log.addLogInfo(LogInfoType.REPEAT_PASSWORD, null, true, null);
        return true;
    }
}
