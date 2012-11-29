package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface FinHeaderView<T> extends IsWidget {

    public interface Presenter {      
        void addFinHeader(String name);
        void editFinHeader(String keyString, String name);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setFinHeader(T t, String keyString, boolean editable);
    void addFinHeaderBtnClicked();
    void editFinHeaderBtnClicked();
}
