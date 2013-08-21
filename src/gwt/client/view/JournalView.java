package gwt.client.view;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface JournalView<T> extends IsWidget {

    public class ItemData {
        public String accChartKeyString;
        public double amt;
        public Date date;
        
        public ItemData(String accChartKeyString, double amt, Date date){
            this.accChartKeyString = accChartKeyString;
            this.amt = amt;
            this.date = date;
        }
    }
    
    public interface Presenter {
        boolean isJournalNoDuplicate(String keyString, String docTypeKeyString,
                String no);
        void addJournal(String docTypeKeyString, String no, int day, int month, int year, String desc, ArrayList<ItemData> itemDataList);
        void editJournal(String keyString, String docTypeKeyString, String no, int day, int month, int year, 
                String desc, ArrayList<ItemData> itemDataList);
        void onDocTypeChanged();
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setJournal(T t, String journalTypeKeyString, String keyString, boolean editable);
    void addJournalBtnClicked();
    void editJournalBtnClicked();
    void addItemBtnClicked(T t);
    void updateJournalDesc(T t);
}
