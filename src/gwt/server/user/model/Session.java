package gwt.server.user.model;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Session implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final int LOG_IN = 1;
    public static final int EMAIL_CONFIRM = 2;
    public static final int FORGOT_PASSWORD = 3;
    
    public static final String TYPE = "type";
    public static final String USER_KEY = "userKey";
    public static final String SESSION_ID = "sessionID";
    public static final String CREATE_DATE = "createDate";
    
    private Entity entity;
    
    public Session(int type, Key userKey, String sessionID) {
        
        if (userKey == null || sessionID == null) {
            throw new IllegalArgumentException();
        }
        
        entity = new Entity(Session.class.getSimpleName());
        entity.setProperty(TYPE, type);
        entity.setProperty(USER_KEY, userKey);
        entity.setProperty(SESSION_ID, sessionID);
        entity.setProperty(CREATE_DATE, new Date());
    }
    
    public Session(Entity entity) {
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
    
    public int getType() {
        // The value returned may not be the same type
        // as originally set via setProperty.
        return ((Number)entity.getProperty(TYPE)).intValue();
    }
    
    public Key getUserKey() {
        return (Key)entity.getProperty(USER_KEY);
    }
    
    public String getUserKeyString() {
        return KeyFactory.keyToString(getUserKey());
    }
    
    public String getSessionID() {
        return (String)entity.getProperty(SESSION_ID);
    }

    public Date getCreateDate() {
        return (Date)entity.getProperty(CREATE_DATE);
    }
}
