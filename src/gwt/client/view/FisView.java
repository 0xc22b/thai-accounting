package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface FisView<T> extends IsWidget {

    public interface Presenter {      
        void addFis(int beginMonth, int beginYear, int endMonth, int endYear, boolean isSetup);
        void editFis(String keyString, int beginMonth, int beginYear, int endMonth, int endYear);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setFis(T t, boolean editable);
    void addFisBtnClicked(boolean isSetup);
    void editFisBtnClicked();
}
