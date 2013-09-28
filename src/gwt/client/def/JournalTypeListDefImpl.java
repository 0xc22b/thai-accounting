package gwt.client.def;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalType;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.view.client.ProvidesKey;

public class JournalTypeListDefImpl extends ListDef<SFiscalYear, SJournalType> {

    private static final TConstants constants = TCF.get();
    private static JournalTypeListDefImpl instance = null;

    public static JournalTypeListDefImpl getInstance() {
        if (instance == null) {
            instance = new JournalTypeListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SJournalType> getList(SFiscalYear l) {
        return l.getSJournalTypeList();
    }

    @Override
    public ProvidesKey<SJournalType> getKeyProvider() {
        return new ProvidesKey<SJournalType>() {
                    public Object getKey(SJournalType sJournalType) {
                        return sJournalType == null ? null : sJournalType.getKeyString();
                    }
        };
    }

    @Override
    public String getKeyString(SJournalType e) {
        return e == null ? null : e.getKeyString();
    }

    @Override
    public int getColumnSize() {
        return 2;
    }


    @Override
    public String getColumnName(int i) {
        switch(i){
        case 0:
            return constants.name();
        case 1:
            return constants.shortName();
        default:
            throw new AssertionError(i);
        }    
    }


    @Override
    public String getColumnValue(SJournalType sJournalType, int i) {
        switch(i){
        case 0:
            return sJournalType.getName();
        case 1:
            return sJournalType.getShortName();
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        return false;
    }

    @Override
    public String getFooterValue(List<SJournalType> list, int i) {
        return null;
    }

    @Override
    public boolean hasSort(int i) {
        return false;
    }

    @Override
    public int getCompare(SJournalType e1, SJournalType e2, int i) {
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
        return 100;
    }
}
