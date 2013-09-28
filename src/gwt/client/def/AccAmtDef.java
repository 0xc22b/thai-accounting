package gwt.client.def;

import java.util.Map;

public abstract class AccAmtDef<A> {

    public abstract double getAmt(Map<String, A> m, String keyString);

}
