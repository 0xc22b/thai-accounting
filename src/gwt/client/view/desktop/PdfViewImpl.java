package gwt.client.view.desktop;

import gwt.client.ui.PdfObject;
import gwt.client.view.PdfView;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class PdfViewImpl extends Composite implements PdfView {

    FlowPanel panel;
    PdfObject pdf;
    
    public PdfViewImpl() {
        panel = new FlowPanel();
        panel.getElement().getStyle().setHeight(100, Style.Unit.PCT);
        
        initWidget(panel);
    }
    
    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void init(Presenter presenter) {
        panel.clear();
        
        pdf = new PdfObject();
        pdf.setWidth("100%");
        pdf.setHeight("100%");
        panel.add(pdf);
    }

    @Override
    public void onPrintBtnClicked() {
        pdf.print();
    }

    @Override
    public void setData(String data) {
        pdf.setData(data);
    }

    @Override
    public void addReadyStateChangeHandler(ValueChangeHandler<String> h) {
        pdf.addReadyStateChangeHandler(h);
    }
}
