package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface BeginView<T> extends IsWidget {

    public interface Presenter {
        void setBegin(String keyString, double beginning);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setBegin(T t, String keyString, boolean editable);
    void setBeginBtnClicked();
}
