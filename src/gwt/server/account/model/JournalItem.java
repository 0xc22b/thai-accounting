package gwt.server.account.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class JournalItem {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private JournalHeader journalHeader;
    
    @Persistent
    private Key accChartKey;
    
    @Persistent
    private double amt;

    @Persistent
    private Date createDate;

    public JournalItem(String accChartKeyString, double amt, Date createDate) {
        this.accChartKey = KeyFactory.stringToKey(accChartKeyString);
        this.amt = amt;
        this.createDate = createDate;
    }

    public Key getKey() {
        return key;
    }
    
    public String getKeyString() {
        return KeyFactory.keyToString(key);
    }
    
    public JournalHeader getJournalHeader() {
        return journalHeader;
    }

    public Key getAccChartKey() {
        return accChartKey;
    }
    
    public String getAccChartKeyString() {
        return KeyFactory.keyToString(accChartKey);
    }

    public double getAmt() {
        return amt;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }
    
    
}
