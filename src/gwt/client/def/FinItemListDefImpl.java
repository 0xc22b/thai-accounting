package gwt.client.def;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SFinItem.Comm;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.view.client.ProvidesKey;

public class FinItemListDefImpl extends ListDef<SFinHeader, SFinItem> {

    private static final TConstants constants = TCF.get();
    private static FinItemListDefImpl instance = null;

    public static FinItemListDefImpl getInstance() {
        if (instance == null) {
            instance = new FinItemListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SFinItem> getList(SFinHeader l) {
        return l.getSFinItemList();
    }

    @Override
    public ProvidesKey<SFinItem> getKeyProvider() {
        return new ProvidesKey<SFinItem>() {
            public Object getKey(SFinItem sFinItem) {
                return sFinItem == null ? null : sFinItem.getKeyString();
            }
        };
    }

    @Override
    public String getKeyString(SFinItem e) {
        return e == null ? null : e.getKeyString();
    }

    @Override
    public int getColumnSize() {
        return 10;
    }

    @Override
    public String getColumnName(int i) {
        switch(i){
        case 0:
            return constants.seq();
        case 1:
            return constants.comm();
        case 2:
            return constants.arg();
        case 3:
            return constants.calCon();
        case 4:
            return constants.printCon();
        case 5:
            return constants.printStyle();
        case 6:
            return constants.var1();
        case 7:
            return constants.var2();
        case 8:
            return constants.var3();
        case 9:
            return constants.var4();
        default:
            throw new AssertionError(i);
        }    
    }

    @Override
    public String getColumnValue(SFinItem sFinItem, int i) {
        switch(i){
        case 0:
            return sFinItem.getSeq() + "";
        case 1:
            return sFinItem.getComm().toString();
        case 2:
            return sFinItem.getComm().equals(Comm.ACCNO) ?
                    sFinItem.getAccChartNo() : sFinItem.getArg();
        case 3:
            return sFinItem.getCalCon().toString();
        case 4:
            return sFinItem.getPrintCon().toString();
        case 5:
            return sFinItem.getPrintStyle().toString();
        case 6:
            return sFinItem.getVar1().toString();
        case 7:
            return sFinItem.getVar2().toString();
        case 8:
            return sFinItem.getVar3().toString();
        case 9:
            return sFinItem.getVar4().toString();
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        return false;
    }

    @Override
    public String getFooterValue(List<SFinItem> list, int i) {
        return null;
    }

    @Override
    public boolean hasSort(int i) {
        return false;
    }

    @Override
    public int getCompare(SFinItem e1, SFinItem e2, int i) {
        return 0;
    }
    
    @Override
    public boolean hasWidth(int i) {
        return true;
    }

    @Override
    public double getWidthValue(int i) {
        switch(i){
        case 0:
            return 5;
        case 1:
            return 10;
        case 2:
            return 18;
        case 3:
            return 13;
        case 4:
            return 14;
        case 5:
            return 10;
        case 6:
            return 7;
        case 7:
            return 7;
        case 8:
            return 7;
        case 9:
            return 9;
        default:
            throw new AssertionError(i);
        }    
    }

    @Override
    public Unit getWidthUnit(int i) {
        return Unit.PCT;
    }
}
