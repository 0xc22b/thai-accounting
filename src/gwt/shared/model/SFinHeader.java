package gwt.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SFinHeader implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String keyString;
    private String name;
    private Date createDate;
    
    private ArrayList<SFinItem> sFinItemList = new ArrayList<SFinItem>();

    public SFinHeader(){
        
    }
    
    public SFinHeader(String keyString, String name, Date createDate){
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
    
    public List<SFinItem> getSFinItemList() {
        return Collections.unmodifiableList(sFinItemList);
    }
    
    public SFinItem getSFinItem(String finItemKeyString){
        for(SFinItem sFinItem: sFinItemList){
            if(sFinItem.getKeyString().equals(finItemKeyString)){
                return sFinItem;
            }
        }
        return null;
    }
    
    public void addSFinItem(SFinItem newSFinItem) {
        // Sorting by inserting a new item into the correct order,
        // better in performance.
        boolean isPut = false;
        for (int i = 0; i < sFinItemList.size(); i++) {
            SFinItem sFinItem = sFinItemList.get(i);
            if (sFinItem.getSeq() > newSFinItem.getSeq()) {
                sFinItemList.add(i, newSFinItem);
                isPut = true;
                break;
            }
        }
        if (!isPut) {
            sFinItemList.add(newSFinItem);
        }
    }

    public SFinItem editSFinItem(SFinItem newSFinItem) {
        boolean isFound = false;
        for(SFinItem sFinItem : sFinItemList){
            if(sFinItem.getKeyString().equals(newSFinItem.getKeyString())){
                sFinItemList.remove(sFinItem);
                isFound = true;
                break;
            }
        }    
        if (isFound) {
            addSFinItem(newSFinItem);
            return newSFinItem;
        }
        return null;
    }
    
    public void removeSFinItem(String keyString){
        for(SFinItem sFinItem : sFinItemList){
            if(sFinItem.getKeyString().equals(keyString)){
                sFinItemList.remove(sFinItem);
                break;
            }
        }
    }
}
