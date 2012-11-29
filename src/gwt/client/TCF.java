package gwt.client;

import com.google.gwt.core.client.GWT;

public class TCF {

    private static final TConstants constants = GWT.create(TConstants.class);
    
    public static TConstants get(){
        return constants;
    }
    
}
