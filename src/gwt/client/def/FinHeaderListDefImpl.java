package gwt.client.def;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFiscalYear;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.view.client.ProvidesKey;

public class FinHeaderListDefImpl extends ListDef<SFiscalYear, SFinHeader> {

    private static final TConstants constants = TCF.get();
    private static FinHeaderListDefImpl instance = null;

    public static FinHeaderListDefImpl getInstance() {
        if (instance == null) {
            instance = new FinHeaderListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SFinHeader> getList(SFiscalYear l) {
        return l.getSFinHeaderList();
    }

    @Override
    public ProvidesKey<SFinHeader> getKeyProvider() {
        return new ProvidesKey<SFinHeader>() {
                    public Object getKey(SFinHeader sFin) {
                        return sFin == null ? null : sFin.getKeyString();
                    }
        };
    }

    @Override
    public String getKeyString(SFinHeader e) {
        return e == null ? null : e.getKeyString();
    }

    @Override
    public int getColumnSize() {
        return 1;
    }

    @Override
    public String getColumnName(int i) {
        switch(i){
        case 0:
            return constants.name();   
        default:
            throw new AssertionError(i);
        }    
    }


    @Override
    public String getColumnValue(SFinHeader sFin, int i) {
        switch(i){
        case 0:
            return sFin.getName();  
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        return false;
    }

    @Override
    public String getFooterValue(List<SFinHeader> list, int i) {
        return null;
    }

    @Override
    public boolean hasSort(int i) {
        return false;
    }

    @Override
    public int getCompare(SFinHeader e1, SFinHeader e2, int i) {
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
