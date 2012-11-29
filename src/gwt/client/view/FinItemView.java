package gwt.client.view;

import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface FinItemView<T> extends IsWidget {

    public interface Presenter {      
        void addFinItem(int seq, Comm comm, String arg, CalCon calCon,
                PrintCon printCon, PrintStyle printStyle, Operand var1,
                Operand var2, Operand var3, Operand var4);
        void editFinItem(String keyString, int seq, Comm comm, String arg,
                CalCon calCon, PrintCon printCon, PrintStyle printStyle, 
                Operand var1, Operand var2, Operand var3, Operand var4);
    }

    Widget asWidget();
    void init(Presenter presenter);
    void setFinItem(T t, String finHeaderKeyString,
            String keyString, boolean editable);
    void addFinItemBtnClicked();
    void editFinItemBtnClicked();
}
