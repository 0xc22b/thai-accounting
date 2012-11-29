package gwt.server.account.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class DocType {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key fisKey;
    
    @Persistent
    private Key journalTypeKey;
    
    @Persistent
    private String code;
    
    @Persistent
    private String name;
    
    @Persistent
    private String journalDesc;
    
    @Persistent
    private Date createDate;

    public DocType(String fisKeyString, String journalTypeKeyString, String code, String name, String journalDesc, Date createDate) {
        this.fisKey = KeyFactory.stringToKey(fisKeyString);
        this.journalTypeKey = KeyFactory.stringToKey(journalTypeKeyString);
        this.code = code;
        this.name = name;
        this.journalDesc = journalDesc;
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

    public Key getJournalTypeKey() {
        return journalTypeKey;
    }
    
    public String getJournalTypeKeyString(){
        return KeyFactory.keyToString(journalTypeKey);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getJournalDesc() {
        return journalDesc;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setJournalTypeKey(String journalTypeKeyString) {
        this.journalTypeKey = KeyFactory.stringToKey(journalTypeKeyString);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJournalDesc(String journalDesc) {
        this.journalDesc = journalDesc;
    }

    
    
}
