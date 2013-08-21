package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface JournalTypeView<T> extends IsWidget {

    public interface Presenter {      
        boolean isJournalTypeNameDuplicate(String keyString, String name);
        void addJournalType(String desc, String shortDesc);
        void editJournalType(String keyString, String desc, String shortDesc);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setJournalType(T t, String keyString, boolean editable);
    void addJournalTypeBtnClicked();
    void editJournalTypeBtnClicked();
}
