package gwt.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class SCom implements Serializable{

    private String keyString;
    private String name;
    private Date createDate;

    private ArrayList<SFiscalYear> sFisList = new ArrayList<SFiscalYear>();

    public SCom(){

    }

    public SCom(String keyString, String name, Date createDate) {
        this.keyString = keyString;
        this.name = name;
        this.createDate = createDate;
    }

    public String getKeyString(){
        return this.keyString;
    }

    public String getName(){
        return this.name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public List<SFiscalYear> getSFisList(){
        return sFisList;
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
        Iterator<SFiscalYear> it = sFisList.iterator();
        while (it.hasNext()) {
            SFiscalYear sFis = it.next();
            if (sFis.getKeyString().equals(fisKeyString)) {
                it.remove();
                break;
            }
        }
    }

    public void setKeyString(String keyString){
        this.keyString = keyString;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
