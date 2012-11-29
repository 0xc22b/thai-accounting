package gwt.client.view;

import gwt.shared.model.SAccChart.AccType;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface AccChartView<T> extends IsWidget {

    public interface Presenter {      
        void addAccChart(String accGroupKeyString, String parentAccChartKeyString, String no, String name, 
                AccType type, int level);
        void editAccChart(String keyString, String accGroupKeyString, String parentAccChartKeyString, String no, 
                String name, AccType type, int level);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setAccChart(T t, String action, String keyString);
    void addAccChartBtnClicked();
    void editAccChartBtnClicked();
}
