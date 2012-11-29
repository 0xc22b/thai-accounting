package gwt.shared.model;

import static org.junit.Assert.assertEquals;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;

import java.util.Date;

import org.junit.Test;

public class SFinHeaderTest {

    @Test
    public void testAddSFinItem() {
        
        SFinItem sFI1 = new SFinItem("fi1", 3, Comm.ACCNO, "arg", CalCon.CAL,
                    PrintCon.PRINT, PrintStyle.BLANK, Operand.CLEAR, Operand.CLEAR,
                    Operand.MINUS, Operand.PLUS);
        
        SFinItem sFI2 = new SFinItem("fi2", 5, Comm.ACCNO, "arg", CalCon.CAL,
                    PrintCon.PRINT, PrintStyle.BLANK, Operand.CLEAR, Operand.CLEAR,
                    Operand.MINUS, Operand.PLUS);
        
        SFinItem sFI3 = new SFinItem("fi3", 2, Comm.ACCNO, "arg", CalCon.CAL,
                    PrintCon.PRINT, PrintStyle.BLANK, Operand.CLEAR, Operand.CLEAR,
                    Operand.MINUS, Operand.PLUS);
        
        SFinHeader sFin = new SFinHeader("fh1", "name", new Date());
        sFin.addSFinItem(sFI1);
        sFin.addSFinItem(sFI2);
        sFin.addSFinItem(sFI3);
        
        assertEquals("fi3", sFin.getSFinItemList().get(0).getKeyString());
        assertEquals("fi1", sFin.getSFinItemList().get(1).getKeyString());
        assertEquals("fi2", sFin.getSFinItemList().get(2).getKeyString());
    }
    
    @Test
    public void testEditSFinItem() {
        
        SFinItem sFI1 = new SFinItem("fi1", 3, Comm.ACCNO, "arg", CalCon.CAL,
                    PrintCon.PRINT, PrintStyle.BLANK, Operand.CLEAR, Operand.CLEAR,
                    Operand.MINUS, Operand.PLUS);
        
        SFinItem sFI2 = new SFinItem("fi2", 5, Comm.ACCNO, "arg", CalCon.CAL,
                    PrintCon.PRINT, PrintStyle.BLANK, Operand.CLEAR, Operand.CLEAR,
                    Operand.MINUS, Operand.PLUS);
        
        SFinItem sFI3 = new SFinItem("fi3", 2, Comm.ACCNO, "arg", CalCon.CAL,
                    PrintCon.PRINT, PrintStyle.BLANK, Operand.CLEAR, Operand.CLEAR,
                    Operand.MINUS, Operand.PLUS);
        
        SFinHeader sFin = new SFinHeader("fh1", "name", new Date());
        sFin.addSFinItem(sFI1);
        sFin.addSFinItem(sFI2);
        sFin.addSFinItem(sFI3);
        
        sFI2.setSeq(1);
        sFin.editSFinItem(sFI2);
        
        assertEquals("fi2", sFin.getSFinItemList().get(0).getKeyString());
        assertEquals("fi3", sFin.getSFinItemList().get(1).getKeyString());
        assertEquals("fi1", sFin.getSFinItemList().get(2).getKeyString());
    }
}
