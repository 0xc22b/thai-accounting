package gwt.client.def;

import gwt.shared.model.SAccChart.AccType;

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

}
