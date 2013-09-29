package gwt.client.def;

import java.util.ArrayList;
import java.util.List;

public abstract class JournalDef<J, M> {

    public abstract String getKeyString(J j);
    public abstract String getDTKeyString(J j);
    public abstract String getNo(J j);
    public abstract int getDay(J j);
    public abstract int getMonth(J j);
    public abstract int getYear(J j);
    public abstract String getDesc(J j);

    public abstract int getIndex(ArrayList<ArrayList<J>> mJList, int month, int year);
    public abstract ArrayList<J> getList(ArrayList<ArrayList<J>> mJList, int index);

    public abstract List<M> getItemList(J j);

    public abstract String getItemACKeyString(M m);
    public abstract double getItemAmt(M m);

    // These are extra fields used by a ledger populated by getJournalListWithAC in RpcServiceImpl.java
    public abstract int getItemDay(M m);
    public abstract int getItemMonth(M m);
    public abstract int getItemYear(M m);
    public abstract String getItemJTShortName(M m);
    public abstract String getItemJNo(M m);
    public abstract String getItemJDesc(M m);
}
