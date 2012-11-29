package gwt.client.def;

public abstract class ComDef<T> {
	
	public abstract String getKeyString(T t);
	public abstract String getName(T t);
	public abstract String getAddress(T t);
	public abstract String getTelNo(T t);
	public abstract String getComType(T t);
	public abstract String getTaxID(T t);
	public abstract String getMerchantID(T t);
	public abstract String getYearType(T t);
	public abstract String getVatRate(T t);
}
