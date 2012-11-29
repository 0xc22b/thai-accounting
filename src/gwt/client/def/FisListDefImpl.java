package gwt.client.def;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SCom;
import gwt.shared.model.SFiscalYear;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.view.client.ProvidesKey;

public class FisListDefImpl extends ListDef<SCom, SFiscalYear> {

    private static final TConstants constants = TCF.get();
    private static FisListDefImpl instance = null;

    public static FisListDefImpl getInstance() {
        if (instance == null) {
            instance = new FisListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SFiscalYear> getList(SCom l) {
        return l.getSFisList();
    }

    @Override
    public ProvidesKey<SFiscalYear> getKeyProvider() {
        return new ProvidesKey<SFiscalYear>() {
                    public Object getKey(SFiscalYear sFis) {
                        return sFis == null ? null : sFis.getKeyString();
                    }
        };
    }

    @Override
    public String getKeyString(SFiscalYear e) {
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
            return constants.fisList();
        default:
            throw new AssertionError(i);
        }    
    }


    @Override
    public String getColumnValue(SFiscalYear sFis, int i) {
        switch(i){
        case 0:
            return sFis.getBeginMonth() + "/" + sFis.getBeginYear() + " - " + sFis.getEndMonth() + "/" + sFis.getEndYear();
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        return false;
    }

    @Override
    public String getFooterValue(List<SFiscalYear> list, int i) {
        return null;
    }

    @Override
    public boolean hasSort(int i) {
        return false;
    }

    @Override
    public int getCompare(SFiscalYear e1, SFiscalYear e2, int i) {
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
