package gwt.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SFiscalYear implements Serializable{

    private static final long serialVersionUID = 1L;

    private String keyString;
    private int beginMonth;
    private int beginYear;
    private int endMonth;
    private int endYear;
    private ArrayList<SJournalType> sJournalTypeList = new ArrayList<SJournalType>();
    private ArrayList<SDocType> sDocTypeList = new ArrayList<SDocType>();
    private ArrayList<SAccGrp> sAccGrpList = new ArrayList<SAccGrp>();
    private ArrayList<SAccChart> sAccChartList = new ArrayList<SAccChart>();

    public SFiscalYear(){

    }

    public SFiscalYear(String keyString, int beginMonth, int beginYear, int endMonth, int endYear){
        this.keyString = keyString;
        this.beginMonth = beginMonth;
        this.beginYear = beginYear;
        this.endMonth = endMonth;
        this.endYear = endYear;
    }

    public String getKeyString(){
        return this.keyString;
    }

    public int getBeginMonth(){
        return this.beginMonth;
    }

    public int getEndMonth(){
        return this.endMonth;
    }

    public int getBeginYear(){
        return this.beginYear;
    }

    public int getEndYear(){
        return this.endYear;
    }

    public List<SJournalType> getSJournalTypeList(){
        return sJournalTypeList;
    }

    public List<SDocType> getSDocTypeList(){
        return sDocTypeList;
    }

    public List<SAccGrp> getSAccGrpList(){
        return sAccGrpList;
    }

    public List<SAccChart> getSAccChartList(){
        return sAccChartList;
    }

    public void setKeyString(String keyString){
        this.keyString = keyString;
    }

    public void setBeginMonth(int beginMonth) {
        this.beginMonth = beginMonth;
    }

    public void setBeginYear(int beginYear) {
        this.beginYear = beginYear;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public void setSetup(SFiscalYear sFis){
        sJournalTypeList.clear();
        for(SJournalType sJournalType : sFis.getSJournalTypeList()){
            addSJournalType(sJournalType);
        }

        sDocTypeList.clear();
        for(SDocType sDocType : sFis.getSDocTypeList()){
            addSDocType(sDocType);
        }

        sAccGrpList.clear();
        for(SAccGrp sAccGrp : sFis.getSAccGrpList()){
            addSAccGrp(sAccGrp);
        }

        sAccChartList.clear();
        for(SAccChart sAccChart : sFis.getSAccChartList()){
            addSAccChart(sAccChart);
        }
    }

    public SJournalType getSJournalType(String journalTypeKeyString){
        for(SJournalType sJournalType : sJournalTypeList){
            if(sJournalType.getKeyString().equals(journalTypeKeyString)){
                return sJournalType;
            }
        }
        return null;
    }

    public void addSJournalType(SJournalType sJournalType){
        sJournalTypeList.add(sJournalType);
    }

    public SJournalType editSJournalType(SJournalType newSJournalType){
        for(SJournalType sJournalType : sJournalTypeList){
            if(sJournalType.getKeyString().equals(newSJournalType.getKeyString())){
                sJournalType.setName(newSJournalType.getName());
                sJournalType.setShortName(newSJournalType.getShortName());
                return sJournalType;
            }
        }
        return null;
    }

    public void removeSJournalType(String keyString){
        Iterator<SJournalType> it = sJournalTypeList.iterator();
        while (it.hasNext()) {
            SJournalType sJournalType = it.next();
            if (sJournalType.getKeyString().equals(keyString)) {
                it.remove();
                break;
            }
        }
    }

    public SDocType getSDocType(String docTypeKeyString){
        for(SDocType sDocType : sDocTypeList){
            if(sDocType.getKeyString().equals(docTypeKeyString)){
                return sDocType;
            }
        }
        return null;
    }

    public void addSDocType(SDocType sDocType){
        sDocTypeList.add(sDocType);
    }

    public SDocType editSDocType(SDocType newSDocType){
        for(SDocType sDocType : sDocTypeList){
            if(sDocType.getKeyString().equals(newSDocType.getKeyString())){
                sDocType.setJournalTypeKeyString(newSDocType.getJournalTypeKeyString());
                sDocType.setCode(newSDocType.getCode());
                sDocType.setName(newSDocType.getName());
                sDocType.setJournalDesc(newSDocType.getJournalDesc());
                return sDocType;
            }
        }
        return null;
    }

    public void removeSDocType(String keyString){
        Iterator<SDocType> it = sDocTypeList.iterator();
        while (it.hasNext()) {
            SDocType sDocType = it.next();
            if (sDocType.getKeyString().equals(keyString)) {
                it.remove();
                break;
            }
        }
    }

    public void updateSDocTypeJournalTypeShortName(){
        for (SDocType sDocType : sDocTypeList) {
            updateSDocTypeJournalTypeShortName(sDocType);
        }
    }

    public void updateSDocTypeJournalTypeShortName(SDocType sDocType){

        assert sDocType != null;
        assert sJournalTypeList != null && !sJournalTypeList.isEmpty();

        // Try to update journalTypeShortDesc
        SJournalType sJournalType = getSJournalType(sDocType.getJournalTypeKeyString());
        if(sJournalType != null){
            sDocType.setJournalTypeShortName(sJournalType.getShortName());
        }
    }

    public SAccGrp getSAccGrp(String accGrpKeyString){
        for(SAccGrp sAccGrp : sAccGrpList){
            if(sAccGrp.getKeyString().equals(accGrpKeyString)){
                return sAccGrp;
            }
        }
        return null;
    }

    public void addSAccGrp(SAccGrp sAccGrp){
        sAccGrpList.add(sAccGrp);
    }

    public SAccGrp editSAccGrp(SAccGrp newSAccGrp){
        for(SAccGrp sAccGrp : sAccGrpList){
            if(sAccGrp.getKeyString().equals(newSAccGrp.getKeyString())){
                sAccGrp.setName(newSAccGrp.getName());
                return sAccGrp;
            }
        }
        return null;
    }

    public void removeSAccGrp(String keyString){
        Iterator<SAccGrp> it = sAccGrpList.iterator();
        while (it.hasNext()) {
            SAccGrp sAccGrp = it.next();
            if (sAccGrp.getKeyString().equals(keyString)) {
                it.remove();
                break;
            }
        }
    }

    public SAccChart getSAccChart(String accChartKeyString){
        for(SAccChart sAccChart : sAccChartList){
            if(sAccChart.getKeyString().equals(accChartKeyString)){
                return sAccChart;
            }
        }
        return null;
    }

    public void addSAccChart(SAccChart sAccChart){
        sAccChartList.add(sAccChart);
    }

    public SAccChart editSAccChart(SAccChart newSAccChart){
        for(SAccChart sAccChart : sAccChartList){
            if(sAccChart.getKeyString().equals(newSAccChart.getKeyString())){
                sAccChart.setAccGroupKeyString(newSAccChart.getAccGroupKeyString());
                sAccChart.setParentAccChartKeyString(
                        newSAccChart.getParentAccChartKeyString());
                sAccChart.setNo(newSAccChart.getNo());
                sAccChart.setName(newSAccChart.getName());
                sAccChart.setType(newSAccChart.getType());
                sAccChart.setLevel(newSAccChart.getLevel());
                return sAccChart;
            }
        }
        return null;
    }

    public void removeSAccChart(String keyString){
        Iterator<SAccChart> it = sAccChartList.iterator();
        while (it.hasNext()) {
            SAccChart sAccChart = it.next();
            if (sAccChart.getKeyString().equals(keyString)) {
                it.remove();
                break;
            }
        }
    }

    public void updateSAccChartAccGrpName(){
        for(SAccChart sAccChart : sAccChartList){
            updateSAccChartAccGrpName(sAccChart);
        }
    }

    public void updateSAccChartAccGrpName(SAccChart sAccChart){

        assert sAccChart != null;
        assert sAccGrpList != null && !sAccGrpList.isEmpty();

        // Try to update accGrpName
        SAccGrp sAccGrp = getSAccGrp(sAccChart.getAccGroupKeyString());
        if(sAccGrp != null){
            sAccChart.setAccGroupName(sAccGrp.getName());
        }
    }

    public void updateSAccChartParentAccChartNo(){
        for(SAccChart sAccChart : sAccChartList){
            updateSAccChartParentAccChartNo(sAccChart);
        }
    }

    public void updateSAccChartParentAccChartNo(SAccChart sAccChart){
        assert sAccChart != null;

        // Try to update parentAccChartNo
        if(sAccChart.getParentAccChartKeyString() != null){
            SAccChart parentSAccChart = getSAccChart(
                    sAccChart.getParentAccChartKeyString());
            sAccChart.setParentAccChartNo(parentSAccChart.getNo());
            sAccChart.setParentAccChartLevel(parentSAccChart.getLevel());
        }
    }

    public void sortSAccChartList(){
        ArrayList<SAccChart> sortedList = new ArrayList<SAccChart>();
        for(SAccGrp sAccGrp : sAccGrpList){
            genSortedSAccChartList(sortedList, sAccGrp.getKeyString(), null);
        }
        sAccChartList = sortedList;
    }

    private void genSortedSAccChartList(ArrayList<SAccChart> sortedList,
            String accGrpKeyString, String parentKeyString){
        ArrayList<SAccChart> sameParentList = getSAccChartList(accGrpKeyString, parentKeyString);
        for(SAccChart each : sameParentList){
            sortedList.add(each);
            genSortedSAccChartList(sortedList, accGrpKeyString, each.getKeyString());
        }
    }

    private ArrayList<SAccChart> getSAccChartList(String accGrpKeyString,
            String parentKeyString){
        ArrayList<SAccChart> sortedList = new ArrayList<SAccChart>();
        boolean didPut;
        for(SAccChart sAccChart : sAccChartList){
            if(sAccChart.getAccGroupKeyString().equals(accGrpKeyString)){
                if((sAccChart.getParentAccChartKeyString() == null &&
                        sAccChart.getParentAccChartKeyString() == parentKeyString) ||
                        (sAccChart.getParentAccChartKeyString() !=null &&
                        sAccChart.getParentAccChartKeyString().equals(parentKeyString))){
                    didPut = false;
                    for(int i=0; i<sortedList.size(); i++){
                        if(sAccChart.getNo().compareTo(sortedList.get(i).getNo()) < 0){
                            sortedList.add(i, sAccChart);
                            didPut = true;
                            break;
                        }
                    }
                    if(!didPut){
                        sortedList.add(sAccChart);
                    }
                }
            }
        }
        return sortedList;
    }
}
