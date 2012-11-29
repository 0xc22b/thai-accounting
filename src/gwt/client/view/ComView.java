package gwt.client.view;

import gwt.shared.model.SCom.ComType;
import gwt.shared.model.SCom.YearType;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface ComView<T> extends IsWidget {

    public interface Presenter {      
        void addCom(String name, String address, String telNo, ComType comType, String taxID, String merchantID,
                YearType yearType, Double vatRate);
        void editCom(String keyString, String name, String address, String telNo, ComType comType, String taxID, String merchantID,
                YearType yearType, Double vatRate);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setCom(T t, boolean editable);
    void addComBtnClicked();
    void editComBtnClicked();
}
