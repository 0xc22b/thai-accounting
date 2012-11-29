package gwt.server.account.model;

import gwt.shared.Utils;

import java.util.Collections;
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
public class JournalHeader {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key fisKey;
    
    @Persistent
    private Key docTypeKey;
    
    @Persistent
    private String no;
    
    @Persistent
    private int day;
    
    @Persistent
    private int month;
    
    @Persistent
    private int year;
    
    @Persistent
    private String desc;
    
    @Persistent(mappedBy = "journalHeader")
    @Element(dependent = "true")
    private HashSet<JournalItem> itemSet = new HashSet<JournalItem>();

    public JournalHeader(String fisKeyString, String docTypeKeyString, String no, int day, int month, int year, String desc) {
        this.fisKey = KeyFactory.stringToKey(fisKeyString);
        this.docTypeKey = KeyFactory.stringToKey(docTypeKeyString);
        this.no = no;
        this.day = day;
        this.month = month;
        this.year = year;
        this.desc = desc;
    }

    public void addItem(JournalItem item){
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

    public Key getDocTypeKey() {
        return docTypeKey;
    }
    
    public String getDocTypeKeyString() {
        return KeyFactory.keyToString(docTypeKey);
    }

    public String getNo() {
        return no;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }
    
    public int getYear() {
        return year;
    }
    
    public String getDesc() {
        return desc;
    }

    public Set<JournalItem> getItemSet() {
        return Collections.unmodifiableSet(itemSet);
    }

    public void setDocTypeKey(String docTypeKeyString) {
        this.docTypeKey = KeyFactory.stringToKey(docTypeKeyString);
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int compareDate(int day, int month, int year){
        return Utils.compareDate(this.day, this.month, this.year, day, month, year);
    }
    
    
}
