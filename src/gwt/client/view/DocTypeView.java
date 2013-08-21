package gwt.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface DocTypeView<T> extends IsWidget {

    public interface Presenter { 
        boolean isDocTypeCodeDuplicate(String keyString, String code);
        void addDocType(String journalTypeKeyString, String code, String name, String journalDesc);
        void editDocType(String keyString, String journalTypeKeyString, String code, String name, String journalDesc);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setDocType(T t, String keyString, boolean editable);
    void addDocTypeBtnClicked();
    void editDocTypeBtnClicked();
}
