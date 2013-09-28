package gwt.client.def;

import java.util.ArrayList;

public abstract class JournalDef<J> {

    public abstract String getKeyString(J j);
    public abstract String getDTKeyString(J j);
    public abstract String getNo(J j);
    public abstract int getDay(J j);
    public abstract int getMonth(J j);
    public abstract int getYear(J j);
    public abstract String getDesc(J j);

    public abstract int getItemListSize(J j);
    public abstract String getItemACKeyString(J j, int i);
    public abstract double getItemAmt(J j, int i);
    
    // These are extra fields used by a ledger populated by getJournalListWithAC in RpcServiceImpl.java
    public abstract int getItemDay(J j, int i);
    public abstract int getItemMonth(J j, int i);
    public abstract int getItemYear(J j, int i);
    public abstract String getItemJTShortName(J j, int i);
    public abstract String getItemJNo(J j, int i);
    public abstract String getItemJDesc(J j, int i);

    public abstract int getIndex(ArrayList<ArrayList<J>> mJList, int month, int year);
    public abstract ArrayList<J> getList(ArrayList<ArrayList<J>> mJList, int index);
}
