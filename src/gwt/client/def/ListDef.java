package gwt.client.def;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.view.client.ProvidesKey;

public abstract class ListDef<L, E> {
	
    public abstract List<E> getList(L l);
	
	public abstract ProvidesKey<E> getKeyProvider();
	public abstract String getKeyString(E e);
    public abstract int getColumnSize();
    public abstract String getColumnName(int i);
    public abstract String getColumnValue(E e, int i);
    
    public abstract boolean hasFooter(int i);
    public abstract String getFooterValue(List<E> list, int i);
    
    public abstract boolean hasSort(int i);
    public abstract int getCompare(E e1, E e2, int i);
    
    public abstract boolean hasWidth(int i);
    public abstract double getWidthValue(int i);
    public abstract Unit getWidthUnit(int i);
    
    public abstract int getPageSize();
}
