package gwt.client.def;

import java.util.ArrayList;

import gwt.shared.model.SJournalHeader;

public class JournalDefImpl extends JournalDef<SJournalHeader> {

    private static JournalDefImpl instance = null;

    public static JournalDefImpl getInstance() {
        if (instance == null) {
            instance = new JournalDefImpl();
        }
        return instance;
    }

    @Override
    public String getKeyString(SJournalHeader e) {
        return e.getKeyString();
    }

    @Override
    public String getDTKeyString(SJournalHeader e) {
        return e.getDocTypeKeyString();
    }

    @Override
    public String getNo(SJournalHeader e) {
        return e.getNo();
    }

    @Override
    public int getDay(SJournalHeader e) {
        return e.getDay();
    }

    @Override
    public int getMonth(SJournalHeader e) {
        return e.getMonth();
    }

    @Override
    public int getYear(SJournalHeader e) {
        return e.getYear();
    }

    @Override
    public String getDesc(SJournalHeader e) {
        return e.getDesc();
    }

    @Override
    public int getItemListSize(SJournalHeader e) {
        return e.getItemList().size();
    }

    @Override
    public String getItemACKeyString(SJournalHeader e, int i) {
        return e.getItemList().get(i).getAccChartKeyString();
    }

    @Override
    public double getItemAmt(SJournalHeader e, int i) {
        return e.getItemList().get(i).getAmt();
    }

    @Override
    public int getIndex(ArrayList<ArrayList<SJournalHeader>> mJList, int month, int year) {
        for (int i = 0; i < mJList.size(); i++) {
            ArrayList<SJournalHeader> sJournalList = mJList.get(i);
            if (!sJournalList.isEmpty()
                    && sJournalList.get(0).getMonth() == month
                    && sJournalList.get(0).getYear() == year) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ArrayList<SJournalHeader> getList(ArrayList<ArrayList<SJournalHeader>> mJList,
            int index) {
        return mJList.get(index);
    }

    @Override
    public int getItemDay(SJournalHeader j, int i) {
        return j.getItemList().get(i).day;
    }

    @Override
    public int getItemMonth(SJournalHeader j, int i) {
        return j.getItemList().get(i).month;
    }

    @Override
    public int getItemYear(SJournalHeader j, int i) {
        return j.getItemList().get(i).year;
    }

    @Override
    public String getItemJTShortName(SJournalHeader j, int i) {
        return j.getItemList().get(i).journalTypeShortName;
    }

    @Override
    public String getItemJNo(SJournalHeader j, int i) {
        return j.getItemList().get(i).journalNo;
    }

    @Override
    public String getItemJDesc(SJournalHeader j, int i) {
        return j.getItemList().get(i).journalDesc;
    }
}
