package gwt.client.def;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.view.client.ProvidesKey;

public class DocTypeListDefImpl extends ListDef<SFiscalYear, SDocType> {

    private static final TConstants constants = TCF.get();
    private static DocTypeListDefImpl instance = null;

    public static DocTypeListDefImpl getInstance() {
        if (instance == null) {
            instance = new DocTypeListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SDocType> getList(SFiscalYear l) {
        return l.getSDocTypeList();
    }

    @Override
    public ProvidesKey<SDocType> getKeyProvider() {
        return new ProvidesKey<SDocType>() {
                    public Object getKey(SDocType sDocType) {
                        return sDocType == null ? null : sDocType.getKeyString();
                    }
        };
    }

    @Override
    public String getKeyString(SDocType e) {
        return e == null ? null : e.getKeyString();
    }

    @Override
    public int getColumnSize() {
        return 4;
    }


    @Override
    public String getColumnName(int i) {
        switch(i){
        case 0:
            return constants.code();
        case 1:
            return constants.name();
        case 2:
            return constants.journalType();    
        case 3:
            return constants.journalDesc();
        default:
            throw new AssertionError(i);
        }    
    }


    @Override
    public String getColumnValue(SDocType sDocType, int i) {
        switch(i){
        case 0:
            return sDocType.getCode();
        case 1:
            return sDocType.getName();
        case 2:
            return sDocType.getJournalTypeShortName();
        case 3:
            return sDocType.getJournalDesc();
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        return false;
    }

    @Override
    public String getFooterValue(List<SDocType> list, int i) {
        return null;
    }

    @Override
    public boolean hasSort(int i) {
        return false;
    }

    @Override
    public int getCompare(SDocType e1, SDocType e2, int i) {
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
     
}
