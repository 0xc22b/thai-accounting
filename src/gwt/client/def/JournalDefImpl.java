package gwt.client.def;

import java.util.ArrayList;
import java.util.List;

import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;

public class JournalDefImpl extends JournalDef<SJournalHeader, SJournalItem> {

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
    public List<SJournalItem> getItemList(SJournalHeader j) {
        return j.getItemList();
    }

    @Override
    public String getItemACKeyString(SJournalItem m) {
        return m.getAccChartKeyString();
    }

    @Override
    public double getItemAmt(SJournalItem m) {
        return m.getAmt();
    }

    @Override
    public int getItemDay(SJournalItem m) {
        return m.day;
    }

    @Override
    public int getItemMonth(SJournalItem m) {
        return m.month;
    }

    @Override
    public int getItemYear(SJournalItem m) {
        return m.year;
    }

    @Override
    public String getItemJTShortName(SJournalItem m) {
        return m.journalTypeShortName;
    }

    @Override
    public String getItemJNo(SJournalItem m) {
        return m.journalNo;
    }

    @Override
    public String getItemJDesc(SJournalItem m) {
        return m.journalDesc;
    }
}
