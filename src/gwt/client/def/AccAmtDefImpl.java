package gwt.client.def;

import gwt.shared.model.SAccAmt;

import java.util.Map;

public class AccAmtDefImpl extends AccAmtDef<SAccAmt> {

    private static AccAmtDefImpl instance = null;

    public static AccAmtDefImpl getInstance() {
        if (instance == null) {
            instance = new AccAmtDefImpl();
        }
        return instance;
    }

    @Override
    public double getAmt(Map<String, SAccAmt> m, String keyString) {
        return m.get(keyString) == null ? 0 : m.get(keyString).getAmt();
    }
}
