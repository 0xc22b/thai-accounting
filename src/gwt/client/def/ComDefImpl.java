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

    @Override
    public String getAddress(SCom t) {
        return t.getAddress();
    }

    @Override
    public String getTelNo(SCom t) {
        return t.getTelNo();
    }

    @Override
    public String getComType(SCom t) {
        return "comtype";
    }

    @Override
    public String getTaxID(SCom t) {
        return t.getTaxID();
    }

    @Override
    public String getMerchantID(SCom t) {
        return t.getMerchantID();
    }

    @Override
    public String getYearType(SCom t) {
        return "yeartype";
    }

    @Override
    public String getVatRate(SCom t) {
        return t.getVatRate() + "";
    }
    
}
