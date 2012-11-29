package gwt.server.user.model;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String USER_NAME = "username";
    public static final String LOWER_CASE_USER_NAME = "lowerCaseUsername";
    public static final String EMAIL = "email";
    public static final String HASH_PASSWORD = "hashPassword";
    public static final String CREATE_DATE = "createDate";
    
    private Entity entity;

    public User(){
        new User(null, null, null, null, null);
    }
    
    public User(Key userGrpKey, String username, String lowerCaseUsername,
            String email, String hashPassword) {
        entity = new Entity(User.class.getSimpleName(), userGrpKey);
        entity.setProperty(USER_NAME, username);
        entity.setProperty(LOWER_CASE_USER_NAME, lowerCaseUsername);
        entity.setProperty(EMAIL, email);
        entity.setProperty(HASH_PASSWORD, hashPassword);
        entity.setProperty(CREATE_DATE, new Date());
    }
    
    public User(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return entity;
    }

    public Key getKey() {
        return entity.getKey();
    }
    
    public String getKeyString() {
        return KeyFactory.keyToString(entity.getKey());
    }

    public String getUsername() {
        return (String)entity.getProperty(USER_NAME);
    }
    
    public String getEmail() {
        return (String)entity.getProperty(EMAIL);
    }

    public String getHashPassword() {
        return (String)entity.getProperty(HASH_PASSWORD);
    }
    
    public Date getCreateDate() {
        return (Date)entity.getProperty(CREATE_DATE);
    }
    
    public void setUsername(String newUsername, String newLowerCaseUsername) {
        entity.setProperty(USER_NAME, newUsername);
        entity.setProperty(LOWER_CASE_USER_NAME, newLowerCaseUsername);
    }
    
    public void setEmail(String newEmail) {
        entity.setProperty(EMAIL, newEmail);
    }

    public void setPassword(String hashPassword) {
        entity.setProperty(HASH_PASSWORD, hashPassword);
    }
}
