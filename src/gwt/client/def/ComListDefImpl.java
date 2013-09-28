package gwt.client.def;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SCom;
import gwt.shared.model.SComList;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.view.client.ProvidesKey;

public class ComListDefImpl extends ListDef<SComList, SCom> {

    private static final TConstants constants = TCF.get();
    
    private static ComListDefImpl instance = null;

    public static ComListDefImpl getInstance() {
        if (instance == null) {
            instance = new ComListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SCom> getList(SComList l) {
        return l.getSComList();
    }

    @Override
    public ProvidesKey<SCom> getKeyProvider() {
        return new ProvidesKey<SCom>() {
                    public Object getKey(SCom sCom) {
                        return sCom == null ? null : sCom.getKeyString();
                    }
        };
    }

    @Override
    public String getKeyString(SCom e) {
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
    public String getColumnValue(SCom sCom, int i) {
        switch(i){
        case 0:
            return sCom.getName();
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        return false;
    }

    @Override
    public String getFooterValue(List<SCom> list, int i) {
        return null;
    }

    @Override
    public boolean hasSort(int i) {
        return false;
    }

    @Override
    public int getCompare(SCom e1, SCom e2, int i) {
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
