package gwt.server.account.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class JournalType {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key fisKey;
    
    @Persistent
    private String name;
    
    @Persistent
    private String shortName;
    
    @Persistent
    private Date createDate;

    public JournalType(String fisKeyString, String name, String shortName, Date createDate) {
        this.fisKey = KeyFactory.stringToKey(fisKeyString);
        this.name = name;
        this.shortName = shortName;
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
    
    public String getFisKeyString(){
        return KeyFactory.keyToString(fisKey);
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    
    
}
