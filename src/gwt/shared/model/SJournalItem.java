package gwt.shared.model;

import java.io.Serializable;
import java.util.Date;

public class SJournalItem implements Serializable{

	private static final long serialVersionUID = 1L;

	private String keyString;
    private String accChartKeyString;
    private double amt;
    private Date createDate;

    // These are extra fields used by a ledger populated by getJournalListWithAC in RpcServiceImpl.java
    public int day;
    public int month;
    public int year;
    public String journalTypeShortName;
    public String journalNo;
    public String journalDesc;
    
	public SJournalItem(){
		
	}

    public SJournalItem(String keyString, String accChartKeyString, double amt, Date createDate) {
        this.keyString = keyString;
        this.accChartKeyString = accChartKeyString;
        this.amt = amt;
        this.createDate = createDate;
    }

    public String getKeyString() {
        return keyString;
    }

    public String getAccChartKeyString() {
        return accChartKeyString;
    }

    public double getAmt() {
        return amt;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }    
}
