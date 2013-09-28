package gwt.shared.model;

import static org.junit.Assert.assertEquals;
import gwt.shared.model.SAccChart.AccType;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class SFiscalYearTest {
    
    private SFiscalYear sFis;
    
    @Before
    public void setUp() {
        sFis = new SFiscalYear();
    }

    @Test
    public void testSetSetup() {
        
        SJournalType sJT = new SJournalType("jtKS", "name", "shortName",
                new Date());
        SDocType sDT = new SDocType("dtKS", "jtKS", "pv",
                    "docname", "docdesc", new Date());
        SAccGrp sAG = new SAccGrp("agKS", "accgrpname", new Date());
        
        SAccChart sAC1 = new SAccChart("acKS1", "agKS", null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
        SAccChart sAC2 = new SAccChart("acKS2", "agKS", "acKS1",
                    "1000111", "accchart2", AccType.ENTRY, 3, 111.34);
        
        SFiscalYear sFY = new SFiscalYear();
        sFY.addSJournalType(sJT);
        sFY.addSDocType(sDT);
        sFY.addSAccGrp(sAG);
        sFY.addSAccChart(sAC1);
        sFY.addSAccChart(sAC2);
        
        sFis.setSetup(sFY);
        
        assertEquals(1, sFis.getSJournalTypeList().size());
        assertEquals(1, sFis.getSDocTypeList().size());
        assertEquals(1, sFis.getSAccGrpList().size());
        assertEquals(2, sFis.getSAccChartList().size());
        
        SJournalType sJT_tmp = sFis.getSJournalType("jtKS");
        assertEquals("name", sJT_tmp.getName());
        assertEquals("shortName", sJT_tmp.getShortName());
        
        SDocType sDT_tmp = sFis.getSDocType("dtKS");
        assertEquals("jtKS", sDT_tmp.getJournalTypeKeyString());
        assertEquals("pv", sDT_tmp.getCode());
        assertEquals("docname", sDT_tmp.getName());
        assertEquals("docdesc", sDT_tmp.getJournalDesc());
        
        SAccGrp sAG_tmp = sFis.getSAccGrp("agKS");
        assertEquals("accgrpname", sAG_tmp.getName());
        
        SAccChart sAC1_tmp = sFis.getSAccChart("acKS1");
        assertEquals("agKS", sAC1_tmp.getAccGroupKeyString());
        assertEquals(null, sAC1_tmp.getParentAccChartKeyString());
        assertEquals("1000100", sAC1_tmp.getNo());
        assertEquals("accchart1", sAC1_tmp.getName());
        assertEquals(AccType.CONTROL, sAC1_tmp.getType());
        assertEquals(2, sAC1_tmp.getLevel());
        assertEquals(400.34, sAC1_tmp.getBeginning(), 2);
        
        SAccChart sAC2_tmp = sFis.getSAccChart("acKS2");
        assertEquals("agKS", sAC2_tmp.getAccGroupKeyString());
        assertEquals("acKS1", sAC2_tmp.getParentAccChartKeyString());
        assertEquals("1000111", sAC2_tmp.getNo());
        assertEquals("accchart2", sAC2_tmp.getName());
        assertEquals(AccType.ENTRY, sAC2_tmp.getType());
        assertEquals(3, sAC2_tmp.getLevel());
        assertEquals(111.34, sAC2_tmp.getBeginning(), 2);
        
    }
    
    @Test
    public void testUpdateSDocTypeJournalTypeShortName1() {
        SJournalType sJT = new SJournalType("jtKS", "name", "shortName",
                new Date());
        SDocType sDT = new SDocType("dtKS", "jtKS", "pv",
                    "docname", "docdesc", new Date());
        
        SFiscalYear sFY = new SFiscalYear();
        sFY.addSJournalType(sJT);
        sFY.addSDocType(sDT);

        sFis.setSetup(sFY);
        
        sFis.updateSDocTypeJournalTypeShortName();
        
        SDocType sDT_tmp = sFis.getSDocType("dtKS");
        assertEquals("jtKS", sDT_tmp.getJournalTypeKeyString());
        assertEquals("shortName", sDT_tmp.getJournalTypeShortName());
        
    }
    
    @Test
    public void testUpdateSDocTypeJournalTypeShortName2() {
        SJournalType sJT = new SJournalType("jtKS", "name", "shortName",
                new Date());
        SDocType sDT = new SDocType("dtKS", "jtKS", "pv",
                    "docname", "docdesc", new Date());
        
        SFiscalYear sFY = new SFiscalYear();
        sFY.addSJournalType(sJT);
        sFY.addSDocType(sDT);

        sFis.setSetup(sFY);
        
        sFis.updateSDocTypeJournalTypeShortName(sDT);
        
        assertEquals("jtKS", sDT.getJournalTypeKeyString());
        assertEquals("shortName", sDT.getJournalTypeShortName());
        
        SDocType sDT_tmp = sFis.getSDocType("dtKS");
        assertEquals("jtKS", sDT_tmp.getJournalTypeKeyString());
        assertEquals("shortName", sDT_tmp.getJournalTypeShortName());
    }
    
    @Test
    public void testUpdateSAccChartAccGrpName1() {
        SAccGrp sAG = new SAccGrp("agKS", "accgrpname", new Date());
        
        SAccChart sAC1 = new SAccChart("acKS1", "agKS", null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
        SAccChart sAC2 = new SAccChart("acKS2", "agKS", "acKS1",
                    "1000111", "accchart2", AccType.ENTRY, 3, 111.34);
        
        SFiscalYear sFY = new SFiscalYear();
        sFY.addSAccGrp(sAG);
        sFY.addSAccChart(sAC1);
        sFY.addSAccChart(sAC2);
        
        sFis.setSetup(sFY);
        
        sFis.updateSAccChartAccGrpName();
        
        SAccChart sAC1_tmp = sFis.getSAccChart("acKS1");
        assertEquals("agKS", sAC1_tmp.getAccGroupKeyString());
        assertEquals("accgrpname", sAC1_tmp.getAccGroupName());
        
        
        SAccChart sAC2_tmp = sFis.getSAccChart("acKS2");
        assertEquals("agKS", sAC2_tmp.getAccGroupKeyString());
        assertEquals("accgrpname", sAC2_tmp.getAccGroupName());
    }
    
    @Test
    public void testUpdateSAccChartAccGrpName2() {
        SAccGrp sAG = new SAccGrp("agKS", "accgrpname", new Date());
        
        SAccChart sAC1 = new SAccChart("acKS1", "agKS", null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
        SAccChart sAC2 = new SAccChart("acKS2", "agKS", "acKS1",
                    "1000111", "accchart2", AccType.ENTRY, 3, 111.34);
        
        SFiscalYear sFY = new SFiscalYear();
        sFY.addSAccGrp(sAG);
        sFY.addSAccChart(sAC1);
        sFY.addSAccChart(sAC2);
        
        sFis.setSetup(sFY);
        
        sFis.updateSAccChartAccGrpName(sAC1);
        
        assertEquals("accgrpname", sAC1.getAccGroupName());
        
        SAccChart sAC1_tmp = sFis.getSAccChart("acKS1");
        assertEquals("agKS", sAC1_tmp.getAccGroupKeyString());
        assertEquals("accgrpname", sAC1_tmp.getAccGroupName());
        
        SAccChart sAC2_tmp = sFis.getSAccChart("acKS2");
        assertEquals("agKS", sAC2_tmp.getAccGroupKeyString());
        assertEquals(null, sAC2_tmp.getAccGroupName());
    }
    
    @Test
    public void testUpdateSAccChartParentAccChartNo1() {
        SAccGrp sAG = new SAccGrp("agKS", "accgrpname", new Date());
        
        SAccChart sAC1 = new SAccChart("acKS1", "agKS", null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
        SAccChart sAC2 = new SAccChart("acKS2", "agKS", "acKS1",
                    "1000111", "accchart2", AccType.ENTRY, 3, 111.34);
        
        SFiscalYear sFY = new SFiscalYear();
        sFY.addSAccGrp(sAG);
        sFY.addSAccChart(sAC1);
        sFY.addSAccChart(sAC2);
        
        sFis.setSetup(sFY);
        
        sFis.updateSAccChartParentAccChartNo();
        
        SAccChart sAC1_tmp = sFis.getSAccChart("acKS1");
        assertEquals(null, sAC1_tmp.getParentAccChartNo());
        
        SAccChart sAC2_tmp = sFis.getSAccChart("acKS2");
        assertEquals("1000100", sAC2_tmp.getParentAccChartNo());
    }
    
    @Test
    public void testUpdateSAccChartParentAccChartNo2() {
        SAccGrp sAG = new SAccGrp("agKS", "accgrpname", new Date());
        
        SAccChart sAC1 = new SAccChart("acKS1", "agKS", null,
                    "1000100", "accchart1", AccType.CONTROL, 2, 400.34);
        SAccChart sAC2 = new SAccChart("acKS2", "agKS", "acKS1",
                    "1000111", "accchart2", AccType.ENTRY, 3, 111.34);
        
        SFiscalYear sFY = new SFiscalYear();
        sFY.addSAccGrp(sAG);
        sFY.addSAccChart(sAC1);
        sFY.addSAccChart(sAC2);
        
        sFis.setSetup(sFY);
        
        sFis.updateSAccChartParentAccChartNo(sAC2);
        
        assertEquals("1000100", sAC2.getParentAccChartNo());
        
        SAccChart sAC1_tmp = sFis.getSAccChart("acKS1");
        assertEquals(null, sAC1_tmp.getParentAccChartNo());
        
        SAccChart sAC2_tmp = sFis.getSAccChart("acKS2");
        assertEquals("1000100", sAC2_tmp.getParentAccChartNo());
    }
    
    @Test
    public void testSortAccChartList() {
        SAccGrp sAG1 = new SAccGrp("agKS1", "accgrpname1", new Date());
        SAccGrp sAG2 = new SAccGrp("agKS2", "accgrpname2", new Date());
        
        SAccChart sAC1 = new SAccChart("acKS1", sAG1.getKeyString(), "acKS4",
                    "1000100", "accchart1", AccType.CONTROL, 1, 400.34);
        SAccChart sAC2 = new SAccChart("acKS2", sAG2.getKeyString(), "acKS5",
                    "1000111", "accchart2", AccType.ENTRY, 1, 111.34);
        SAccChart sAC3 = new SAccChart("acKS3", sAG2.getKeyString(), "acKS5",
                    "1000112", "accchart3", AccType.ENTRY, 1, 111.34);
        SAccChart sAC4 = new SAccChart("acKS4", sAG1.getKeyString(), null,
                    "1000113", "accchart4", AccType.ENTRY, 0, 111.34);
        SAccChart sAC5 = new SAccChart("acKS5", sAG2.getKeyString(), null,
                    "1000141", "accchart5", AccType.ENTRY, 0, 111.34);
        
        SFiscalYear sFY = new SFiscalYear();
        sFY.addSAccGrp(sAG1);
        sFY.addSAccGrp(sAG2);
        sFY.addSAccChart(sAC1);
        sFY.addSAccChart(sAC2);
        sFY.addSAccChart(sAC3);
        sFY.addSAccChart(sAC4);
        sFY.addSAccChart(sAC5);
        
        sFis.setSetup(sFY);
        
        sFis.sortSAccChartList();
        
        assertEquals("acKS4", sFis.getSAccChartList().get(0).getKeyString());
        assertEquals("acKS1", sFis.getSAccChartList().get(1).getKeyString());
        assertEquals("acKS5", sFis.getSAccChartList().get(2).getKeyString());
        assertEquals("acKS2", sFis.getSAccChartList().get(3).getKeyString());
        assertEquals("acKS3", sFis.getSAccChartList().get(4).getKeyString());
    }
    
    /*@Test
    public void testAddSJournal() {
        
        SJournalHeader sJournal1 = new SJournalHeader("jh1", "dt1", "no", 25,
                11, 2555, "desc");
        SJournalHeader sJournal2 = new SJournalHeader("jh2", "dt1", "no", 24,
                11, 2555, "desc");
        SJournalHeader sJournal3 = new SJournalHeader("jh3", "dt1", "no", 25,
                11, 2554, "desc");
        SJournalHeader sJournal4 = new SJournalHeader("jh4", "dt1", "no", 25,
                10, 2554, "desc");
        SJournalHeader sJournal5 = new SJournalHeader("jh5", "dt1", "no", 25,
                11, 2554, "desc");
        sFis.addSJournal(sJournal1);
        sFis.addSJournal(sJournal2);
        sFis.addSJournal(sJournal3);
        sFis.addSJournal(sJournal4);
        sFis.addSJournal(sJournal5);
        
        assertEquals("jh4", sFis.getSJournalList().get(0).getKeyString());
        assertEquals("jh3", sFis.getSJournalList().get(1).getKeyString());
        assertEquals("jh5", sFis.getSJournalList().get(2).getKeyString());
        assertEquals("jh2", sFis.getSJournalList().get(3).getKeyString());
        assertEquals("jh1", sFis.getSJournalList().get(4).getKeyString());
    }
    
    @Test
    public void testEditJournal() {
        SJournalHeader sJournal1 = new SJournalHeader("jh1", "dt1", "no", 25,
                11, 2555, "desc");
        SJournalHeader sJournal2 = new SJournalHeader("jh2", "dt1", "no", 24,
                11, 2555, "desc");
        SJournalHeader sJournal3 = new SJournalHeader("jh3", "dt1", "no", 25,
                11, 2554, "desc");
        SJournalHeader sJournal4 = new SJournalHeader("jh4", "dt1", "no", 25,
                10, 2554, "desc");
        SJournalHeader sJournal5 = new SJournalHeader("jh5", "dt1", "no", 25,
                11, 2554, "desc");
        sFis.addSJournal(sJournal1);
        sFis.addSJournal(sJournal2);
        sFis.addSJournal(sJournal3);
        sFis.addSJournal(sJournal4);
        sFis.addSJournal(sJournal5);
        
        sJournal4.setMonth(12);
        sFis.editSJournal(sJournal4);
        
        assertEquals("jh3", sFis.getSJournalList().get(0).getKeyString());
        assertEquals("jh5", sFis.getSJournalList().get(1).getKeyString());
        assertEquals("jh4", sFis.getSJournalList().get(2).getKeyString());
        assertEquals("jh2", sFis.getSJournalList().get(3).getKeyString());
        assertEquals("jh1", sFis.getSJournalList().get(4).getKeyString());
    }
    
    @Test
    public void testUpdateSJournalJournalTypeShortName1() {
        
        SJournalType sJT = new SJournalType("jtKS", "name", "shortName",
                new Date());
        SDocType sDT = new SDocType("dtKS", "jtKS", "pv",
                    "docname", "docdesc", new Date());

        SJournalHeader sJournal1 = new SJournalHeader("jh1", "dtKS", "no", 25,
                11, 2555, "desc");
        SJournalHeader sJournal2 = new SJournalHeader("jh2", "dtKS", "no", 24,
                11, 2555, "desc");

        sFis.addSJournalType(sJT);
        sFis.addSDocType(sDT);
        sFis.addSJournal(sJournal1);
        sFis.addSJournal(sJournal2);
        
        sFis.updateSJournalJournalTypeShortName();
        
        assertEquals("shortName", sFis.getSJournal("jh1").getJournalTypeShortName());
        assertEquals("shortName", sFis.getSJournal("jh2").getJournalTypeShortName());
    }
    
    @Test
    public void testUpdateSJournalJournalTypeShortName2() {
        
        SJournalType sJT = new SJournalType("jtKS", "name", "shortName",
                new Date());
        SDocType sDT = new SDocType("dtKS", "jtKS", "pv",
                    "docname", "docdesc", new Date());

        SJournalHeader sJournal1 = new SJournalHeader("jh1", "dtKS", "no", 25,
                11, 2555, "desc");
        SJournalHeader sJournal2 = new SJournalHeader("jh2", "dtKS", "no", 24,
                11, 2555, "desc");

        sFis.addSJournalType(sJT);
        sFis.addSDocType(sDT);
        sFis.addSJournal(sJournal1);
        sFis.addSJournal(sJournal2);
        
        sFis.updateSJournalJournalTypeShortName(sJournal1);
        
        assertEquals("shortName", sJournal1.getJournalTypeShortName());
        
        assertEquals("shortName", sFis.getSJournal("jh1").getJournalTypeShortName());
        assertEquals(null, sFis.getSJournal("jh2").getJournalTypeShortName());
    }
    
    @Test
    public void testUpdateSJournalDocTypeCode1() {
        
        SDocType sDT = new SDocType("dtKS", "jtKS", "pv",
                    "docname", "docdesc", new Date());

        SJournalHeader sJournal1 = new SJournalHeader("jh1", "dtKS", "no", 25,
                11, 2555, "desc");
        SJournalHeader sJournal2 = new SJournalHeader("jh2", "dtKS", "no", 24,
                11, 2555, "desc");

        sFis.addSDocType(sDT);
        sFis.addSJournal(sJournal1);
        sFis.addSJournal(sJournal2);
        
        sFis.updateSJournalDocTypeCode();
        
        assertEquals("pv", sFis.getSJournal("jh1").getDocTypeCode());
        assertEquals("pv", sFis.getSJournal("jh2").getDocTypeCode());
    }
    
    @Test
    public void testUpdateSJournalDocTypeCode2() {
        
        SDocType sDT = new SDocType("dtKS", "jtKS", "pv",
                    "docname", "docdesc", new Date());

        SJournalHeader sJournal1 = new SJournalHeader("jh1", "dtKS", "no", 25,
                11, 2555, "desc");
        SJournalHeader sJournal2 = new SJournalHeader("jh2", "dtKS", "no", 24,
                11, 2555, "desc");

        sFis.addSDocType(sDT);
        sFis.addSJournal(sJournal1);
        sFis.addSJournal(sJournal2);
        
        sFis.updateSJournalDocTypeCode(sJournal1);
        
        assertEquals("pv", sJournal1.getDocTypeCode());
        
        assertEquals("pv", sFis.getSJournal("jh1").getDocTypeCode());
        assertEquals(null, sFis.getSJournal("jh2").getDocTypeCode());
    }
    
    @Test
    public void testUpdateSJournalItemAccChartNo1() {
        
        SAccChart sAC1 = new SAccChart("acKS1", "ag1", null,
                "1000100", "accchart1", AccType.CONTROL, 1, 400.34);
        
        SJournalItem sJI1 = new SJournalItem("sJI1", sAC1.getKeyString(),
                    500.02, new Date());
            
        SJournalItem sJI2 = new SJournalItem("sJI2", sAC1.getKeyString(),
                156.02, new Date());
        
        SJournalHeader sJH = new SJournalHeader("sJH", "dtKS",
                "no", 25, 11, 2555, "desc");
        sJH.addItem(sJI1);
        sJH.addItem(sJI2);
        
        sFis.addSAccChart(sAC1);
        sFis.addSJournal(sJH);
        
        sFis.updateSJournalItemAccChartNo();
        
        SJournalHeader sJH_tmp = sFis.getSJournal("sJH");
        assertEquals("1000100", sJH_tmp.getItemList().get(0).getAccChartNo());
        assertEquals("1000100", sJH_tmp.getItemList().get(1).getAccChartNo());
    }
    
    @Test
    public void testUpdateSJournalItemAccChartNo2() {
        
        SAccChart sAC1 = new SAccChart("acKS1", "ag1", null,
                "1000100", "accchart1", AccType.CONTROL, 1, 400.34);
        
        SJournalItem sJI1 = new SJournalItem("sJI1", sAC1.getKeyString(),
                    500.02, new Date());
            
        SJournalItem sJI2 = new SJournalItem("sJI2", sAC1.getKeyString(),
                156.02, new Date());
        
        SJournalHeader sJH1 = new SJournalHeader("sJH1", "dtKS",
                "no", 25, 11, 2555, "desc");
        sJH1.addItem(sJI1);
        
        SJournalHeader sJH2 = new SJournalHeader("sJH2", "dtKS",
                "no", 25, 11, 2555, "desc");
        sJH2.addItem(sJI2);
        
        sFis.addSAccChart(sAC1);
        sFis.addSJournal(sJH1);
        sFis.addSJournal(sJH2);
        
        sFis.updateSJournalItemAccChartNo(sJH1);
        
        assertEquals("1000100", sJH1.getItemList().get(0).getAccChartNo());
        
        SJournalHeader sJH1_tmp = sFis.getSJournal("sJH1");
        assertEquals("1000100", sJH1_tmp.getItemList().get(0).getAccChartNo());
        SJournalHeader sJH2_tmp = sFis.getSJournal("sJH2");
        assertEquals(null, sJH2_tmp.getItemList().get(0).getAccChartNo());
    }*/
}
