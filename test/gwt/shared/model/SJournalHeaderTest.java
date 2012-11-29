package gwt.shared.model;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class SJournalHeaderTest {

    @Test
    public void testAddItem() {
        
        Date d1 = new Date();
        Date d2 = new Date(d1.getTime() + 100l);
        Date d3 = new Date(d1.getTime() - 100l);
        
        SJournalItem sJournalItem1 = new SJournalItem("ji1", "acks", 500.02, d1);
        SJournalItem sJournalItem2 = new SJournalItem("ji2", "acks", 156.02, d2);
        SJournalItem sJournalItem3 = new SJournalItem("ji3", "acks", 156.02, d3);
        
        SJournalHeader sJournal = new SJournalHeader(null, "dt1", "no", 25, 11, 2555, "desc");
        sJournal.addItem(sJournalItem1);
        sJournal.addItem(sJournalItem2);
        sJournal.addItem(sJournalItem3);
        
        assertEquals("ji3", sJournal.getItemList().get(0).getKeyString());
        assertEquals("ji1", sJournal.getItemList().get(1).getKeyString());
        assertEquals("ji2", sJournal.getItemList().get(2).getKeyString());
    }
}
