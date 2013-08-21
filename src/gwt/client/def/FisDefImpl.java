package gwt.client.def;

import gwt.shared.model.SAccChart;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;
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
    
    @Override
    public String getFinHeaderName(SFiscalYear t, String keyString) {
        return t.getSFinHeader(keyString).getName();
    }
    
    @Override
    public int getFinHeaderListSize(SFiscalYear t) {
        return t.getSFinHeaderList().size();
    }

    @Override
    public String getFinHeaderKeyString(SFiscalYear t, int i) {
        return t.getSFinHeaderList().get(i).getKeyString();
    }
    
    @Override
    public int getFinItemListSize(SFiscalYear t, String keyString) {
        return t.getSFinHeader(keyString).getSFinItemList().size();
    }
    
    @Override
    public String getFinItemKeyString(SFiscalYear t, String finHeaderKeyString, int i) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItemList().get(i).getKeyString();
    }

    @Override
    public int getFinItemSeq(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getSeq();
    }

    @Override
    public Comm getFinItemComm(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getComm();
    }

    @Override
    public String getFinItemArg(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getArg();
    }

    @Override
    public CalCon getFinItemCalCon(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getCalCon();
    }

    @Override
    public PrintCon getFinItemPrintCon(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getPrintCon();
    }
    
    @Override
    public PrintStyle getFinItemPrintStyle(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getPrintStyle();
    }

    @Override
    public Operand getFinItemVar1(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getVar1();
    }

    @Override
    public Operand getFinItemVar2(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getVar2();
    }

    @Override
    public Operand getFinItemVar3(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getVar3();
    }

    @Override
    public Operand getFinItemVar4(SFiscalYear t, String finHeaderKeyString, String keyString) {
        return t.getSFinHeader(finHeaderKeyString).getSFinItem(keyString).getVar4();
    }
    
    @Override
    public String getJDTKeyString(SFiscalYear t, String keyString) {
        return t.getSJournal(keyString).getDocTypeKeyString();
    }
    
    @Override
    public String getJJTKeyString(SFiscalYear t, String keyString) {
        String docTypeKeyString = t.getSJournal(keyString).getDocTypeKeyString();
        return t.getSDocType(docTypeKeyString).getJournalTypeKeyString();
    }
    
    @Override
    public String getJNo(SFiscalYear t, String keyString) {
        return t.getSJournal(keyString).getNo();
    }

    @Override
    public int getJDay(SFiscalYear t, String keyString) {
        return t.getSJournal(keyString).getDay();
    }
    
    @Override
    public int getJMonth(SFiscalYear t, String keyString) {
        return t.getSJournal(keyString).getMonth();
    }
    
    @Override
    public int getJYear(SFiscalYear t, String keyString) {
        return t.getSJournal(keyString).getYear();
    }
    
    @Override
    public int getJCompareDate(SFiscalYear t, String keyString, int day, int month, int year) {
        return t.getSJournal(keyString).compareDate(day, month, year);
    }

    @Override
    public String getJDesc(SFiscalYear t, String keyString) {
        return t.getSJournal(keyString).getDesc();
    }

    @Override
    public int getJItemListSize(SFiscalYear t, String keyString) {
        return t.getSJournal(keyString).getItemList().size();
    }
    
    @Override
    public String getJItemACKeyString(SFiscalYear t, String keyString, int i) {
        return t.getSJournal(keyString).getItemList().get(i).getAccChartKeyString();
    }

    @Override
    public String getJItemACNo(SFiscalYear t, String keyString, int i) {
        return t.getSJournal(keyString).getItemList().get(i).getAccChartNo();
    }
    
    @Override
    public String getJItemACName(SFiscalYear t, String keyString, int i) {
        String accChartKeyString = t.getSJournal(keyString).getItemList().get(i).getAccChartKeyString();
        return t.getSAccChart(accChartKeyString).getName();
    }

    @Override
    public double getJItemAmt(SFiscalYear t, String keyString, int i) {
        return t.getSJournal(keyString).getItemList().get(i).getAmt();
    }

    @Override
    public int getJListSize(SFiscalYear t) {
        return t.getSJournalList().size();
    }

    @Override
    public String getJKeyString(SFiscalYear t, int i) {
        return t.getSJournalList().get(i).getKeyString();
    }
}
