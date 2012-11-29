package gwt.shared.model;

import java.io.Serializable;
import java.util.Date;

public class SAccGrp implements Serializable{

	private static final long serialVersionUID = 1L;

	private String keyString;
	private String name;
	private Date createDate;
	
	public SAccGrp(){
		
	}

    public SAccGrp(String keyString, String name, Date createDate) {
        this.keyString = keyString;
        this.name = name;
        this.createDate = createDate;
    }

    public String getKeyString() {
        return keyString;
    }

    public String getName() {
        return name;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    public void setName(String name) {
        this.name = name;
    }
	
	
}
