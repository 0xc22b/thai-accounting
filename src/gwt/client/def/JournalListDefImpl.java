package gwt.client.def;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.view.client.ProvidesKey;

public class JournalListDefImpl extends ListDef<List<SJournalHeader>, SJournalHeader> {

    private static final TConstants constants = TCF.get();
    private static JournalListDefImpl instance = null;

    public static JournalListDefImpl getInstance() {
        if (instance == null) {
            instance = new JournalListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SJournalHeader> getList(List<SJournalHeader> l) {
        return l;
    }

    @Override
    public ProvidesKey<SJournalHeader> getKeyProvider() {
        return new ProvidesKey<SJournalHeader>() {
                    public Object getKey(SJournalHeader sJournal) {
                        return sJournal == null ? null : sJournal.getKeyString();
                    }
        };
    }

    @Override
    public String getKeyString(SJournalHeader e) {
        return e == null ? null : e.getKeyString();
    }

    @Override
    public int getColumnSize() {
        return 5;
    }


    @Override
    public String getColumnName(int i) {
        switch(i){
        case 0:
            return constants.date();
        case 1:
            return constants.journalNo();
        case 2:
            return constants.desc();
        case 3:
            return constants.debit();
        case 4:
            return constants.credit();    
        default:
            throw new AssertionError(i);
        }    
    }


    @Override
    public String getColumnValue(SJournalHeader sJournal, int i) {
        
        double total = 0.0;
        
        switch(i){
        case 0:
            return sJournal.getDay() + "/" + sJournal.getMonth() + "/" + sJournal.getYear();
        case 1:
            return sJournal.getNo();
        case 2:
            return sJournal.getDesc();
        case 3:
            for(SJournalItem sJournalItem : sJournal.getItemList()){
                if(sJournalItem.getAmt() > 0){
                    total += sJournalItem.getAmt(); 
                }
            }
            return NumberFormat.getFormat("#,##0.00").format(total);
        case 4:
            for(SJournalItem sJournalItem : sJournal.getItemList()){
                if(sJournalItem.getAmt() < 0){
                    total += sJournalItem.getAmt(); 
                }
            }
            return NumberFormat.getFormat("#,##0.00").format(Math.abs(total));   
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        return false;
    }

    @Override
    public String getFooterValue(List<SJournalHeader> list, int i) {
        return null;
    }

    @Override
    public boolean hasSort(int i) {
        return false;
    }

    @Override
    public int getCompare(SJournalHeader e1, SJournalHeader e2, int i) {
        return 0;
    }
    
    @Override
    public boolean hasWidth(int i) {
        return false;
    }

    @Override
    public double getWidthValue(int i) {
        return 0;
    }

    @Override
    public Unit getWidthUnit(int i) {
        return null;
    }
    
    @Override
    public int getPageSize() {
        return 500;
    }
}
