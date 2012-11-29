package gwt.client.ui;

import gwt.shared.InvalidValueException;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class CustomDoubleBoxTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "gwt.ThaiAccounting";
    }

    @Test
    public void testGetCustomValue() {
        CustomDoubleBox doubleBox = new CustomDoubleBox();
        doubleBox.setValue(40.00);
        
        assertEquals(40.00, doubleBox.getCustomValue());
        
        doubleBox.setRange(0, 999999);
        doubleBox.setValue(1000000.00);
        try {
            doubleBox.getCustomValue();
            fail();
        } catch (InvalidValueException e) {
            
        }
        
        doubleBox.setValue(88.888);
        try {
            doubleBox.getCustomValue();
            fail();
        } catch (InvalidValueException e) {
            
        }
        
        doubleBox.setValue(-88.88);
        try {
            doubleBox.getCustomValue();
            fail();
        } catch (InvalidValueException e) {
            
        }
    }
}
