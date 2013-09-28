package gwt.client.def;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SFiscalYear;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.view.client.ProvidesKey;

public class AccGrpListDefImpl extends ListDef<SFiscalYear, SAccGrp> {

    private static final TConstants constants = TCF.get();
    private static AccGrpListDefImpl instance = null;

    public static AccGrpListDefImpl getInstance() {
        if (instance == null) {
            instance = new AccGrpListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SAccGrp> getList(SFiscalYear l) {
        return l.getSAccGrpList();
    }

    @Override
    public ProvidesKey<SAccGrp> getKeyProvider() {
        return new ProvidesKey<SAccGrp>() {
                    public Object getKey(SAccGrp sAccGrp) {
                        return sAccGrp == null ? null : sAccGrp.getKeyString();
                    }
        };
    }

    @Override
    public String getKeyString(SAccGrp e) {
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
    public String getColumnValue(SAccGrp sAccGrp, int i) {
        switch(i){
        case 0:
            return sAccGrp.getName();
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        return false;
    }

    @Override
    public String getFooterValue(List<SAccGrp> list, int i) {
        return null;
    }

    @Override
    public boolean hasSort(int i) {
        return false;
    }

    @Override
    public int getCompare(SAccGrp e1, SAccGrp e2, int i) {
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
