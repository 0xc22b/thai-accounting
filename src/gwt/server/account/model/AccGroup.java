package gwt.server.account.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class AccGroup {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key fisKey;
    
    @Persistent
    private String name;
    
    @Persistent
    private Date createDate;

    public AccGroup(String fisKeyString, String name, Date createDate) {
        this.fisKey = KeyFactory.stringToKey(fisKeyString);
        this.name = name;
        this.createDate = createDate;
    }

    public Key getKey() {
        return key;
    }
    
    public String getKeyString(){
        return KeyFactory.keyToString(key);
    }

    public Key getFisKey() {
        return fisKey;
    }
    
    public Date getCreateDate(){
        return createDate;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
}
