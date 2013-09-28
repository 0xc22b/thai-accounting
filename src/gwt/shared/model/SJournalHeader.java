package gwt.shared.model;

import gwt.shared.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SJournalHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	private String keyString;
    private String docTypeKeyString;
    private String no;
    private int day;
    private int month;
    private int year;
    private String desc;
    
    private ArrayList<SJournalItem> itemList = new ArrayList<SJournalItem>();
    
	public SJournalHeader(){
		
	}

    public SJournalHeader(String keyString, String docTypeKeyString, String no, int day, int month, int year, String desc) {
        this.keyString = keyString;
        this.docTypeKeyString = docTypeKeyString;
        this.no = no;
        this.day = day;
        this.month = month;
        this.year = year;
        this.desc = desc;
    }
    
    public void addItem(SJournalItem newSJournalItem) {
        // Sorting by inserting a new item into the correct order,
        // better in performance.
        boolean isPut = false;
        for (int i = 0; i < itemList.size(); i++) {
            SJournalItem sJournalItem = itemList.get(i);
            if (sJournalItem.getCreateDate().after(newSJournalItem.getCreateDate())) {
                itemList.add(i, newSJournalItem);
                isPut = true;
                break;
            }
        }
        if (!isPut) {
            itemList.add(newSJournalItem);
        }
    }

    public String getKeyString() {
        return keyString;
    }

    public String getDocTypeKeyString() {
        return docTypeKeyString;
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

    public List<SJournalItem> getItemList() {
        return Collections.unmodifiableList(itemList);
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    public void setDocTypeKeyString(String docTypeKeyString) {
        this.docTypeKeyString = docTypeKeyString;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public int compareDate(int day, int month, int year){
        return Utils.compareDate(this.day, this.month, this.year, day, month, year);
    }
}
