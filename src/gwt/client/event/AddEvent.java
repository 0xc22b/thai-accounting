package gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AddEvent extends GwtEvent<AddEvent.Handler>{

    public interface Handler extends EventHandler {
        void onAdd(AddEvent event);
    }
    
    public static final Type<AddEvent.Handler> TYPE = new Type<AddEvent.Handler>();

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onAdd(this);
    }
    
}
