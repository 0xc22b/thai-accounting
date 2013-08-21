package gwt.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gwt.server.account.model.AccChart;
import gwt.server.account.model.AccGroup;
import gwt.server.account.model.Com;
import gwt.server.account.model.DocType;
import gwt.server.account.model.FinHeader;
import gwt.server.account.model.FinItem;
import gwt.server.account.model.FiscalYear;
import gwt.server.account.model.JournalHeader;
import gwt.server.account.model.JournalType;
import gwt.server.user.UserManager;
import gwt.server.user.UserVerifier;
import gwt.server.user.model.Session;
import gwt.server.user.model.User;
import gwt.shared.NotLoggedInException;
import gwt.shared.SConstants;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SCom;
import gwt.shared.model.SCom.ComType;
import gwt.shared.model.SCom.YearType;
import gwt.shared.model.SComList;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;
import gwt.shared.model.SJournalType;

import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;


public class RpcServiceImplTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig(),
            new LocalMemcacheServiceTestConfig());
    
    private RpcServiceImpl rpcService;
    private User user;
    private Session session;
    
    @Before
    public void setUp() {
        helper.setUp();
        
        UserVerifier.Log log = new UserVerifier.Log();
        user = UserManager.signUp("wit", "wit@gmail.c", "raknaja2",
                "raknaja2", log);
        session = UserManager.addLoginSession(user.getKey());
        
        rpcService = new RpcServiceImpl();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
    
    @Test
    public void testGetCom() {
        
        Com com1 = null;
        Com com2 = null;
        FiscalYear fis1 = null;
        FiscalYear fis2 = null;
        FiscalYear fis3 = null;
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            com1 = new Com(user.getKey(), "com1", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com1);
            
            fis1 = new FiscalYear(com1.getKeyString(), 1, 2012, 12,
                    2012);
            pm1.makePersistent(fis1);
            
            fis2 = new FiscalYear(com1.getKeyString(), 1, 2013, 12,
                    2013);
            pm1.makePersistent(fis2);
            
            com2 = new Com(user.getKey(), "com2", "", "", ComType.LIMITED,
                    "", "", YearType.INTER, 7.0, new Date());
            pm1.makePersistent(com2);
            
            fis3 = new FiscalYear(com2.getKeyString(), 3, 2012, 2,
                    2013);
            pm1.makePersistent(fis3);
        } finally {
            pm1.close();
        }
        
        try {
            SComList sComList = rpcService.getComList(session.getKeyString(), session.getSessionID());
            assertEquals(2, sComList.getSComList().size());
            
            SCom sCom1 = sComList.getSCom(com1.getKeyString());
            assertEquals("com1", sCom1.getName());
            
            SFiscalYear sFis1 = sCom1.getSFis(fis1.getKeyString());
            assertEquals(1, sFis1.getBeginMonth());
            assertEquals(2012, sFis1.getBeginYear());
            assertEquals(12, sFis1.getEndMonth());
            assertEquals(2012, sFis1.getEndYear());
            
            SFiscalYear sFis2 = sCom1.getSFis(fis2.getKeyString());
            assertEquals(1, sFis2.getBeginMonth());
            assertEquals(2013, sFis2.getBeginYear());
            assertEquals(12, sFis2.getEndMonth());
            assertEquals(2013, sFis2.getEndYear());
            
            SCom sCom2 = sComList.getSCom(com2.getKeyString());
            assertEquals("com2", sCom2.getName());
            
            SFiscalYear sFis3 = sCom2.getSFis(fis3.getKeyString());
            assertEquals(3, sFis3.getBeginMonth());
            assertEquals(2012, sFis3.getBeginYear());
            assertEquals(2, sFis3.getEndMonth());
            assertEquals(2013, sFis3.getEndYear());
        } catch (NotLoggedInException e) {
            fail();
        }
    }
    
    @Test
    public void testAddCom() {
        
        String keyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            keyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            Com com = pm1.getObjectById(Com.class, KeyFactory.stringToKey(
                    keyString));
            
            assertEquals("com1", com.getName());
            assertEquals("a1", com.getAddress());
            assertEquals("tel1", com.getTelNo());
            assertEquals(ComType.GOVERN, com.getComType());
            assertEquals("338", com.getTaxID());
            assertEquals("ID", com.getMerchantID());
            assertEquals(YearType.INTER, com.getYearType());
            assertEquals(7.0, com.getVatRate(), 1);
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testEditCom() {
        String keyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            keyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            SCom sCom = new SCom(keyString, "com2", "a2", "tel2", ComType.LIMITED, "3228",
                    "ID2", YearType.THAI, 3.0, new Date());
            rpcService.editCom(session.getKeyString(), session.getSessionID(), sCom);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            Com com = pm1.getObjectById(Com.class, KeyFactory.stringToKey(
                    keyString));
            
            assertEquals("com2", com.getName());
            assertEquals("a2", com.getAddress());
            assertEquals("tel2", com.getTelNo());
            assertEquals(ComType.LIMITED, com.getComType());
            assertEquals("3228", com.getTaxID());
            assertEquals("ID2", com.getMerchantID());
            assertEquals(YearType.THAI, com.getYearType());
            assertEquals(3.0, com.getVatRate(), 1);
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testDeleteCom() {
        String keyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            keyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            rpcService.deleteCom(session.getKeyString(), session.getSessionID(),
                    keyString);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            pm1.getObjectById(Com.class, KeyFactory.stringToKey(
                    keyString));
            fail();
        } catch(JDOObjectNotFoundException e){
            
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testAddFis() {
        
        String fisKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            FiscalYear fis = pm1.getObjectById(FiscalYear.class, KeyFactory.stringToKey(
                    fisKeyString));
            assertEquals(2, fis.getBeginMonth());
            assertEquals(2012, fis.getBeginYear());
            assertEquals(1, fis.getEndMonth());
            assertEquals(2013, fis.getEndYear());
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testEditFis() {
        String fisKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            SFiscalYear sFis = new SFiscalYear(fisKeyString, 5, 2015, 4, 2016);
            rpcService.editFis(session.getKeyString(), session.getSessionID(),
                    sFis);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            FiscalYear fis = pm1.getObjectById(FiscalYear.class, KeyFactory.stringToKey(
                    fisKeyString));
            assertEquals(5, fis.getBeginMonth());
            assertEquals(2015, fis.getBeginYear());
            assertEquals(4, fis.getEndMonth());
            assertEquals(2016, fis.getEndYear());
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testDeleteFis() {
        String fisKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            rpcService.deleteFis(session.getKeyString(), session.getSessionID(),
                    fisKeyString);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            pm1.getObjectById(FiscalYear.class, KeyFactory.stringToKey(fisKeyString));
            fail();
        } catch(JDOObjectNotFoundException e){
            
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testGetSetup() {
        String fisKeyString = null;
        String journalTypeKeyString = null;
        String accGrpKeyString = null;
        String accChartKeyString1 = null;
        String accChartKeyString2 = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SJournalType sJournalType = new SJournalType(null, "name", "shortName", new Date());
            journalTypeKeyString = rpcService.addJournalType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournalType);

            SDocType sDocType = new SDocType(null, journalTypeKeyString, "pv",
                    "docname", "docdesc", new Date());
            rpcService.addDocType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sDocType);
            
            SAccGrp sAccGrp = new SAccGrp(null, "accgrpname", new Date());
            accGrpKeyString = rpcService.addAccGrp(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccGrp);

            SAccChart sAccChart1 = new SAccChart(null, accGrpKeyString, null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString1 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart1);
            
            SAccChart sAccChart2 = new SAccChart(null, accGrpKeyString, accChartKeyString1,
                    "1000111", "accchart2", AccType.ENTRY, 3, 111.34);
            accChartKeyString2 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart2);
            
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            SFiscalYear sFis = rpcService.getSetup(session.getKeyString(),
                    session.getSessionID(), fisKeyString);
            assertEquals(1, sFis.getSJournalTypeList().size());
            assertEquals(1, sFis.getSDocTypeList().size());
            assertEquals(1, sFis.getSAccGrpList().size());
            assertEquals(2, sFis.getSAccChartList().size());
            
            SJournalType sJT = sFis.getSJournalTypeList().get(0);
            assertEquals("name", sJT.getName());
            assertEquals("shortName", sJT.getShortName());
            
            SDocType sDT = sFis.getSDocTypeList().get(0);
            assertEquals(journalTypeKeyString, sDT.getJournalTypeKeyString());
            assertEquals("pv", sDT.getCode());
            assertEquals("docname", sDT.getName());
            assertEquals("docdesc", sDT.getJournalDesc());
            
            SAccGrp sAG = sFis.getSAccGrpList().get(0);
            assertEquals("accgrpname", sAG.getName());
            
            for (SAccChart sAC : sFis.getSAccChartList()) {
                if (sAC.getKeyString().equals(accChartKeyString1)) {
                    assertEquals(accGrpKeyString, sAC.getAccGroupKeyString());
                    assertEquals(null, sAC.getParentAccChartKeyString());
                    assertEquals("1000100", sAC.getNo());
                    assertEquals("accchart1", sAC.getName());
                    assertEquals(AccType.CONTROL, sAC.getType());
                    assertEquals(2, sAC.getLevel());
                    assertEquals(400.34, sAC.getBeginning(), 2);
                    continue;
                }
                if (sAC.getKeyString().equals(accChartKeyString2)) {
                    assertEquals(accGrpKeyString, sAC.getAccGroupKeyString());
                    assertEquals(accChartKeyString1, sAC.getParentAccChartKeyString());
                    assertEquals("1000111", sAC.getNo());
                    assertEquals("accchart2", sAC.getName());
                    assertEquals(AccType.ENTRY, sAC.getType());
                    assertEquals(3, sAC.getLevel());
                    assertEquals(111.34, sAC.getBeginning(), 2);
                    continue;
                }
                fail();
            }
        } catch (NotLoggedInException e) {
            fail();
        }
    }
    
    @Test
    public void testAddJournalType() {
        String journalTypeKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SJournalType sJournalType = new SJournalType(null, "name", "shortName", new Date());
            journalTypeKeyString = rpcService.addJournalType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournalType);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            JournalType jt = pm1.getObjectById(JournalType.class, KeyFactory.stringToKey(
                    journalTypeKeyString));
            assertEquals("name", jt.getName());
            assertEquals("shortName", jt.getShortName());
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testEditJournalType() {
        String journalTypeKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SJournalType sJournalType = new SJournalType(null, "name", "shortName", new Date());
            journalTypeKeyString = rpcService.addJournalType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournalType);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            SJournalType sJournalType = new SJournalType(journalTypeKeyString,
                    "name2", "shortName2", new Date());
            rpcService.editJournalType(session.getKeyString(), session.getSessionID(),
                    sJournalType);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            JournalType jt = pm1.getObjectById(JournalType.class, KeyFactory.stringToKey(
                    journalTypeKeyString));
            assertEquals("name2", jt.getName());
            assertEquals("shortName2", jt.getShortName());
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testDeleteJournalType() {
        String journalTypeKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SJournalType sJournalType = new SJournalType(null, "name", "shortName", new Date());
            journalTypeKeyString = rpcService.addJournalType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournalType);
        
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            rpcService.deleteJournalType(session.getKeyString(), session.getSessionID(),
                    journalTypeKeyString);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            pm1.getObjectById(JournalType.class, KeyFactory.stringToKey(
                    journalTypeKeyString));
            fail();
        } catch(JDOObjectNotFoundException e) {
            
        }finally {
            pm1.close();
        }
    }
    
    @Test
    public void testAddDocType() {
        String journalTypeKeyString = null;
        String docTypeKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SJournalType sJournalType = new SJournalType(null, "name", "shortName", new Date());
            journalTypeKeyString = rpcService.addJournalType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournalType);
            
            SDocType sDocType = new SDocType(null, journalTypeKeyString, "pv",
                    "docname", "docdesc", new Date());
            docTypeKeyString = rpcService.addDocType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sDocType);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            DocType jt = pm1.getObjectById(DocType.class, KeyFactory.stringToKey(
                    docTypeKeyString));
            assertEquals(journalTypeKeyString, jt.getJournalTypeKeyString());
            assertEquals("pv", jt.getCode());
            assertEquals("docname", jt.getName());
            assertEquals("docdesc", jt.getJournalDesc());
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testEditDocType() {
        String journalTypeKeyString = null;
        String docTypeKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SJournalType sJournalType = new SJournalType(null, "name", "shortName", new Date());
            journalTypeKeyString = rpcService.addJournalType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournalType);
            
            SDocType sDocType = new SDocType(null, journalTypeKeyString, "pv",
                    "docname", "docdesc", new Date());
            docTypeKeyString = rpcService.addDocType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sDocType);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            SDocType sDocType = new SDocType(docTypeKeyString, journalTypeKeyString, "pv2",
                    "docname2", "docdesc2", new Date());
            rpcService.editDocType(session.getKeyString(), session.getSessionID(),
                    sDocType);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            DocType jt = pm1.getObjectById(DocType.class, KeyFactory.stringToKey(
                    docTypeKeyString));
            assertEquals(journalTypeKeyString, jt.getJournalTypeKeyString());
            assertEquals("pv2", jt.getCode());
            assertEquals("docname2", jt.getName());
            assertEquals("docdesc2", jt.getJournalDesc());
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testDeleteDocType() {
        String journalTypeKeyString = null;
        String docTypeKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SJournalType sJournalType = new SJournalType(null, "name", "shortName", new Date());
            journalTypeKeyString = rpcService.addJournalType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournalType);
            
            SDocType sDocType = new SDocType(null, journalTypeKeyString, "pv",
                    "docname", "docdesc", new Date());
            docTypeKeyString = rpcService.addDocType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sDocType);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            rpcService.deleteDocType(session.getKeyString(), session.getSessionID(),
                    docTypeKeyString);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            pm1.getObjectById(DocType.class, KeyFactory.stringToKey(
                    docTypeKeyString));
            fail();
        } catch (JDOObjectNotFoundException e) {
            
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testAddAccGrp() {
        String accGrpKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SAccGrp sAccGrp = new SAccGrp(null, "accgrpname", new Date());
            accGrpKeyString = rpcService.addAccGrp(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccGrp);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            AccGroup ag = pm1.getObjectById(AccGroup.class, KeyFactory.stringToKey(
                    accGrpKeyString));
            assertEquals("accgrpname", ag.getName());
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testEditAccGrp() {
        String accGrpKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SAccGrp sAccGrp = new SAccGrp(null, "accgrpname", new Date());
            accGrpKeyString = rpcService.addAccGrp(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccGrp);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            SAccGrp sAccGrp = new SAccGrp(accGrpKeyString, "accgrpname2", new Date());
            rpcService.editAccGrp(session.getKeyString(), session.getSessionID(),
                    sAccGrp);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            AccGroup ag = pm1.getObjectById(AccGroup.class, KeyFactory.stringToKey(
                    accGrpKeyString));
            assertEquals("accgrpname2", ag.getName());
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testDeleteAccGrp() {
        String accGrpKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SAccGrp sAccGrp = new SAccGrp(null, "accgrpname", new Date());
            accGrpKeyString = rpcService.addAccGrp(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccGrp);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            rpcService.deleteAccGrp(session.getKeyString(), session.getSessionID(),
                    accGrpKeyString);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            pm1.getObjectById(AccGroup.class, KeyFactory.stringToKey(
                    accGrpKeyString));
            fail();
        } catch (JDOObjectNotFoundException e) {
            
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testAddAccChart() {
        String accChartKeyString1 = null;        
        String accChartKeyString2 = null;
        String accGrpKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SAccGrp sAccGrp = new SAccGrp(null, "accgrpname", new Date());
            accGrpKeyString = rpcService.addAccGrp(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccGrp);
            
            SAccChart sAccChart1 = new SAccChart(null, accGrpKeyString, null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString1 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart1);
            
            SAccChart sAccChart2 = new SAccChart(null, accGrpKeyString, accChartKeyString1,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString2 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart2);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            AccChart ac = pm1.getObjectById(AccChart.class, KeyFactory.stringToKey(
                    accChartKeyString2));
            assertEquals(accGrpKeyString, ac.getAccGroupKeyString());
            assertEquals(accChartKeyString1, ac.getParentAccChartKeyString());
            assertEquals("1000100", ac.getNo());
            assertEquals("accchart1", ac.getName());
            assertEquals(AccType.CONTROL, ac.getType());
            assertEquals(2, ac.getLevel());
            assertEquals(400.34, ac.getBeginning(), 2);
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testEditAccChart() {
        String fisKeyString = null;
        String accChartKeyString1 = null;        
        String accChartKeyString2 = null;
        String accGrpKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SAccGrp sAccGrp = new SAccGrp(null, "accgrpname", new Date());
            accGrpKeyString = rpcService.addAccGrp(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccGrp);
            
            SAccChart sAccChart1 = new SAccChart(null, accGrpKeyString, null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString1 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart1);
            
            SAccChart sAccChart2 = new SAccChart(null, accGrpKeyString, accChartKeyString1,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString2 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart2);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        String accChartKeyString3 = null;
        try {
            
            SAccChart sAccChart3 = new SAccChart(null, accGrpKeyString, accChartKeyString1,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString3 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart3);
            
            SAccChart sAccChart4 = new SAccChart(accChartKeyString2, accGrpKeyString, accChartKeyString3,
                    "1000111", "accchart4", AccType.ENTRY, 5, 232.34);
            rpcService.editAccChart(session.getKeyString(), session.getSessionID(),
                    sAccChart4);
        } catch (NotLoggedInException e) {
            
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            AccChart ac = pm1.getObjectById(AccChart.class, KeyFactory.stringToKey(
                    accChartKeyString2));
            assertEquals(accGrpKeyString, ac.getAccGroupKeyString());
            assertEquals(accChartKeyString3, ac.getParentAccChartKeyString());
            assertEquals("1000111", ac.getNo());
            assertEquals("accchart4", ac.getName());
            assertEquals(AccType.ENTRY, ac.getType());
            assertEquals(5, ac.getLevel());
            assertEquals(232.34, ac.getBeginning(), 2);
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testDeleteAccChart() {
        String accChartKeyString1 = null;        
        String accChartKeyString2 = null;
        String accGrpKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SAccGrp sAccGrp = new SAccGrp(null, "accgrpname", new Date());
            accGrpKeyString = rpcService.addAccGrp(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccGrp);
            
            SAccChart sAccChart1 = new SAccChart(null, accGrpKeyString, null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString1 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart1);
            
            SAccChart sAccChart2 = new SAccChart(null, accGrpKeyString, accChartKeyString1,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString2 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart2);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            rpcService.deleteAccChart(session.getKeyString(), session.getSessionID(),
                    accChartKeyString2);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            pm1.getObjectById(AccChart.class, KeyFactory.stringToKey(
                    accChartKeyString2));
            fail();
        } catch (JDOObjectNotFoundException e) {
            
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testDeleteFinHeader() {
        String finHeaderKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SFinHeader sFinHeader = new SFinHeader(null, "finheadername", new Date());
            finHeaderKeyString = rpcService.addFinHeader(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sFinHeader);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            rpcService.deleteFinHeader(session.getKeyString(), session.getSessionID(),
                    finHeaderKeyString);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            pm1.getObjectById(FinHeader.class, KeyFactory.stringToKey(
                    finHeaderKeyString));
            fail();
        } catch (JDOObjectNotFoundException e) {
            
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testDeleteFinItem() {
        String finItemKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SFinHeader sFinHeader = new SFinHeader(null, "finheadername", new Date());
            String finHeaderKeyString = rpcService.addFinHeader(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sFinHeader);
            
            SFinItem sFinItem = new SFinItem(null, 3, Comm.ACCNO, "arg", CalCon.CAL,
                    PrintCon.PRINT, PrintStyle.BLANK, Operand.CLEAR, Operand.CLEAR,
                    Operand.MINUS, Operand.PLUS);
            finItemKeyString = rpcService.addFinItem(session.getKeyString(),
                    session.getSessionID(), finHeaderKeyString, sFinItem);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            rpcService.deleteFinItem(session.getKeyString(), session.getSessionID(),
                    finItemKeyString);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            pm1.getObjectById(FinItem.class, KeyFactory.stringToKey(
                    finItemKeyString));
            fail();
        } catch (JDOObjectNotFoundException e) {
            
        } finally {
            pm1.close();
        }
    }
    
    @Test
    public void testGetJournalList() {
        String fisKeyString = null;
        String journalKeyString = null;
        String docTypeKeyString = null;
        String accChartKeyString1 = null;
        String accChartKeyString2 = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SJournalType sJournalType = new SJournalType(null, "name", "shortName", new Date());
            String journalTypeKeyString = rpcService.addJournalType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournalType);

            SDocType sDocType = new SDocType(null, journalTypeKeyString, "pv",
                    "docname", "docdesc", new Date());
            docTypeKeyString = rpcService.addDocType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sDocType);
            
            SAccGrp sAccGrp = new SAccGrp(null, "accgrpname", new Date());
            String accGrpKeyString = rpcService.addAccGrp(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccGrp);

            SAccChart sAccChart1 = new SAccChart(null, accGrpKeyString, null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString1 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart1);
            
            SAccChart sAccChart2 = new SAccChart(null, accGrpKeyString, accChartKeyString1,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            accChartKeyString2 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart2);
            
            SJournalItem sJournalItem1 = new SJournalItem(null, accChartKeyString1,
                    500.02, new Date());
            
            SJournalItem sJournalItem2 = new SJournalItem(null, accChartKeyString2,
                    156.02, new Date());
            
            SJournalHeader sJournal = new SJournalHeader(null, docTypeKeyString,
                    "no", 25, 11, 2555, "desc");
            sJournal.addItem(sJournalItem1);
            sJournal.addItem(sJournalItem2);
            
            journalKeyString = rpcService.addJournal(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournal);
            
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            SFiscalYear journalList = rpcService.getJournalList(
                    session.getKeyString(), session.getSessionID(), fisKeyString);
            
            assertEquals(1, journalList.getSJournalList().size());
            SJournalHeader sJH = journalList.getSJournalList().get(0);
            assertEquals(journalKeyString, sJH.getKeyString());
            assertEquals(docTypeKeyString, sJH.getDocTypeKeyString());
            assertEquals("no", sJH.getNo());
            assertEquals(25, sJH.getDay());
            assertEquals(11, sJH.getMonth());
            assertEquals(2555, sJH.getYear());
            
            for (SJournalItem sJI : sJH.getItemList()) {
                if (sJI.getAccChartKeyString().equals(accChartKeyString1)
                        && sJI.getAmt() == 500.02) {
                    continue;
                }
                if (sJI.getAccChartKeyString().equals(accChartKeyString2)
                        && sJI.getAmt() == 156.02) {
                    continue;
                }
                fail();
            }
        } catch (NotLoggedInException e) {
            fail();
        }
    }
    
    @Test
    public void testDeleteJournal() {
        String journalKeyString = null;
        try {
            SCom sCom = new SCom(null, "com1", "a1", "tel1", ComType.GOVERN, "338",
                    "ID", YearType.INTER, 7.0, new Date());
            String comKeyString = rpcService.addCom(session.getKeyString(),
                    session.getSessionID(), sCom);
            
            SFiscalYear sFis = new SFiscalYear(null, 2, 2012, 1, 2013);
            String fisKeyString = rpcService.addFis(session.getKeyString(),
                    session.getSessionID(), comKeyString,
                    SConstants.ADD_FIS_WITH_NO_SETUP, sFis);
            
            SJournalType sJournalType = new SJournalType(null, "name", "shortName", new Date());
            String journalTypeKeyString = rpcService.addJournalType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournalType);

            SDocType sDocType = new SDocType(null, journalTypeKeyString, "pv",
                    "docname", "docdesc", new Date());
            String docTypeKeyString = rpcService.addDocType(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sDocType);
            
            SAccGrp sAccGrp = new SAccGrp(null, "accgrpname", new Date());
            String accGrpKeyString = rpcService.addAccGrp(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccGrp);

            SAccChart sAccChart1 = new SAccChart(null, accGrpKeyString, null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            String accChartKeyString1 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart1);
            
            SAccChart sAccChart2 = new SAccChart(null, accGrpKeyString, accChartKeyString1,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
            String accChartKeyString2 = rpcService.addAccChart(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sAccChart2);
            
            SJournalItem sJournalItem1 = new SJournalItem(null, accChartKeyString1,
                    500.02, new Date());
            
            SJournalItem sJournalItem2 = new SJournalItem(null, accChartKeyString2,
                    156.02, new Date());
            
            SJournalHeader sJournal = new SJournalHeader(null, docTypeKeyString,
                    "no", 25, 11, 2555, "desc");
            sJournal.addItem(sJournalItem1);
            sJournal.addItem(sJournalItem2);
            
            journalKeyString = rpcService.addJournal(session.getKeyString(),
                    session.getSessionID(), fisKeyString, sJournal);
            
        } catch (NotLoggedInException e) {
            fail();
        }
        
        try {
            rpcService.deleteJournal(session.getKeyString(), session.getSessionID(),
                    journalKeyString);
        } catch (NotLoggedInException e) {
            fail();
        }
        
        PersistenceManager pm1 = PMF.get().getPersistenceManager();
        try {
            pm1.getObjectById(JournalHeader.class, KeyFactory.stringToKey(
                    journalKeyString));
            fail();
        } catch (JDOObjectNotFoundException e) {
            
        } finally {
            pm1.close();
        }
    }
}
