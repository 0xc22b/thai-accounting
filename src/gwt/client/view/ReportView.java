package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface ReportView<T> extends IsWidget {

    public interface Presenter {      
        
    }

    Widget asWidget();
    void init(Presenter presenter);
    void onPrintBtnClicked();
    void setChartData(T t, String comName);
    void setJourData(T t, String comName, String journalTypeKeyString, int beginDay, int beginMonth, int beginYear,
            int endDay, int endMonth, int endYear);
    void setLedgerData(T t, String comName, String beginAccChartKeyString, String endAccChartKeyString, int beginDay, int beginMonth, int beginYear,
            int endDay, int endMonth, int endYear);
    void setTrialData(T t, String comName);
    void setFinData(T t, String comName, String finKeyString);
}
