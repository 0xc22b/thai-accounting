package gwt.server.user.model;

import com.google.appengine.api.datastore.Entity;

public class UserGrp {
    
    public static final String USER_GRP_KEY_NAME = "userGrpKeyName";
    
    private Entity entity;
    
    public UserGrp() {
        entity = new Entity(UserGrp.class.getSimpleName(), USER_GRP_KEY_NAME);
    }
    
    public Entity getEntity() {
        return entity;
    }
}
