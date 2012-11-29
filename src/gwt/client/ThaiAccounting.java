package gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ThaiAccounting implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        // Create ClientFactory using deferred binding so we can replace with different impls in gwt.xml
        ClientFactory clientFactory = GWT.create(ClientFactory.class);
        clientFactory.getApp().run(RootLayoutPanel.get());
    }
}
