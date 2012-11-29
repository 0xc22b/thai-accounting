package gwt.server.account.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class FinHeader {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key fisKey;
    
    @Persistent
    private String name;
    
    @Persistent
    private Date createDate;
    
    @Persistent(mappedBy = "finHeader")
    @Element(dependent = "true")
    private HashSet<FinItem> itemSet = new HashSet<FinItem>();

    public FinHeader(String fisKeyString, String name, Date createDate) {
        this.fisKey = KeyFactory.stringToKey(fisKeyString);
        this.name = name;
        this.createDate = createDate;
    }

    public void addItem(FinItem item){
        itemSet.add(item);
    }
    
    public void clearItemSet(){
        itemSet.clear();
    }

    public Key getKey() {
        return key;
    }
    
    public String getKeyString() {
        return KeyFactory.keyToString(key);
    }

    public Key getFisKey() {
        return fisKey;
    }
    
    public String getFisKeyString() {
        return KeyFactory.keyToString(fisKey);
    }

    public String getName() {
        return name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Set<FinItem> getItemSet() {
        return Collections.unmodifiableSet(itemSet);
    }

    public void setName(String name) {
        this.name = name;
    }

}
