package gwt.client.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface Shell extends AcceptsOneWidget, IsWidget {

    void setWidget(IsWidget w);
    void reset();
    void setHLb(String text);
    void setActBtn(int i, String text, String actionName, boolean visible);
    void setActBtnVisible(int i, boolean visible);

}
