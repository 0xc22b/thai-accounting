package gwt.client.def;

import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;

public abstract class FisDef<T> {
	
	public abstract String getFKeyString(T t);
	public abstract int getFBeginMonth(T t);
	public abstract int getFBeginYear(T t);
	public abstract int getFEndMonth(T t);
	public abstract int getFEndYear(T t);
	
    public abstract String getJTName(T t, String keyString);
    public abstract String getJTShortName(T t, String keyString);
    public abstract int getJTListSize(T t);
    public abstract String getJTKeyString(T t, int i);
    public abstract String getJTShortName(T t, int i);
    
    public abstract String getDTJTKeyString(T t, String keyString);
    public abstract String getDTCode(T t, String keyString);
    public abstract String getDTName(T t, String keyString);
    public abstract String getDTJournalDesc(T t, String keyString);
    public abstract int getDTListSize(T t);
    public abstract String getDTKeyString(T t, int i);
    public abstract String getDTJTKeyString(T t, int i);
    public abstract String getDTCode(T t, int i);
    
    public abstract String getAGName(T t, String keyString);
    public abstract int getAGListSize(T t);
    public abstract String getAGKeyString(T t, int i);
    public abstract String getAGName(T t, int i);
    
    public abstract String getACAGKeyString(T t, String keyString);
    public abstract String getACParentACKeyString(T t, String keyString);
    public abstract String getACNo(T t, String keyString);
    public abstract String getACName(T t, String keyString);
    public abstract AccType getACType(T t, String keyString);
    public abstract int getACLevel(T t, String keyString);
    public abstract double getACBeginning(T t, String keyString);
    public abstract String getACAGName(T t, String keyString);
    public abstract String getACParentACNo(T t, String keyString);
    public abstract int getACListSize(T t);
    public abstract String getACKeyString(T t, int i);
    public abstract String getACNo(T t, int i);
    public abstract String getACName(T t, int i);
    public abstract AccType getACType(T t, int i);
    public abstract int getACLevel(T t, int i);
    public abstract double getACBeginning(T t, int i);
    public abstract boolean getACIsControl(T t, int i);
    public abstract boolean getACIsEntry(T t, int i);
    
    public abstract String getFinHeaderName(T t, String keyString);
    public abstract int getFinHeaderListSize(T t);
    public abstract String getFinHeaderKeyString(T t, int i);

    public abstract int getFinItemListSize(T t, String finHeaderKeyString);
    public abstract String getFinItemKeyString(T t, String finHeaderKeyString, int i);
    public abstract int getFinItemSeq(T t, String finHeaderKeyString, String keyString);
    public abstract Comm getFinItemComm(T t, String finHeaderKeyString, String keyString);
    public abstract String getFinItemArg(T t, String finHeaderKeyString, String keyString);
    public abstract CalCon getFinItemCalCon(T t, String finHeaderKeyString, String keyString);
    public abstract PrintCon getFinItemPrintCon(T t, String finHeaderKeyString, String keyString);
    public abstract PrintStyle getFinItemPrintStyle(T t, String finHeaderKeyString, String keyString);
    public abstract Operand getFinItemVar1(T t, String finHeaderKeyString, String keyString);
    public abstract Operand getFinItemVar2(T t, String finHeaderKeyString, String keyString);
    public abstract Operand getFinItemVar3(T t, String finHeaderKeyString, String keyString);
    public abstract Operand getFinItemVar4(T t, String finHeaderKeyString, String keyString);
    
    public abstract String getJDTKeyString(T t, String keyString);
    public abstract String getJJTKeyString(T t, String keyString);
    public abstract String getJNo(T t, String keyString);
    public abstract int getJDay(T t, String keyString);
    public abstract int getJMonth(T t, String keyString);
    public abstract int getJYear(T t, String keyString);
    public abstract int getJCompareDate(T t, String keyString, int day, int month, int year);
    public abstract String getJDesc(T t, String keyString);
    public abstract int getJItemListSize(T t, String keyString);
    public abstract String getJItemACKeyString(T t, String keyString, int i);
    public abstract String getJItemACNo(T t, String keyString, int i);
    public abstract String getJItemACName(T t, String keyString, int i);
    public abstract double getJItemAmt(T t, String keyString, int i);
    public abstract int getJListSize(T t);
    public abstract String getJKeyString(T t, int i);
}
