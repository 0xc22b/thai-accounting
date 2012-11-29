package gwt.server.account.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class FiscalYear {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key comKey;
    
    @Persistent
    private int beginMonth;
    
    @Persistent
    private int beginYear;
    
    @Persistent
    private int endMonth;
    
    @Persistent
    private int endYear;
    
    public FiscalYear(String comKeyString, int beginMonth, int beginYear, int endMonth, int endYear){
        this.comKey = KeyFactory.stringToKey(comKeyString);
        this.beginMonth = beginMonth;
        this.beginYear = beginYear;
        this.endMonth = endMonth;
        this.endYear = endYear;
    }
    
    public Key getKey(){
        return this.key;
    }
    
    public String getKeyString(){
        return KeyFactory.keyToString(this.key);
    }
    
    public Key getComKey(){
        return this.comKey;
    }
    
    public int getBeginMonth(){
        return this.beginMonth;
    }
    
    public int getEndMonth(){
        return this.endMonth;
    }
    
    public int getBeginYear(){
        return this.beginYear;
    }
    
    public int getEndYear(){
        return this.endYear;
    }

    public void setBeginMonth(int beginMonth) {
        this.beginMonth = beginMonth;
    }

    public void setBeginYear(int beginYear) {
        this.beginYear = beginYear;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
    
    
}
