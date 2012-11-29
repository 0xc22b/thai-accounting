package gwt.server.user.model;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UserData {
    
    public static final String DID_EMAIL_CONFIRM = "didEmailConfirm";
    public static final String LANG = "lang";
    
    private Entity entity;
    
    public UserData(Key userKey) {
        // Always be the same id as User
        entity = new Entity(UserData.class.getSimpleName(), userKey.getId());
        entity.setProperty(DID_EMAIL_CONFIRM, false);
        entity.setProperty(LANG, "en");
    }
    
    public UserData(Entity entity) {
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

    public boolean didEmailComfirm() {
        return (Boolean)entity.getProperty(DID_EMAIL_CONFIRM);
    }
    
    public String getLang() {
        return (String)entity.getProperty(LANG);
    }
    
    public void setEmailConfirm(boolean didEmailConfirm) {
        entity.setProperty(DID_EMAIL_CONFIRM, didEmailConfirm);
    }
    
    public void setLang(String lang) {
        entity.setProperty(LANG, lang);
    }
    
    public static Key createKey(long id) {
        return KeyFactory.createKey(UserData.class.getSimpleName(), id);
    }
}
