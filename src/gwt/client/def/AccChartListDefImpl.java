package gwt.client.def;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.view.client.ProvidesKey;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SFiscalYear;

public class AccChartListDefImpl extends ListDef<SFiscalYear, SAccChart> {

    private static final TConstants constants = TCF.get();
    private static AccChartListDefImpl instance = null;

    public static AccChartListDefImpl getInstance() {
        if (instance == null) {
            instance = new AccChartListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SAccChart> getList(SFiscalYear l) {
        return l.getSAccChartList();
    }

    @Override
    public ProvidesKey<SAccChart> getKeyProvider() {
        return new ProvidesKey<SAccChart>() {
                    public Object getKey(SAccChart sAccChart) {
                        return sAccChart == null ? null : sAccChart.getKeyString();
                    }
        };
    }

    @Override
    public String getKeyString(SAccChart e) {
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
            return constants.accType();
        case 1:
            return constants.accNo();
        case 2:
            return constants.name();
        case 3:
            return constants.accGrp();
        default:
            throw new AssertionError(i);
        }    
    }

    @Override
    public String getColumnValue(SAccChart sAccChart, int i) {
        switch(i){
        case 0:
            if(sAccChart.getType().equals(SAccChart.AccType.CONTROL)){
                return constants.control();
            }else if(sAccChart.getType().equals(SAccChart.AccType.ENTRY)){
                return constants.entry();
            }else{
                throw new AssertionError(sAccChart.getType());
            }
        case 1:
            return sAccChart.getNo();
        case 2:
            int parentACLevel = sAccChart.getParentAccChartLevel();
            // Level starts from 1
            parentACLevel = parentACLevel == 0 ? 1 : parentACLevel;
            String s = "";
            for(int j = 1; j < parentACLevel; j++){
                s += "&nbsp;&nbsp;";
            }
            for(int j = parentACLevel; j < sAccChart.getLevel(); j++){
                s += "&bull;&bull;";
            }
            return s + sAccChart.getName();
        case 3:
            return sAccChart.getAccGroupName();
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        return false;
    }

    @Override
    public String getFooterValue(List<SAccChart> list, int i) {
        return null;
    }

    @Override
    public boolean hasSort(int i) {
        return false;
    }

    @Override
    public int getCompare(SAccChart e1, SAccChart e2, int i) {
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
            return 15;
        case 1:
            return 20;
        case 2:
            return 50;
        case 3:
            return 15;
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public Unit getWidthUnit(int i) {
        return Unit.PCT;
    }

    @Override
    public int getPageSize() {
        return 200;
    }
}
