package gwt.client.view;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface PdfView extends IsWidget {

    public interface Presenter {

    }
    
    Widget asWidget();
    void init(Presenter presenter);

    void onPrintBtnClicked();
    
    void setData(String data);
    void addReadyStateChangeHandler(ValueChangeHandler<String> h);
}