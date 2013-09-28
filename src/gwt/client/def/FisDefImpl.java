package gwt.client.def;

import gwt.shared.model.SAccChart;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SAccChart.AccType;

public class FisDefImpl extends FisDef<SFiscalYear> {

    private static FisDefImpl instance = null;

    public static FisDefImpl getInstance() {
        if (instance == null) {
            instance = new FisDefImpl();
        }
        return instance;
    }

    @Override
    public String getFKeyString(SFiscalYear t) {
        return t.getKeyString();
    }

    @Override
    public int getFBeginMonth(SFiscalYear t) {
        return t.getBeginMonth();
    }

    @Override
    public int getFBeginYear(SFiscalYear t) {
        return t.getBeginYear();
    }

    @Override
    public int getFEndMonth(SFiscalYear t) {
        return t.getEndMonth();
    }

    @Override
    public int getFEndYear(SFiscalYear t) {
        return t.getEndYear();
    }

    @Override
    public String getJTName(SFiscalYear t, String keyString) {
        return t.getSJournalType(keyString).getName();
    }

    @Override
    public String getJTShortName(SFiscalYear t, String keyString) {
        return t.getSJournalType(keyString).getShortName();
    }
    
    @Override
    public int getJTListSize(SFiscalYear t) {
        return t.getSJournalTypeList().size();
    }

    @Override
    public String getJTKeyString(SFiscalYear t, int i) {
        return t.getSJournalTypeList().get(i).getKeyString();
    }

    @Override
    public String getJTShortName(SFiscalYear t, int i) {
        return t.getSJournalTypeList().get(i).getShortName();
    }
    
    @Override
    public String getDTJTKeyString(SFiscalYear t, String keyString) {
        return t.getSDocType(keyString).getJournalTypeKeyString();
    }

    @Override
    public String getDTCode(SFiscalYear t, String keyString) {
        return t.getSDocType(keyString).getCode();
    }

    @Override
    public String getDTName(SFiscalYear t, String keyString) {
        return t.getSDocType(keyString).getName();
    }

    @Override
    public String getDTJournalDesc(SFiscalYear t, String keyString) {
        return t.getSDocType(keyString).getJournalDesc();
    }
    
    @Override
    public int getDTListSize(SFiscalYear t) {
        return t.getSDocTypeList().size();
    }

    @Override
    public String getDTKeyString(SFiscalYear t, int i) {
        return t.getSDocTypeList().get(i).getKeyString();
    }
    
    @Override
    public String getDTJTKeyString(SFiscalYear t, int i) {
        return t.getSDocTypeList().get(i).getJournalTypeKeyString();
    }

    @Override
    public String getDTCode(SFiscalYear t, int i) {
        return t.getSDocTypeList().get(i).getCode();
    }
    
    @Override
    public String getAGName(SFiscalYear t, String keyString) {
        return t.getSAccGrp(keyString).getName();
    }
    
    @Override
    public int getAGListSize(SFiscalYear t) {
        return t.getSAccGrpList().size();
    }
    
    @Override
    public String getAGKeyString(SFiscalYear t, int i) {
        return t.getSAccGrpList().get(i).getKeyString();
    }

    @Override
    public String getAGName(SFiscalYear t, int i) {
        return t.getSAccGrpList().get(i).getName();
    }
    
    @Override
    public String getACAGKeyString(SFiscalYear t, String keyString) {
        return t.getSAccChart(keyString).getAccGroupKeyString();
    }
    
    @Override
    public String getACParentACKeyString(SFiscalYear t, String keyString) {
        return t.getSAccChart(keyString).getParentAccChartKeyString();
    }

    @Override
    public String getACNo(SFiscalYear t, String keyString) {
        return t.getSAccChart(keyString).getNo();
    }

    @Override
    public String getACName(SFiscalYear t, String keyString) {
        return t.getSAccChart(keyString).getName();
    }

    @Override
    public AccType getACType(SFiscalYear t, String keyString) {
        return t.getSAccChart(keyString).getType();
    }

    @Override
    public int getACLevel(SFiscalYear t, String keyString) {
        return t.getSAccChart(keyString).getLevel();
    }

    @Override
    public double getACBeginning(SFiscalYear t, String keyString) {
        return t.getSAccChart(keyString).getBeginning();
    }
    
    @Override
    public String getACAGName(SFiscalYear t, String keyString) {
        return t.getSAccChart(keyString).getAccGroupName();
    }
    
    @Override
    public String getACParentACNo(SFiscalYear t, String keyString) {
        return t.getSAccChart(keyString).getParentAccChartNo();
    }
    
    @Override
    public int getACListSize(SFiscalYear t) {
        return t.getSAccChartList().size();
    }
    
    @Override
    public String getACKeyString(SFiscalYear t, int i) {
        return t.getSAccChartList().get(i).getKeyString();
    }

    @Override
    public String getACNo(SFiscalYear t, int i) {
        return t.getSAccChartList().get(i).getNo();
    }
    
    @Override
    public String getACName(SFiscalYear t, int i) {
        return t.getSAccChartList().get(i).getName();
    }

    @Override
    public AccType getACType(SFiscalYear t, int i) {
        return t.getSAccChartList().get(i).getType();
    }
    
    @Override
    public int getACLevel(SFiscalYear t, int i) {
        return t.getSAccChartList().get(i).getLevel();
    }

    @Override
    public double getACBeginning(SFiscalYear t, int i) {
        return t.getSAccChartList().get(i).getBeginning();
    }
    
    @Override
    public boolean getACIsControl(SFiscalYear t, int i) {
        return t.getSAccChartList().get(i).getType().equals(SAccChart.AccType.CONTROL);
    }

    @Override
    public boolean getACIsEntry(SFiscalYear t, int i) {
        return t.getSAccChartList().get(i).getType().equals(SAccChart.AccType.ENTRY);
    }
}
