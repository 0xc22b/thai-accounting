package gwt.client.def;

import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SFiscalYear;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.view.client.ProvidesKey;

public class BeginListDefImpl extends ListDef<SFiscalYear, SAccChart> {

    private static final TConstants constants = TCF.get();
    private static BeginListDefImpl instance = null;

    public static BeginListDefImpl getInstance() {
        if (instance == null) {
            instance = new BeginListDefImpl();
        }
        return instance;
    }

    @Override
    public List<SAccChart> getList(SFiscalYear l) {
        ArrayList<SAccChart> beginList = new ArrayList<SAccChart>();
        for(SAccChart sAccChart : l.getSAccChartList()){
            if(sAccChart.getBeginning() != 0){
                beginList.add(sAccChart);
            }
        }
        return beginList;
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
            return constants.accNo();
        case 1:
            return constants.accName();
        case 2:
            return constants.debit();
        case 3:
            return constants.credit();
        default:
            throw new AssertionError(i);
        }    
    }


    @Override
    public String getColumnValue(SAccChart sAccChart, int i) {
        switch(i){
        case 0:
            return sAccChart.getNo();
        case 1:
            return sAccChart.getName();
        case 2:
            if(sAccChart.getBeginning() > 0){
                return NumberFormat.getFormat("#,##0.00").format(sAccChart.getBeginning());
            }else{
                return "";
            }
        case 3:
            if(sAccChart.getBeginning() < 0){
                return NumberFormat.getFormat("#,##0.00").format(Math.abs(sAccChart.getBeginning()));
            }else{
                return "";
            }
        default:
            throw new AssertionError(i);
        }  
    }

    @Override
    public boolean hasFooter(int i) {
        switch(i){
        case 2:
            return true;
        case 3:
            return true;
        default:
            return false;
        }
    }

    @Override
    public String getFooterValue(List<SAccChart> list, int i) {
        
        if(list.isEmpty()){
            return "";
        }
        
        double total = 0;
        switch(i){
        case 2:
            for(SAccChart sAccChart : list){
                if(sAccChart.getBeginning() > 0){
                    total += sAccChart.getBeginning();
                }
            }
            return NumberFormat.getFormat("#,##0.00").format(total);
        case 3:
            for(SAccChart sAccChart : list){
                if(sAccChart.getBeginning() < 0){
                    total += Math.abs(sAccChart.getBeginning());
                }
            }
            return NumberFormat.getFormat("#,##0.00").format(total);
        default:
            return "";
        }
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
