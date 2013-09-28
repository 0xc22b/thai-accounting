package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface ListView<L, E> extends IsWidget {

    public interface Presenter {      
        void selectionChanged(boolean isSelected);
        void itemClicked(String keyString);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setData(L l);
    String getSelectedItemKeyString();
    void keepState(int scrollMore);
}
