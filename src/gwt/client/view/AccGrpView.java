package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface AccGrpView<T> extends IsWidget {

    public interface Presenter {      
        void addAccGrp(String name);
        void editAccGrp(String keyString, String name);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setAccGrp(T t, String keyString, boolean editable);
    void addAccGrpBtnClicked();
    void editAccGrpBtnClicked();
}
