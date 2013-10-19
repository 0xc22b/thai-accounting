package gwt.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ObjectElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class PdfObject extends Widget {

    ObjectElement o;

    public PdfObject() {
        o = Document.get().createObjectElement();
        setElement(o);
        
        addNativeReadyStateChange(this, o);
    }

    public void setData(String data) {
        o.setData(data);
    }

    public void print() {
        print(o);
    }

    private native void print(ObjectElement o)/*-{
		o.print();
    }-*/;

    public HandlerRegistration addReadyStateChangeHandler(
            ValueChangeHandler<String> handler) {
        return this.addHandler(handler, ValueChangeEvent.getType());
    }

    public String getReadyState() {
        try {
            return o.getPropertyString("readyState");
        } catch (Exception e) {
            return null;
        }
    }

    private void fireReadyStateChange() {
        this.fireEvent(new ValueChangeEvent<String>(getReadyState()) {});
    }

    private static native void addNativeReadyStateChange(PdfObject self,
            ObjectElement e) /*-{
		var handleStateChange = function() {
			self.@gwt.client.ui.PdfObject::fireReadyStateChange()();
		};
		if (e.addEventListener) {
			e.addEventListener("readystatechange", handleStateChange, false);
		} else if (e.attachEvent) {
			e.attachEvent("onreadystatechange", handleStateChange);
		} else {
			e.onreadystatechange = handleStateChange;
		}
    }-*/;
}