package gwt.shared.model;

import java.io.Serializable;
import java.util.Date;

public class SJournalType implements Serializable{

	private static final long serialVersionUID = 1L;

	private String keyString;
    private String name;
    private String shortName;
	private Date createDate;
	
	public SJournalType(){
		
	}

    public SJournalType(String keyString, String name, String shortName, Date createDate) {
        this.keyString = keyString;
        this.name = name;
        this.shortName = shortName;
        this.createDate = createDate;
    }

    public String getKeyString() {
        return keyString;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
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

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
	
}
