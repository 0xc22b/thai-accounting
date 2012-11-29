package gwt.shared.model;

import java.io.Serializable;
import java.util.Date;

public class SDocType implements Serializable{

	private static final long serialVersionUID = 1L;

	private String keyString;
	private String journalTypeKeyString;
	private String code;
	private String name;
    private String journalDesc;
    private Date createDate;
	
    private String journalTypeShortName;
    
	public SDocType(){
		
	}

    public SDocType(String keyString, String journalTypeKeyString, String code, String name, String journalDesc, Date createDate) {
        this.keyString = keyString;
        this.journalTypeKeyString = journalTypeKeyString;
        this.code = code;
        this.name = name;
        this.journalDesc = journalDesc;
        this.createDate = createDate;
    }

    public String getKeyString() {
        return keyString;
    }
    
    public String getJournalTypeKeyString(){
        return journalTypeKeyString;
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
    
    public String getJournalTypeShortName() {
        return journalTypeShortName;
    }
    
    public Date getCreateDate(){
        return createDate;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }
    
    public void setJournalTypeKeyString(String journalTypeKeyString){
        this.journalTypeKeyString = journalTypeKeyString;
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

    public void setJournalTypeShortName(String name) {
        this.journalTypeShortName = name;
    }
	
}
