package gwt.client.def;

import gwt.shared.model.SCom;

public class ComDefImpl extends ComDef<SCom> {

    private static ComDefImpl instance = null;

    public static ComDefImpl getInstance() {
        if (instance == null) {
            instance = new ComDefImpl();
        }
        return instance;
    }

    @Override
    public String getKeyString(SCom t) {
        return t.getKeyString();
    }

    @Override
    public String getName(SCom t) {
        return t.getName();
    }    
}
