package gwt.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SCom implements Serializable{

    public enum ComType {
        PERSONAL,
        LIMITED,
        GOVERN
    }
    
    public enum YearType {
        INTER,
        THAI
    }
    
	private static final long serialVersionUID = 1L;

	private String keyString;
	private String name;
	private String address;
    private String telNo;
    private ComType comType;
    private String taxID;
    private String merchantID;
    private YearType yearType;
    private double vatRate;
    private Date createDate;
	
    private ArrayList<SFiscalYear> sFisList = new ArrayList<SFiscalYear>();
    
	public SCom(){
		
	}
	
	public SCom(String keyString, String name, String address, String telNo, ComType comType, String taxID,
            String merchantID, YearType yearType, double vatRate, Date createDate) {
        this.keyString = keyString;
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

    public String getKeyString(){
	    return this.keyString;
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
	
	public List<SFiscalYear> getSFisList(){
        return Collections.unmodifiableList(sFisList);
    }
	
	public SFiscalYear getSFis(String fisKeyString){
	    for(SFiscalYear sFis : sFisList){
            if(sFis.getKeyString().equals(fisKeyString)){
                return sFis;
            }
        }
        return null;
	}
    
    public void addSFis(SFiscalYear sFis){
        sFisList.add(sFis);
    }
    
    public SFiscalYear editSFis(SFiscalYear newSFis){
        for(SFiscalYear sFis : sFisList){
            if(sFis.getKeyString().equals(newSFis.getKeyString())){
                sFis.setBeginMonth(newSFis.getBeginMonth());
                sFis.setBeginYear(newSFis.getBeginYear());
                sFis.setEndMonth(newSFis.getEndMonth());
                sFis.setEndYear(newSFis.getEndYear());
                return sFis;
            }
        }
        return null;
    }
    
    public void removeSFis(String fisKeyString){
        for(SFiscalYear sFis : sFisList){
            if(sFis.getKeyString().equals(fisKeyString)){
                sFisList.remove(sFis);
                return;
            }
        }    
    }
    
    public void setKeyString(String keyString){
        this.keyString = keyString;
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

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    
}
