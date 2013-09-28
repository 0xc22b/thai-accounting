package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface ComView<T> extends IsWidget {

    public interface Presenter {      
        void addCom(String name);
        void editCom(String keyString, String name);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setCom(T t, boolean editable);
    void addComBtnClicked();
    void editComBtnClicked();
}
