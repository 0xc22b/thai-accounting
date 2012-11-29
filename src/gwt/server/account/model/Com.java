package gwt.server.account.model;

import gwt.shared.model.SCom.ComType;
import gwt.shared.model.SCom.YearType;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Com {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key userKey;
    
    @Persistent
    private String name;
    
    @Persistent
    private String address;
    
    @Persistent
    private String telNo;
    
    @Persistent
    private ComType comType;
    
    @Persistent
    private String taxID;
    
    @Persistent
    private String merchantID;
    
    @Persistent
    private YearType yearType;
    
    @Persistent
    private double vatRate;
    
    @Persistent
    private Date createDate;
    
    public Com(Key userKey, String name, String address, String telNo, ComType comType, String taxID, String merchantID,
            YearType yearType, double vatRate, Date createDate) {
        
        if (userKey == null || name == null) {
            throw new IllegalArgumentException();
        }
        
        this.userKey = userKey;
        this.name = name;
        this.address = address;
        this.telNo = telNo;
        this.comType = comType;
        this.taxID = taxID;
        this.merchantID = merchantID;
        this.yearType = yearType;
        this.vatRate = vatRate;
        this.createDate = createDate;
    }

    public Key getKey(){
        return this.key;
    }
    
    public String getKeyString(){
        return KeyFactory.keyToString(this.key);
    }
    
    public Key getUserKey() {
        return userKey;
    }
    
    public String getUserKeyString(){
        return KeyFactory.keyToString(this.userKey);
    }

    public String getName(){
        return this.name;
    }
    
    public String getAddress() {
        return address;
    }

    public String getTelNo() {
        return telNo;
    }

    public ComType getComType() {
        return comType;
    }

    public String getTaxID() {
        return taxID;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public YearType getYearType() {
        return yearType;
    }

    public double getVatRate() {
        return vatRate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public void setComType(ComType comType) {
        this.comType = comType;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public void setYearType(YearType yearType) {
        this.yearType = yearType;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }
    
    
}
