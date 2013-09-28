package gwt.shared.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SAccAmt implements Serializable{

	private String accChartKeyString;
	private double amt;
	private Date createDate;
	
	public SAccAmt(){
		
	}

    public SAccAmt(String accChartKeyString, double amt, Date createDate) {
        this.accChartKeyString = accChartKeyString;
        this.amt = amt;
        this.createDate = createDate;
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
}
