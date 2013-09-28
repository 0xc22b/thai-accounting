package gwt.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface ReportView<T, J, A> extends IsWidget {

    public interface Presenter {      
        
    }

    Widget asWidget();
    void init(Presenter presenter);

    void onPrintBtnClicked();

    void setChartData(T t, String comName);

    void setJourData(T t, ArrayList<ArrayList<J>> mJList, ArrayList<int[]> months, String comName,
            String journalTypeName);

    void setLedgerData(T t, HashMap<String, J> aJList, int[] dates, String comName,
            String beginACNo, String endACNo, boolean doShowAll);

    void setTrialData(T t, Map<String, A> aMap, String comName, boolean doShowAll);

    void setBalanceData(T t, Map<String, A> aMap, String comName, String assetACKeyString,
            String debtACKeyString, String shareholderACKeyString,
            String accruedProfitACKeyString, String incomeACKeyString,
            String expenseACKeyString, boolean doShowAll, boolean doesSplit);

    void setProfitData(T t, Map<String, A> aMap, String comName, String incomeACKeyString,
            String expenseACKeyString, boolean doShowAll, boolean doesSplit);

    void setCostData(T t, Map<String, A> aMap, String comName, String costACKeyString,
            boolean doShowAll);
}
