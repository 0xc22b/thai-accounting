package gwt.server.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import gwt.server.PMF;
import gwt.server.account.model.AccChart;
import gwt.server.account.model.AccGroup;
import gwt.server.account.model.Com;
import gwt.server.account.model.DocType;
import gwt.server.account.model.FinHeader;
import gwt.server.account.model.FinItem;
import gwt.server.account.model.FiscalYear;
import gwt.server.account.model.JournalHeader;
import gwt.server.account.model.JournalItem;
import gwt.server.account.model.JournalType;
import gwt.server.user.UserManager;
import gwt.server.user.UserVerifier;
import gwt.server.user.model.User;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SCom.ComType;
import gwt.shared.model.SCom.YearType;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DbTest {
    
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig(),
            new LocalMemcacheServiceTestConfig());
    
    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testGetFiscalYears() {
        
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        List<Key> comKeyList = new ArrayList<Key>();
        Com com = null;
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            com = new Com(user.getKey(), "com1", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com);
            FiscalYear fis1 = new FiscalYear(com.getKeyString(), 1, 2012, 12,
                    2012);
            pm1.makePersistent(fis1);
            FiscalYear fis2 = new FiscalYear(com.getKeyString(), 1, 2013, 12,
                    2013);
            pm1.makePersistent(fis2);
            
            comKeyList.add(com.getKey());
        } finally {
            pm1.close();
        }
        
        PersistenceManager pm2 = PMF.get().getPersistenceManager();
        try {
            List<FiscalYear> fisList = Db.getFiscalYears(pm2, comKeyList);
            assertEquals(2, fisList.size());
            for (FiscalYear fis : fisList) {
                if (fis.getComKey().equals(com.getKey())) {
                    if (fis.getBeginMonth() == 1 && fis.getEndMonth() == 12) {
                        if ((fis.getBeginYear() == 2012 && fis.getEndYear() == 2012)
                                || (fis.getBeginYear() == 2013 && fis.getEndYear() == 2013)) {
                            continue;
                        }
                    }
                }
                fail();
            }
        } finally {
            pm2.close();
        }
    }
    
    @Test
    public void testSetBeginning() {
        
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        AccChart accChart = null;
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            Com com = new Com(user.getKey(), "com1", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com);
            FiscalYear fis = new FiscalYear(com.getKeyString(), 1, 2012, 12,
                    2012);
            pm1.makePersistent(fis);
            accChart = new AccChart(fis.getKeyString(), null, null, "100010",
                    "acc1", AccType.CONTROL, 0, 12.23);
            pm1.makePersistent(accChart);
        } finally {
            pm1.close();
        }
        
        PersistenceManager pm2 = PMF.get().getPersistenceManager();
        try {
            Db.setBeginning(pm2, accChart.getKeyString(), 2345.89);
        } finally {
            pm2.close();
        }
        
        PersistenceManager pm3 = PMF.get().getPersistenceManager();
        try {
            AccChart accChart_tmp = pm3.getObjectById(AccChart.class, accChart.getKey());
            assertEquals(accChart.getKey(), accChart_tmp.getKey());
            assertEquals("100010", accChart_tmp.getNo());
            assertEquals(2345.89, accChart_tmp.getBeginning(), 2);
        } finally {
            pm3.close();
        }
    }
    
    @Test
    public void testAddFinHeader() {
        
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        FiscalYear fis = null;
        FinHeader finHeader = null;
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            Com com = new Com(user.getKey(), "com1", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com);
            fis = new FiscalYear(com.getKeyString(), 1, 2012, 12,
                    2012);
            pm1.makePersistent(fis);
        
            SFinHeader sFinHeader = new SFinHeader(null, "fin1", new Date());
            finHeader = Db.addFinHeader(pm1, fis.getKeyString(), sFinHeader);
        } finally {
            pm1.close();
        }
        
        assertNotNull(finHeader);
        assertEquals(fis.getKey(), finHeader.getFisKey());
        assertEquals("fin1", finHeader.getName());
        
        PersistenceManager pm2 = PMF.get().getPersistenceManager();
        try {
            FinHeader finHeader_tmp = pm2.getObjectById(FinHeader.class, finHeader.getKey());
            assertNotNull(finHeader_tmp);
            assertEquals(fis.getKey(), finHeader_tmp.getFisKey());
            assertEquals("fin1", finHeader_tmp.getName());
            
        } finally {
            pm2.close();
        }
    }
    
    @Test
    public void testEditFinHeader() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        FiscalYear fis = null;
        FinHeader finHeader = null;
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            Com com = new Com(user.getKey(), "com1", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com);
            fis = new FiscalYear(com.getKeyString(), 1, 2012, 12,
                    2012);
            pm1.makePersistent(fis);
        
            SFinHeader sFinHeader = new SFinHeader(null, "fin1", new Date());
            finHeader = Db.addFinHeader(pm1, fis.getKeyString(), sFinHeader);
        } finally {
            pm1.close();
        }
        
        PersistenceManager pm2 = PMF.get().getPersistenceManager();
        try {
            SFinHeader sFinHeader = new SFinHeader(finHeader.getKeyString(),
                    "fin1edited", new Date());
            FinHeader finHeader_tmp = Db.editFinHeader(pm2, sFinHeader);
            assertEquals(finHeader.getKey(), finHeader_tmp.getKey());
            assertEquals(finHeader.getFisKey(), finHeader_tmp.getFisKey());
            assertEquals("fin1edited", finHeader_tmp.getName());
        } finally {
            pm2.close();
        }
        
        PersistenceManager pm3 = PMF.get().getPersistenceManager();
        try {
            FinHeader finHeader_tmp = pm3.getObjectById(FinHeader.class, finHeader.getKey());
            assertNotNull(finHeader_tmp);
            assertEquals(fis.getKey(), finHeader_tmp.getFisKey());
            assertEquals("fin1edited", finHeader_tmp.getName());
            
        } finally {
            pm3.close();
        }
    }
    
    @Test
    public void testAddFinItem() {
        
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        FiscalYear fis = null;
        FinHeader finHeader = null;
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            Com com = new Com(user.getKey(), "com1", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com);
            fis = new FiscalYear(com.getKeyString(), 1, 2012, 12,
                    2012);
            pm1.makePersistent(fis);
        
            SFinHeader sFinHeader = new SFinHeader(null, "fin1", new Date());
            finHeader = Db.addFinHeader(pm1, fis.getKeyString(), sFinHeader);
        } finally {
            pm1.close();
        }
        
        FinItem finItem = null;
        PersistenceManager pm2 = PMF.get().getPersistenceManager();
        try {
            SFinItem sFinItem = new SFinItem(null, 0, Comm.ACCNO, "1001200",
                    CalCon.CAL, PrintCon.NOPRINT, PrintStyle.CENTER,
                    Operand.CLEAR, Operand.BLANK, Operand.BLANK, Operand.MINUS);
            finItem = Db.addFinItem(pm2, finHeader.getKeyString(), sFinItem);
            
            assertEquals(0, finItem.getSeq());
            assertEquals(Comm.ACCNO, finItem.getComm());
            assertEquals("1001200", finItem.getArg());
            assertEquals(CalCon.CAL, finItem.getCalCon());
            assertEquals(PrintCon.NOPRINT, finItem.getPrintCon());
            assertEquals(PrintStyle.CENTER, finItem.getPrintStyle());
            assertEquals(Operand.CLEAR, finItem.getVar1());
            assertEquals(Operand.BLANK, finItem.getVar2());
            assertEquals(Operand.BLANK, finItem.getVar3());
            assertEquals(Operand.MINUS, finItem.getVar4());
        } finally {
            pm2.close();
        }
        
        
        PersistenceManager pm3 = PMF.get().getPersistenceManager();
        try {
            FinItem finItem_tmp = pm3.getObjectById(FinItem.class, finItem.getKey());
            assertNotNull(finItem_tmp);
            assertEquals(0, finItem_tmp.getSeq());
            assertEquals(Comm.ACCNO, finItem_tmp.getComm());
            assertEquals("1001200", finItem_tmp.getArg());
            assertEquals(CalCon.CAL, finItem_tmp.getCalCon());
            assertEquals(PrintCon.NOPRINT, finItem_tmp.getPrintCon());
            assertEquals(PrintStyle.CENTER, finItem_tmp.getPrintStyle());
            assertEquals(Operand.CLEAR, finItem_tmp.getVar1());
            assertEquals(Operand.BLANK, finItem_tmp.getVar2());
            assertEquals(Operand.BLANK, finItem_tmp.getVar3());
            assertEquals(Operand.MINUS, finItem_tmp.getVar4());
            
        } finally {
            pm3.close();
        }
    }
    
    @Test
    public void testEditFinItem() {
        
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        FinItem finItem = null;
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            Com com = new Com(user.getKey(), "com1", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com);
            FiscalYear fis = new FiscalYear(com.getKeyString(), 1, 2012, 12,
                    2012);
            pm1.makePersistent(fis);
        
            SFinHeader sFinHeader = new SFinHeader(null, "fin1", new Date());
            FinHeader finHeader = Db.addFinHeader(pm1, fis.getKeyString(), sFinHeader);
            
            SFinItem sFinItem = new SFinItem(null, 0, Comm.ACCNO, "1001200",
                    CalCon.CAL, PrintCon.NOPRINT, PrintStyle.CENTER,
                    Operand.CLEAR, Operand.BLANK, Operand.BLANK, Operand.MINUS);
            finItem = Db.addFinItem(pm1, finHeader.getKeyString(), sFinItem);
        } finally {
            pm1.close();
        }
        
        PersistenceManager pm2 = PMF.get().getPersistenceManager();
        try {
            SFinItem sFinItem = new SFinItem(finItem.getKeyString(), 3,
                    Comm.TXT, "Text", CalCon.CALIFPOS, PrintCon.PRINT,
                    PrintStyle.BLANK, Operand.BLANK, Operand.MINUS, Operand.PLUS,
                    Operand.CLEAR);
            finItem = Db.editFinItem(pm2, sFinItem);
            
            assertEquals(3, finItem.getSeq());
            assertEquals(Comm.TXT, finItem.getComm());
            assertEquals("Text", finItem.getArg());
            assertEquals(CalCon.CALIFPOS, finItem.getCalCon());
            assertEquals(PrintCon.PRINT, finItem.getPrintCon());
            assertEquals(PrintStyle.BLANK, finItem.getPrintStyle());
            assertEquals(Operand.BLANK, finItem.getVar1());
            assertEquals(Operand.MINUS, finItem.getVar2());
            assertEquals(Operand.PLUS, finItem.getVar3());
            assertEquals(Operand.CLEAR, finItem.getVar4());
        } finally {
            pm2.close();
        }
        
        
        PersistenceManager pm3 = PMF.get().getPersistenceManager();
        try {
            FinItem finItem_tmp = pm3.getObjectById(FinItem.class, finItem.getKey());
            assertNotNull(finItem_tmp);
            assertEquals(3, finItem_tmp.getSeq());
            assertEquals(Comm.TXT, finItem_tmp.getComm());
            assertEquals("Text", finItem_tmp.getArg());
            assertEquals(CalCon.CALIFPOS, finItem_tmp.getCalCon());
            assertEquals(PrintCon.PRINT, finItem_tmp.getPrintCon());
            assertEquals(PrintStyle.BLANK, finItem_tmp.getPrintStyle());
            assertEquals(Operand.BLANK, finItem_tmp.getVar1());
            assertEquals(Operand.MINUS, finItem_tmp.getVar2());
            assertEquals(Operand.PLUS, finItem_tmp.getVar3());
            assertEquals(Operand.CLEAR, finItem_tmp.getVar4());
            
        } finally {
            pm3.close();
        }
    }
    
    @Test
    public void testAddJournal() {
        
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        JournalHeader jh = null;
        DocType docType = null;
        AccChart accChart = null;
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            Com com = new Com(user.getKey(), "com1", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com);
            FiscalYear fis = new FiscalYear(com.getKeyString(), 1, 2012, 12,
                    2012);
            pm1.makePersistent(fis);
            
            JournalType journalType = new JournalType(fis.getKeyString(), "name", "shortName", new Date());
            pm1.makePersistent(journalType);
            
            docType = new DocType(fis.getKeyString(), journalType.getKeyString(),
                    "code", "name", "journalDesc", new Date());
            pm1.makePersistent(docType);
            
            AccGroup accGrp = new AccGroup(fis.getKeyString(), "name", new Date());
            pm1.makePersistent(accGrp);
            
            accChart = new AccChart(fis.getKeyString(), accGrp.getKeyString(),
                    null, "101101", "name", AccType.CONTROL, 1, 0.0);
            pm1.makePersistent(accChart);
            
            SJournalItem sJI1 = new SJournalItem(null, accChart.getKeyString(), 508.08, new Date());
            SJournalItem sJI2 = new SJournalItem(null, accChart.getKeyString(), 98.08, new Date());
            
            SJournalHeader sJH = new SJournalHeader(null, docType.getKeyString(), "001", 3, 11, 2012, "Desc");
            sJH.addItem(sJI1);
            sJH.addItem(sJI2);
            
            jh = Db.addJournal(pm1, fis.getKeyString(), sJH);
            
            assertEquals(2, jh.getItemSet().size());
            assertEquals(docType.getKeyString(), jh.getDocTypeKeyString());
            assertEquals("001", jh.getNo());
            assertEquals(3, jh.getDay());
            assertEquals(11, jh.getMonth());
            assertEquals(2012, jh.getYear());
            assertEquals("Desc", jh.getDesc());
            
            for (JournalItem ji : jh.getItemSet()) {
                if (ji.getAccChartKey().equals(accChart.getKey())
                        && ji.getAmt() == 508.08) {
                    continue;
                }
                
                if (ji.getAccChartKey().equals(accChart.getKey())
                        && ji.getAmt() == 98.08) {
                    continue;
                }
                fail();
            }
        } finally {
            pm1.close();
        }
        
        PersistenceManager pm2 = PMF.get().getPersistenceManager();
        try {
            JournalHeader jh_tmp = pm2.getObjectById(JournalHeader.class, jh.getKey());
            assertNotNull(jh_tmp);
            assertEquals(2, jh_tmp.getItemSet().size());
            assertEquals(docType.getKeyString(), jh_tmp.getDocTypeKeyString());
            assertEquals("001", jh_tmp.getNo());
            assertEquals(3, jh_tmp.getDay());
            assertEquals(11, jh_tmp.getMonth());
            assertEquals(2012, jh_tmp.getYear());
            assertEquals("Desc", jh_tmp.getDesc());
            
            for (JournalItem ji : jh_tmp.getItemSet()) {
                if (ji.getAccChartKey().equals(accChart.getKey())
                        && ji.getAmt() == 508.08) {
                    continue;
                }
                
                if (ji.getAccChartKey().equals(accChart.getKey())
                        && ji.getAmt() == 98.08) {
                    continue;
                }
                fail();
            }
        } finally {
            pm2.close();
        }
    }
    
    @Test
    public void testEditJournal() {
        UserVerifier.Log log = new UserVerifier.Log();
        User user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        
        FiscalYear fis = null;
        JournalHeader jh = null;
        DocType docType = null;
        AccChart accChart = null;
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            Com com = new Com(user.getKey(), "com1", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com);
            fis = new FiscalYear(com.getKeyString(), 1, 2012, 12,
                    2012);
            pm1.makePersistent(fis);
            
            JournalType journalType = new JournalType(fis.getKeyString(), "name", "shortName", new Date());
            pm1.makePersistent(journalType);
            
            docType = new DocType(fis.getKeyString(), journalType.getKeyString(),
                    "code", "name", "journalDesc", new Date());
            pm1.makePersistent(docType);
            
            AccGroup accGrp = new AccGroup(fis.getKeyString(), "name", new Date());
            pm1.makePersistent(accGrp);
            
            accChart = new AccChart(fis.getKeyString(), accGrp.getKeyString(),
                    null, "101101", "name", AccType.CONTROL, 1, 0.0);
            pm1.makePersistent(accChart);
            
            SJournalItem sJI1 = new SJournalItem(null, accChart.getKeyString(), 508.08, new Date());
            SJournalItem sJI2 = new SJournalItem(null, accChart.getKeyString(), 98.08, new Date());
            
            SJournalHeader sJH = new SJournalHeader(null, docType.getKeyString(), "001", 3, 11, 2012, "Desc");
            sJH.addItem(sJI1);
            sJH.addItem(sJI2);
            
            jh = Db.addJournal(pm1, fis.getKeyString(), sJH);
            
        } finally {
            pm1.close();
        }
        
        AccChart accChart1;
        PersistenceManager pm2 = PMF.get().getPersistenceManager();
        try {
            
            AccGroup accGrp = new AccGroup(fis.getKeyString(), "name", new Date());
            pm2.makePersistent(accGrp);
            
            accChart1 = new AccChart(fis.getKeyString(), accGrp.getKeyString(),
                    null, "101101", "name", AccType.CONTROL, 1, 0.0);
            pm2.makePersistent(accChart1);
            
            SJournalHeader sJH = new SJournalHeader(jh.getKeyString(),
                    docType.getKeyString(), "002", 4, 12, 2013, "Desc_e");
            
            boolean y = false;
            Iterator<JournalItem> it = jh.getItemSet().iterator();
            while (it.hasNext()) {
                JournalItem ji = it.next();
                SJournalItem sJI = new SJournalItem(ji.getKeyString(),
                        accChart1.getKeyString(), y ? 111.08 : 211.08, new Date());
                sJH.addItem(sJI);
            }
            
            jh = Db.editJournal(pm2, sJH);
            
            assertEquals(2, jh.getItemSet().size());
            assertEquals(docType.getKeyString(), jh.getDocTypeKeyString());
            assertEquals("002", jh.getNo());
            assertEquals(4, jh.getDay());
            assertEquals(12, jh.getMonth());
            assertEquals(2013, jh.getYear());
            assertEquals("Desc_e", jh.getDesc());
            
            for (JournalItem ji : jh.getItemSet()) {
                if (ji.getAccChartKey().equals(accChart1.getKey())
                        && ji.getAmt() == 111.08) {
                    continue;
                }
                
                if (ji.getAccChartKey().equals(accChart1.getKey())
                        && ji.getAmt() == 211.08) {
                    continue;
                }
                fail();
            }
        } finally {
            pm2.close();
        }
        
        PersistenceManager pm3 = PMF.get().getPersistenceManager();
        try {
            JournalHeader jh_tmp = pm3.getObjectById(JournalHeader.class, jh.getKey());
            assertNotNull(jh_tmp);
            assertEquals(2, jh_tmp.getItemSet().size());
            assertEquals(docType.getKeyString(), jh_tmp.getDocTypeKeyString());
            assertEquals("002", jh_tmp.getNo());
            assertEquals(4, jh_tmp.getDay());
            assertEquals(12, jh_tmp.getMonth());
            assertEquals(2013, jh_tmp.getYear());
            assertEquals("Desc_e", jh_tmp.getDesc());
            
            for (JournalItem ji : jh_tmp.getItemSet()) {
                if (ji.getAccChartKey().equals(accChart1.getKey())
                        && ji.getAmt() == 111.08) {
                    continue;
                }
                
                if (ji.getAccChartKey().equals(accChart1.getKey())
                        && ji.getAmt() == 211.08) {
                    continue;
                }
                fail();
            }
        } finally {
            pm3.close();
        }
    }
}
