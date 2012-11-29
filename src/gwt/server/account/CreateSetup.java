package gwt.server.account;

import gwt.server.account.Db.DbAddCallback;
import gwt.server.account.model.AccChart;
import gwt.server.account.model.AccGroup;
import gwt.server.account.model.DocType;
import gwt.server.account.model.FinHeader;
import gwt.server.account.model.JournalType;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;

import java.util.Date;

import javax.jdo.PersistenceManager;

public class CreateSetup {
    
    @SuppressWarnings("unused")
    public static void createInEnglish(PersistenceManager pm, final String fisKeyString) {
        Date d = new Date();
        
        Db<JournalType> jTDb = new Db<JournalType>();
        JournalType gJT = addJT(jTDb, pm, fisKeyString, "general", "ge", d);
        JournalType pJT = addJT(jTDb, pm, fisKeyString, "pay", "pay", d);
        JournalType rJT = addJT(jTDb, pm, fisKeyString, "receive", "rec", d);
        JournalType sJT = addJT(jTDb, pm, fisKeyString, "sale", "sale", d);
        JournalType bJT = addJT(jTDb, pm, fisKeyString, "buy", "buy", d);
        
        Db<DocType> dTDb = new Db<DocType>();
        DocType gDT = addDT(dTDb, pm, fisKeyString, gJT.getKeyString(), "JV", "general", "", d);
        DocType pDT = addDT(dTDb, pm, fisKeyString, pJT.getKeyString(), "PV", "pay", "", d);
        DocType rDT = addDT(dTDb, pm, fisKeyString, rJT.getKeyString(), "RV", "receive", "", d);
        DocType sDT = addDT(dTDb, pm, fisKeyString, sJT.getKeyString(), "SV", "sale", "", d);
        DocType bDT = addDT(dTDb, pm, fisKeyString, bJT.getKeyString(), "UV", "buy", "", d);
        
        Db<AccGroup> aGDb = new Db<AccGroup>();
        AccGroup wAG = addAG(aGDb, pm, fisKeyString, "asset", d);
        AccGroup dAG = addAG(aGDb, pm, fisKeyString, "debt", d);
        AccGroup fAG = addAG(aGDb, pm, fisKeyString, "budget", d);
        AccGroup rAG = addAG(aGDb, pm, fisKeyString, "income", d);
        AccGroup eAG = addAG(aGDb, pm, fisKeyString, "expense", d);
        
        Db<AccChart> aCDb = new Db<AccChart>();
        AccChart aC1 = addAC(aCDb, pm, fisKeyString, "10-00-00-00", "assets", wAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC2 = addAC(aCDb, pm, fisKeyString, "11-00-00-00", "live assets", wAG.getKeyString(), 2, AccType.CONTROL, aC1.getKeyString());
        AccChart aC3 = addAC(aCDb, pm, fisKeyString, "11-01-00-00", "cash and savings", wAG.getKeyString(), 3, AccType.CONTROL, aC2.getKeyString());
        AccChart aC4 = addAC(aCDb, pm, fisKeyString, "11-01-01-00", "cash", wAG.getKeyString(), 4, AccType.ENTRY, aC3.getKeyString());
        AccChart aC5 = addAC(aCDb, pm, fisKeyString, "11-01-02-00", "current accounts", wAG.getKeyString(), 4, AccType.CONTROL, aC3.getKeyString());
        AccChart aC6 = addAC(aCDb, pm, fisKeyString, "11-01-02-01", "current account no. 999-9-9999-1", wAG.getKeyString(), 5, AccType.ENTRY, aC5.getKeyString());
        AccChart aC9 = addAC(aCDb, pm, fisKeyString, "11-01-03-00", "saving accounts", wAG.getKeyString(), 4, AccType.CONTROL, aC3.getKeyString());
        AccChart aC10 = addAC(aCDb, pm, fisKeyString, "11-01-03-01", "saving accounts no. 999-9-9999-1", wAG.getKeyString(), 5, AccType.ENTRY, aC9.getKeyString());
        AccChart aC22 = addAC(aCDb, pm, fisKeyString, "11-04-00-00", "remaining products", wAG.getKeyString(), 3, AccType.CONTROL, aC2.getKeyString());
        AccChart aC23 = addAC(aCDb, pm, fisKeyString, "11-04-01-00", "remaining materials", wAG.getKeyString(), 4, AccType.ENTRY, aC22.getKeyString());
        AccChart aC26 = addAC(aCDb, pm, fisKeyString, "11-05-00-00", "other assets", wAG.getKeyString(), 3, AccType.CONTROL, aC2.getKeyString());
        AccChart aC27 = addAC(aCDb, pm, fisKeyString, "11-05-01-00", "advanced expenses", wAG.getKeyString(), 4, AccType.CONTROL, aC26.getKeyString());
        AccChart aC28 = addAC(aCDb, pm, fisKeyString, "11-05-01-01", "advanced expenses-products", wAG.getKeyString(), 5, AccType.ENTRY, aC27.getKeyString());
        AccChart aC29 = addAC(aCDb, pm, fisKeyString, "11-05-01-02", "advanced tax", wAG.getKeyString(), 5, AccType.ENTRY, aC27.getKeyString());
        AccChart aC30 = addAC(aCDb, pm, fisKeyString, "11-05-01-03", "others advanced expenses", wAG.getKeyString(), 5, AccType.ENTRY, aC27.getKeyString());
        
        AccChart aC57 = addAC(aCDb, pm, fisKeyString, "20-00-00-00", "debts", dAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC58 = addAC(aCDb, pm, fisKeyString, "21-00-00-00", "live debts", dAG.getKeyString(), 2, AccType.CONTROL, aC57.getKeyString());
        AccChart aC59 = addAC(aCDb, pm, fisKeyString, "21-02-00-00", "creditors and checks", dAG.getKeyString(), 3, AccType.CONTROL, aC58.getKeyString());
        AccChart aC60 = addAC(aCDb, pm, fisKeyString, "21-02-01-00", "creditors", dAG.getKeyString(), 4, AccType.ENTRY, aC59.getKeyString());
        AccChart aC61 = addAC(aCDb, pm, fisKeyString, "21-02-02-00", "advance checks", dAG.getKeyString(), 4, AccType.ENTRY, aC59.getKeyString());
        AccChart aC62 = addAC(aCDb, pm, fisKeyString, "21-03-00-00", "other debts", dAG.getKeyString(), 3, AccType.CONTROL, aC58.getKeyString());
        AccChart aC63 = addAC(aCDb, pm, fisKeyString, "21-03-01-00", "pending expenses", dAG.getKeyString(), 4, AccType.CONTROL, aC62.getKeyString());
        AccChart aC64 = addAC(aCDb, pm, fisKeyString, "21-03-01-01", "phone expenses", dAG.getKeyString(), 5, AccType.ENTRY, aC63.getKeyString());
        AccChart aC65 = addAC(aCDb, pm, fisKeyString, "21-03-01-02", "electric expenses", dAG.getKeyString(), 5, AccType.ENTRY, aC63.getKeyString());
        
        AccChart aC73 = addAC(aCDb, pm, fisKeyString, "30-00-00-00", "shareholdors", fAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC74 = addAC(aCDb, pm, fisKeyString, "31-00-00-00", "budget", fAG.getKeyString(), 2, AccType.ENTRY, aC73.getKeyString());
        AccChart aC75 = addAC(aCDb, pm, fisKeyString, "32-00-00-00", "cumulative profit(loss)", fAG.getKeyString(), 2, AccType.ENTRY, aC73.getKeyString());
        AccChart aC76 = addAC(aCDb, pm, fisKeyString, "33-00-00-00", "profit(loss)", fAG.getKeyString(), 2, AccType.ENTRY, aC73.getKeyString());
        
        AccChart aC77 = addAC(aCDb, pm, fisKeyString, "40-00-00-00", "income", rAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC78 = addAC(aCDb, pm, fisKeyString, "41-00-00-00", "income from products", rAG.getKeyString(), 2, AccType.CONTROL, aC77.getKeyString());
        AccChart aC79 = addAC(aCDb, pm, fisKeyString, "41-01-00-00", "income from sale", rAG.getKeyString(), 3, AccType.ENTRY, aC78.getKeyString());
        AccChart aC80 = addAC(aCDb, pm, fisKeyString, "41-02-00-00", "returned products", rAG.getKeyString(), 3, AccType.ENTRY, aC78.getKeyString());
        AccChart aC81 = addAC(aCDb, pm, fisKeyString, "41-03-00-00", "discount", rAG.getKeyString(), 3, AccType.ENTRY, aC78.getKeyString());
        AccChart aC82 = addAC(aCDb, pm, fisKeyString, "42-00-00-00", "other income", rAG.getKeyString(), 2, AccType.CONTROL, aC77.getKeyString());
        AccChart aC83 = addAC(aCDb, pm, fisKeyString, "42-01-00-00", "income from remnant", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());
        AccChart aC84 = addAC(aCDb, pm, fisKeyString, "42-02-00-00", "interest", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());

        AccChart aC91 = addAC(aCDb, pm, fisKeyString, "50-00-00-00", "total expenses", eAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC92 = addAC(aCDb, pm, fisKeyString, "51-00-00-00", "total cost", eAG.getKeyString(), 2, AccType.CONTROL, aC91.getKeyString());
        AccChart aC93 = addAC(aCDb, pm, fisKeyString, "51-01-00-00", "cost", eAG.getKeyString(), 3, AccType.ENTRY, aC92.getKeyString());
        AccChart aC94 = addAC(aCDb, pm, fisKeyString, "51-03-00-00", "total buy", eAG.getKeyString(), 3, AccType.CONTROL, aC92.getKeyString());
        AccChart aC95 = addAC(aCDb, pm, fisKeyString, "51-03-01-00", "buy", eAG.getKeyString(), 4, AccType.ENTRY, aC94.getKeyString());
        AccChart aC99 = addAC(aCDb, pm, fisKeyString, "52-00-00-00", "sale expenses", eAG.getKeyString(), 2, AccType.CONTROL, aC91.getKeyString());
        AccChart aC100 = addAC(aCDb, pm, fisKeyString, "52-01-00-00", "gas", eAG.getKeyString(), 3, AccType.ENTRY, aC99.getKeyString());
        AccChart aC101 = addAC(aCDb, pm, fisKeyString, "52-02-00-00", "transportation", eAG.getKeyString(), 3, AccType.ENTRY, aC99.getKeyString());
        AccChart aC105 = addAC(aCDb, pm, fisKeyString, "52-06-00-00", "ad fee", eAG.getKeyString(), 3, AccType.ENTRY, aC99.getKeyString());
        AccChart aC106 = addAC(aCDb, pm, fisKeyString, "53-00-00-00", "managing expenses", eAG.getKeyString(), 2, AccType.CONTROL, aC91.getKeyString());
        AccChart aC107 = addAC(aCDb, pm, fisKeyString, "53-01-00-00", "employee expenses", eAG.getKeyString(), 3, AccType.CONTROL, aC106.getKeyString());
        AccChart aC108 = addAC(aCDb, pm, fisKeyString, "53-01-01-00", "salary", eAG.getKeyString(), 4, AccType.ENTRY, aC107.getKeyString());
        
        SFinHeader sBal = new SFinHeader(null, "trail", d);
        addFinItem(sBal, 100, Comm.TXT, "assets", CalCon.CAL, PrintCon.PRINT, PrintStyle.CENTER, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 120, Comm.TXT, "live assets", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 130, Comm.ACCNO, aC4.getKeyString(), CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 141, Comm.ACCNO, aC6.getKeyString(), CalCon.CALIFPOS, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 151, Comm.ACCNO, aC10.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 170, Comm.PVAR3, "bank account", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 190, Comm.PVAR3, "debtors and checks", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 201, Comm.ACCNO, aC23.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 210, Comm.PVAR3, "remaining products", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 221, Comm.ACCNO, aC28.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 222, Comm.ACCNO, aC29.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 223, Comm.ACCNO, aC30.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 240, Comm.PVAR3, "other live assets", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 250, Comm.PVAR1, "total of live assets", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 270, Comm.PVAR3, "lands, buildings and equipments", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 280, Comm.PVAR1, "total of assets", CalCon.CAL, PrintCon.PRINT, PrintStyle.TWOULINES, Operand.CLEAR, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        
        addFinItem(sBal, 290, Comm.TXT, "debts and shareholders", CalCon.CAL, PrintCon.PRINT, PrintStyle.CENTER, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 300, Comm.TXT, "live debts", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 311, Comm.ACCNO, aC6.getKeyString(), CalCon.CALIFNE, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 320, Comm.PVAR3, "exceeded withdrawal   999-9-99999-1", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 331, Comm.ACCNO, aC60.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 332, Comm.ACCNO, aC61.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 340, Comm.PVAR3, "creditor and checks", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 351, Comm.ACCNO, aC64.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 352, Comm.ACCNO, aC65.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 360, Comm.PVAR3, "other live debts", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 370, Comm.PVAR1, "total of live debts", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 380, Comm.TXT, " ", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 390, Comm.PVAR1, "total of live debts", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 400, Comm.TXT, "shareholders", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 411, Comm.ACCNO, aC74.getKeyString(), CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 412, Comm.ACCNO, aC75.getKeyString(), CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 413, Comm.ACCNO, aC76.getKeyString(), CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 420, Comm.PVAR3, "total of shareholders", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 430, Comm.PVAR1, "total of live debts and shareholders", CalCon.CAL, PrintCon.PRINT, PrintStyle.TWOULINES, Operand.CLEAR, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        
        FinHeader bal = Db.addFinHeader(pm, fisKeyString, sBal);
        
        SFinHeader sPro = new SFinHeader(null, "profit and loss", d);
        addFinItem(sPro, 100, Comm.TXT, "income", CalCon.CAL, PrintCon.PRINT, PrintStyle.CENTER, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 111, Comm.ACCNO, aC79.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 112, Comm.ACCNO, aC80.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 113, Comm.ACCNO, aC81.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 120, Comm.PVAR3, "income from sale", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sPro, 131, Comm.ACCNO, aC83.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 132, Comm.ACCNO, aC84.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 140, Comm.PVAR3, "other income", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sPro, 150, Comm.PVAR1, "totol of income", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 160, Comm.TXT, "expenses", CalCon.CAL, PrintCon.PRINT, PrintStyle.CENTER, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 171, Comm.ACCNO, aC93.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 172, Comm.ACCNO, aC95.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 180, Comm.PVAR3, "cost of sale", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.PLUS, Operand.CLEAR, Operand.BLANK);
        addFinItem(sPro, 191, Comm.ACCNO, aC100.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 192, Comm.ACCNO, aC101.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 196, Comm.ACCNO, aC105.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 200, Comm.PVAR3, "sale expenses", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.PLUS, Operand.CLEAR, Operand.BLANK);
        addFinItem(sPro, 210, Comm.TXT, "managing expenses", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 221, Comm.ACCNO, aC108.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 230, Comm.PVAR3, "employee expenses", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.CLEAR, Operand.PLUS);
        addFinItem(sPro, 250, Comm.PVAR3, "office expenses", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.CLEAR, Operand.PLUS);
        addFinItem(sPro, 260, Comm.PVAR4, "totla of managing expenses", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR);
        addFinItem(sPro, 270, Comm.PVAR2, "total of expenses", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.MINUS, Operand.CLEAR, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 280, Comm.PVAR1, "profit (loss)", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.CLEAR, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        
        FinHeader pro = Db.addFinHeader(pm, fisKeyString, sPro);
    }
    
    @SuppressWarnings("unused")
    public static void createInThai(PersistenceManager pm, final String fisKeyString){
        
        Date d = new Date();
        
        Db<JournalType> jTDb = new Db<JournalType>();
        JournalType gJT = addJT(jTDb, pm, fisKeyString, "รายวันทั่วไป", "ทป.", d);
        JournalType pJT = addJT(jTDb, pm, fisKeyString, "รายวันจ่าย", "จ่าย", d);
        JournalType rJT = addJT(jTDb, pm, fisKeyString, "รายวันรับ", "รับ", d);
        JournalType sJT = addJT(jTDb, pm, fisKeyString, "รายวันขาย", "ขาย", d);
        JournalType bJT = addJT(jTDb, pm, fisKeyString, "รายวันซื้อ", "ซื้อ", d);
        
        Db<DocType> dTDb = new Db<DocType>();
        DocType gDT = addDT(dTDb, pm, fisKeyString, gJT.getKeyString(), "JV", "ทั่วไป", "", d);
        DocType pDT = addDT(dTDb, pm, fisKeyString, pJT.getKeyString(), "PV", "จ่าย", "", d);
        DocType rDT = addDT(dTDb, pm, fisKeyString, rJT.getKeyString(), "RV", "รับ", "", d);
        DocType sDT = addDT(dTDb, pm, fisKeyString, sJT.getKeyString(), "SV", "ขาย", "", d);
        DocType bDT = addDT(dTDb, pm, fisKeyString, bJT.getKeyString(), "UV", "ซื้อ", "", d);
        
        
        Db<AccGroup> aGDb = new Db<AccGroup>();
        AccGroup wAG = addAG(aGDb, pm, fisKeyString, "ส/ท", d);
        AccGroup dAG = addAG(aGDb, pm, fisKeyString, "หนี้สิน", d);
        AccGroup fAG = addAG(aGDb, pm, fisKeyString, "ทุน", d);
        AccGroup rAG = addAG(aGDb, pm, fisKeyString, "รายได้", d);
        AccGroup eAG = addAG(aGDb, pm, fisKeyString, "คชจ.", d);
        
        
        Db<AccChart> aCDb = new Db<AccChart>();
        AccChart aC1 = addAC(aCDb, pm, fisKeyString, "10-00-00-00", "สินทรัพย์", wAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC2 = addAC(aCDb, pm, fisKeyString, "11-00-00-00", "สินทรัพย์หมุนเวียน", wAG.getKeyString(), 2, AccType.CONTROL, aC1.getKeyString());
        AccChart aC3 = addAC(aCDb, pm, fisKeyString, "11-01-00-00", "เงินสดและเงินฝากธนาคาร", wAG.getKeyString(), 3, AccType.CONTROL, aC2.getKeyString());
        AccChart aC4 = addAC(aCDb, pm, fisKeyString, "11-01-01-00", "เงินสด", wAG.getKeyString(), 4, AccType.ENTRY, aC3.getKeyString());
        AccChart aC5 = addAC(aCDb, pm, fisKeyString, "11-01-02-00", "เงินฝากกระแสรายวัน", wAG.getKeyString(), 4, AccType.CONTROL, aC3.getKeyString());
        AccChart aC6 = addAC(aCDb, pm, fisKeyString, "11-01-02-01", "เงินฝากกระแสรายวัน    999-9-9999-1", wAG.getKeyString(), 5, AccType.ENTRY, aC5.getKeyString());
        AccChart aC7 = addAC(aCDb, pm, fisKeyString, "11-01-02-02", "เงินฝากกระแสรายวัน    999-9-9999-2", wAG.getKeyString(), 5, AccType.ENTRY, aC5.getKeyString());
        AccChart aC8 = addAC(aCDb, pm, fisKeyString, "11-01-02-03", "เงินฝากกระแสรายวัน    999-9-9999-3", wAG.getKeyString(), 5, AccType.ENTRY, aC5.getKeyString());
        AccChart aC9 = addAC(aCDb, pm, fisKeyString, "11-01-03-00", "เงินฝากออกทรัพย์", wAG.getKeyString(), 4, AccType.CONTROL, aC3.getKeyString());
        AccChart aC10 = addAC(aCDb, pm, fisKeyString, "11-01-03-01", "เงินฝากออกทรัพย์     999-9-9999-1", wAG.getKeyString(), 5, AccType.ENTRY, aC9.getKeyString());
        AccChart aC11 = addAC(aCDb, pm, fisKeyString, "11-01-03-02", "เงินฝากออกทรัพย์     999-9-9999-2", wAG.getKeyString(), 5, AccType.ENTRY, aC9.getKeyString());
        AccChart aC12 = addAC(aCDb, pm, fisKeyString, "11-01-03-03", "เงินฝากออกทรัพย์     999-9-9999-3", wAG.getKeyString(), 5, AccType.ENTRY, aC9.getKeyString());
        AccChart aC13 = addAC(aCDb, pm, fisKeyString, "11-01-04-00", "เงินฝากประจำ", wAG.getKeyString(), 4, AccType.CONTROL, aC3.getKeyString());
        AccChart aC14 = addAC(aCDb, pm, fisKeyString, "11-01-04-01", "เงินฝากประจำ     999-9-9999-1 ", wAG.getKeyString(), 5, AccType.ENTRY, aC13.getKeyString());
        AccChart aC15 = addAC(aCDb, pm, fisKeyString, "11-01-04-02", "เงินฝากประจำ     999-9-9999-2 ", wAG.getKeyString(), 5, AccType.ENTRY, aC13.getKeyString());
        AccChart aC16 = addAC(aCDb, pm, fisKeyString, "11-01-04-03", "เงินฝากประจำ     999-9-9999-3 ", wAG.getKeyString(), 5, AccType.ENTRY, aC13.getKeyString());
        AccChart aC17 = addAC(aCDb, pm, fisKeyString, "11-02-00-00", "ลูกหนี้การค้าและตั๋วเงินรับ", wAG.getKeyString(), 3, AccType.CONTROL, aC2.getKeyString());
        AccChart aC18 = addAC(aCDb, pm, fisKeyString, "11-02-01-00", "ลูกหนี้การค้า", wAG.getKeyString(), 4, AccType.ENTRY, aC17.getKeyString());
        AccChart aC19 = addAC(aCDb, pm, fisKeyString, "11-02-02-00", "เช็ครับลงวันที่ล่วงหน้า", wAG.getKeyString(), 4, AccType.ENTRY, aC17.getKeyString());
        AccChart aC20 = addAC(aCDb, pm, fisKeyString, "11-02-03-00", "สำรองหนี้สูญ", wAG.getKeyString(), 4, AccType.ENTRY, aC17.getKeyString());
        AccChart aC21 = addAC(aCDb, pm, fisKeyString, "11-02-04-00", "ลูกหนี้อื่นๆ", wAG.getKeyString(), 4, AccType.ENTRY, aC17.getKeyString());
        AccChart aC22 = addAC(aCDb, pm, fisKeyString, "11-04-00-00", "สินค้าคงเหลือ", wAG.getKeyString(), 3, AccType.CONTROL, aC2.getKeyString());
        AccChart aC23 = addAC(aCDb, pm, fisKeyString, "11-04-01-00", "วัตถุดิบคงเหลือ", wAG.getKeyString(), 4, AccType.ENTRY, aC22.getKeyString());
        AccChart aC24 = addAC(aCDb, pm, fisKeyString, "11-04-02-00", "สินค้าสำเร็จรูปคงเหลือ", wAG.getKeyString(), 4, AccType.ENTRY, aC22.getKeyString());
        AccChart aC25 = addAC(aCDb, pm, fisKeyString, "11-04-03-00", "งานระหว่างทำ", wAG.getKeyString(), 4, AccType.ENTRY, aC22.getKeyString());
        AccChart aC26 = addAC(aCDb, pm, fisKeyString, "11-05-00-00", "สินทรัพย์หมุนเวียนอื่นๆ", wAG.getKeyString(), 3, AccType.CONTROL, aC2.getKeyString());
        AccChart aC27 = addAC(aCDb, pm, fisKeyString, "11-05-01-00", "ค่าใช้จ่ายจ่ายล่วงหน้า", wAG.getKeyString(), 4, AccType.CONTROL, aC26.getKeyString());
        AccChart aC28 = addAC(aCDb, pm, fisKeyString, "11-05-01-01", "ค่าใช้จ่ายจ่ายล่วงหน้า-ค่าสินค้า", wAG.getKeyString(), 5, AccType.ENTRY, aC27.getKeyString());
        AccChart aC29 = addAC(aCDb, pm, fisKeyString, "11-05-01-02", "ภาษีนิติบุคคลจ่ายล่วงหน้า", wAG.getKeyString(), 5, AccType.ENTRY, aC27.getKeyString());
        AccChart aC30 = addAC(aCDb, pm, fisKeyString, "11-05-01-03", "ค่าใช้จ่ายล่วงหน้าอื่นๆ", wAG.getKeyString(), 5, AccType.ENTRY, aC27.getKeyString());
        AccChart aC31 = addAC(aCDb, pm, fisKeyString, "11-05-02-00", "เงินทดลองจ่ายพนักงาน", wAG.getKeyString(), 4, AccType.ENTRY, aC26.getKeyString());
        AccChart aC32 = addAC(aCDb, pm, fisKeyString, "11-05-03-00", "รายได้ค้างรับ", wAG.getKeyString(), 4, AccType.ENTRY, aC26.getKeyString());
        AccChart aC33 = addAC(aCDb, pm, fisKeyString, "11-05-04-00", "ภาษีมูลค่าเพิ่ม", wAG.getKeyString(), 4, AccType.CONTROL, aC26.getKeyString());
        AccChart aC34 = addAC(aCDb, pm, fisKeyString, "11-05-04-01", "ภาษีซื้อ", wAG.getKeyString(), 5, AccType.ENTRY, aC33.getKeyString());
        AccChart aC35 = addAC(aCDb, pm, fisKeyString, "11-05-04-02", "ภาษีขาย", wAG.getKeyString(), 5, AccType.ENTRY, aC33.getKeyString());
        AccChart aC36 = addAC(aCDb, pm, fisKeyString, "11-05-04-03", "ภาษีขาย-รอเรียกเก็บ", wAG.getKeyString(), 5, AccType.ENTRY, aC33.getKeyString());
        AccChart aC37 = addAC(aCDb, pm, fisKeyString, "11-05-04-04", "ภาษีซื้อ-ยังไม่ถึงกำหนด", wAG.getKeyString(), 5, AccType.ENTRY, aC33.getKeyString());
        AccChart aC38 = addAC(aCDb, pm, fisKeyString, "11-05-05-00", "ลูกหนี้-กรมสรรพากร", wAG.getKeyString(), 4, AccType.ENTRY, aC26.getKeyString());
        AccChart aC39 = addAC(aCDb, pm, fisKeyString, "12-00-00-00", "ลูกหนี้เงินให้กู้ยืมแก่กรรมการและลูกจ้าง", wAG.getKeyString(), 2, AccType.CONTROL, aC1.getKeyString());
        AccChart aC40 = addAC(aCDb, pm, fisKeyString, "12-01-00-00", "ลูกหนี้เงินให้กู้ยืม-นาย...", wAG.getKeyString(), 3, AccType.ENTRY, aC39.getKeyString());
        AccChart aC41 = addAC(aCDb, pm, fisKeyString, "13-00-00-00", "เงินลงทุนในบริษัทในเครือ", wAG.getKeyString(), 2, AccType.CONTROL, aC1.getKeyString());
        AccChart aC42 = addAC(aCDb, pm, fisKeyString, "14-00-00-00", "ที่ดิน อาคารและอุปกรณ์สิทธิ", wAG.getKeyString(), 2, AccType.CONTROL, aC1.getKeyString());
        AccChart aC43 = addAC(aCDb, pm, fisKeyString, "14-01-00-00", "ที่ดิน อาคาร ยานพาหนะและอุปกรณ์สิทธิ", wAG.getKeyString(), 3, AccType.CONTROL, aC42.getKeyString());
        AccChart aC44 = addAC(aCDb, pm, fisKeyString, "14-01-01-00", "ที่ดิน", wAG.getKeyString(), 4, AccType.ENTRY, aC43.getKeyString());
        AccChart aC45 = addAC(aCDb, pm, fisKeyString, "14-01-02-00", "อาคาร", wAG.getKeyString(), 4, AccType.ENTRY, aC43.getKeyString());
        AccChart aC46 = addAC(aCDb, pm, fisKeyString, "14-01-03-00", "อุปกรณ์สำนักงาน", wAG.getKeyString(), 4, AccType.ENTRY, aC43.getKeyString());
        AccChart aC47 = addAC(aCDb, pm, fisKeyString, "14-01-04-00", "ยานพาหนะ", wAG.getKeyString(), 4, AccType.ENTRY, aC43.getKeyString());
        AccChart aC48 = addAC(aCDb, pm, fisKeyString, "14-02-00-00", "ค่าเสื่อมราคาสะสม", wAG.getKeyString(), 3, AccType.CONTROL, aC42.getKeyString());
        AccChart aC49 = addAC(aCDb, pm, fisKeyString, "14-02-01-00", "ค่าเสื่อมราคาสะสม-อาคาร", wAG.getKeyString(), 4, AccType.ENTRY, aC48.getKeyString());
        AccChart aC50 = addAC(aCDb, pm, fisKeyString, "14-02-02-00", "ค่าเสื่อมราคาสะสม-อุปกรณ์สำนักงาน", wAG.getKeyString(), 4, AccType.ENTRY, aC48.getKeyString());
        AccChart aC51 = addAC(aCDb, pm, fisKeyString, "14-02-03-00", "ค่าเสื่อมราคาสะสม-ยานพาหนะ", wAG.getKeyString(), 4, AccType.ENTRY, aC48.getKeyString());
        AccChart aC52 = addAC(aCDb, pm, fisKeyString, "15-00-00-00", "สินทรัพย์อื่นๆ", wAG.getKeyString(), 2, AccType.CONTROL, aC1.getKeyString());
        AccChart aC53 = addAC(aCDb, pm, fisKeyString, "15-01-00-00", "กรมธรรม์ประกันอัคคีภัย-สินค้าและอาคาร", wAG.getKeyString(), 3, AccType.ENTRY, aC52.getKeyString());
        AccChart aC54 = addAC(aCDb, pm, fisKeyString, "15-02-00-00", "กรมธรรม์ประกันอัคคีภัย-ยานพาหนะ", wAG.getKeyString(), 3, AccType.ENTRY, aC52.getKeyString());
        AccChart aC55 = addAC(aCDb, pm, fisKeyString, "15-03-00-00", "กรมธรรม์ประกันอุบัติเหตุพนักงาน", wAG.getKeyString(), 3, AccType.ENTRY, aC52.getKeyString());
        AccChart aC56 = addAC(aCDb, pm, fisKeyString, "15-04-00-00", "พันธบัตรโทรศัพท์", wAG.getKeyString(), 3, AccType.ENTRY, aC52.getKeyString());
        
        AccChart aC57 = addAC(aCDb, pm, fisKeyString, "20-00-00-00", "หนี้สิน", dAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC58 = addAC(aCDb, pm, fisKeyString, "21-00-00-00", "หนี้สินหมุนเวียน", dAG.getKeyString(), 2, AccType.CONTROL, aC57.getKeyString());
        AccChart aC59 = addAC(aCDb, pm, fisKeyString, "21-02-00-00", "เจ้าหนี้การค้าและตั๋วเงินจ่าย", dAG.getKeyString(), 3, AccType.CONTROL, aC58.getKeyString());
        AccChart aC60 = addAC(aCDb, pm, fisKeyString, "21-02-01-00", "เจ้าหนี้การค้า", dAG.getKeyString(), 4, AccType.ENTRY, aC59.getKeyString());
        AccChart aC61 = addAC(aCDb, pm, fisKeyString, "21-02-02-00", "เช็คจ่ายล่วงหน้า", dAG.getKeyString(), 4, AccType.ENTRY, aC59.getKeyString());
        AccChart aC62 = addAC(aCDb, pm, fisKeyString, "21-03-00-00", "หนี้สินหมุนเวียนอื่น", dAG.getKeyString(), 3, AccType.CONTROL, aC58.getKeyString());
        AccChart aC63 = addAC(aCDb, pm, fisKeyString, "21-03-01-00", "ค่าใช้จ่ายค้างจ่าย", dAG.getKeyString(), 4, AccType.CONTROL, aC62.getKeyString());
        AccChart aC64 = addAC(aCDb, pm, fisKeyString, "21-03-01-01", "ค่าใช้จ่ายค้างจ่าย-ค่าโทรศัพท์", dAG.getKeyString(), 5, AccType.ENTRY, aC63.getKeyString());
        AccChart aC65 = addAC(aCDb, pm, fisKeyString, "21-03-01-02", "ค่าใช้จ่ายค้างจ่าย-ค่าไฟฟ้าและประปา", dAG.getKeyString(), 5, AccType.ENTRY, aC63.getKeyString());
        AccChart aC66 = addAC(aCDb, pm, fisKeyString, "21-03-02-00", "ดอกเบี้ยเงินกู้", dAG.getKeyString(), 4, AccType.ENTRY, aC62.getKeyString());
        AccChart aC67 = addAC(aCDb, pm, fisKeyString, "21-03-03-00", "ภาษีเงินได้หัก ณ ที่จ่ายค้างจ่าย", dAG.getKeyString(), 4, AccType.ENTRY, aC62.getKeyString());
        AccChart aC68 = addAC(aCDb, pm, fisKeyString, "21-03-04-00", "คชจ.ค้างจ่ายอื่นๆ", dAG.getKeyString(), 4, AccType.ENTRY, aC62.getKeyString());
        AccChart aC69 = addAC(aCDb, pm, fisKeyString, "21-03-05-00", "รายได้รับล่วงหน้า", dAG.getKeyString(), 4, AccType.ENTRY, aC62.getKeyString());
        AccChart aC70 = addAC(aCDb, pm, fisKeyString, "21-03-06-00", "เจ้าหนี้กรมสรรพากร", dAG.getKeyString(), 4, AccType.ENTRY, aC62.getKeyString());
        AccChart aC71 = addAC(aCDb, pm, fisKeyString, "22-00-00-00", "หนี้สินอื่นๆ", dAG.getKeyString(), 2, AccType.CONTROL, aC57.getKeyString());
        AccChart aC72 = addAC(aCDb, pm, fisKeyString, "22-01-00-00", "หนี้สินอื่นๆ", dAG.getKeyString(), 3, AccType.ENTRY, aC71.getKeyString());
        
        AccChart aC73 = addAC(aCDb, pm, fisKeyString, "30-00-00-00", "ส่วนของผู้ถือหุ้น", fAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC74 = addAC(aCDb, pm, fisKeyString, "31-00-00-00", "ทุน", fAG.getKeyString(), 2, AccType.ENTRY, aC73.getKeyString());
        AccChart aC75 = addAC(aCDb, pm, fisKeyString, "32-00-00-00", "กำไร(ขาดทุน)สะสม", fAG.getKeyString(), 2, AccType.ENTRY, aC73.getKeyString());
        AccChart aC76 = addAC(aCDb, pm, fisKeyString, "33-00-00-00", "กำไร(ขาดทุน)", fAG.getKeyString(), 2, AccType.ENTRY, aC73.getKeyString());
        
        AccChart aC77 = addAC(aCDb, pm, fisKeyString, "40-00-00-00", "รายได้", rAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC78 = addAC(aCDb, pm, fisKeyString, "41-00-00-00", "รายได้จากการขายสินค้า-สุทธิ", rAG.getKeyString(), 2, AccType.CONTROL, aC77.getKeyString());
        AccChart aC79 = addAC(aCDb, pm, fisKeyString, "41-01-00-00", "รายได้จากการขาย", rAG.getKeyString(), 3, AccType.ENTRY, aC78.getKeyString());
        AccChart aC80 = addAC(aCDb, pm, fisKeyString, "41-02-00-00", "รับคืนสินค้า", rAG.getKeyString(), 3, AccType.ENTRY, aC78.getKeyString());
        AccChart aC81 = addAC(aCDb, pm, fisKeyString, "41-03-00-00", "ส่วนลดจ่าย", rAG.getKeyString(), 3, AccType.ENTRY, aC78.getKeyString());
        AccChart aC82 = addAC(aCDb, pm, fisKeyString, "42-00-00-00", "รายได้อื่นๆ", rAG.getKeyString(), 2, AccType.CONTROL, aC77.getKeyString());
        AccChart aC83 = addAC(aCDb, pm, fisKeyString, "42-01-00-00", "รายได้จากการขายเศษวัสดุ", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());
        AccChart aC84 = addAC(aCDb, pm, fisKeyString, "42-02-00-00", "ดอกเบี้ยรับ", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());
        AccChart aC85 = addAC(aCDb, pm, fisKeyString, "42-03-00-00", "กำไร(ขาดทุน)จากการจำหน่ายทรัพย์สิน", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());
        AccChart aC86 = addAC(aCDb, pm, fisKeyString, "42-04-00-00", "ส่วนลดเงินสดรับ", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());
        AccChart aC87 = addAC(aCDb, pm, fisKeyString, "42-05-00-00", "รายได้อื่นๆ", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());
        AccChart aC88 = addAC(aCDb, pm, fisKeyString, "42-06-00-00", "รายได้จากการปรับแบบฟอร์ม", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());
        AccChart aC89 = addAC(aCDb, pm, fisKeyString, "42-07-00-00", "รายได้ให้เช่าที่จอดรถ", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());
        AccChart aC90 = addAC(aCDb, pm, fisKeyString, "42-08-00-00", "รายได้บริการให้คำปรึกษา", rAG.getKeyString(), 3, AccType.ENTRY, aC82.getKeyString());
        
        AccChart aC91 = addAC(aCDb, pm, fisKeyString, "50-00-00-00", "ค่าใช้จ่าย", eAG.getKeyString(), 1, AccType.CONTROL, null);
        AccChart aC92 = addAC(aCDb, pm, fisKeyString, "51-00-00-00", "ต้นทุนขายสุทธิ", eAG.getKeyString(), 2, AccType.CONTROL, aC91.getKeyString());
        AccChart aC93 = addAC(aCDb, pm, fisKeyString, "51-01-00-00", "ต้นทุนสินค้าเพื่อขาย", eAG.getKeyString(), 3, AccType.ENTRY, aC92.getKeyString());
        AccChart aC94 = addAC(aCDb, pm, fisKeyString, "51-03-00-00", "ซื้อสุทธิ", eAG.getKeyString(), 3, AccType.CONTROL, aC92.getKeyString());
        AccChart aC95 = addAC(aCDb, pm, fisKeyString, "51-03-01-00", "ซื้อ", eAG.getKeyString(), 4, AccType.ENTRY, aC94.getKeyString());
        AccChart aC96 = addAC(aCDb, pm, fisKeyString, "51-03-02-00", "ส่วนลดรับ", eAG.getKeyString(), 4, AccType.ENTRY, aC94.getKeyString());
        AccChart aC97 = addAC(aCDb, pm, fisKeyString, "51-03-03-00", "ส่งคืนสินค้า", eAG.getKeyString(), 4, AccType.ENTRY, aC94.getKeyString());
        AccChart aC98 = addAC(aCDb, pm, fisKeyString, "51-05-00-00", "สินค้าตัวอย่าง", eAG.getKeyString(), 3, AccType.ENTRY, aC92.getKeyString());
        AccChart aC99 = addAC(aCDb, pm, fisKeyString, "52-00-00-00", "ค่าใช้จ่ายในการขาย", eAG.getKeyString(), 2, AccType.CONTROL, aC91.getKeyString());
        AccChart aC100 = addAC(aCDb, pm, fisKeyString, "52-01-00-00", "ค่าน้ำมันรถ", eAG.getKeyString(), 3, AccType.ENTRY, aC99.getKeyString());
        AccChart aC101 = addAC(aCDb, pm, fisKeyString, "52-02-00-00", "ค่าขนส่ง", eAG.getKeyString(), 3, AccType.ENTRY, aC99.getKeyString());
        AccChart aC102 = addAC(aCDb, pm, fisKeyString, "52-03-00-00", "คชจ.ในการขาย-ค่าติดต่อสื่อสาร", eAG.getKeyString(), 3, AccType.ENTRY, aC99.getKeyString());
        AccChart aC103 = addAC(aCDb, pm, fisKeyString, "52-04-00-00", "คชจ.ในการขาย-ค่าโทรศัพท์", eAG.getKeyString(), 3, AccType.ENTRY, aC99.getKeyString());
        AccChart aC104 = addAC(aCDb, pm, fisKeyString, "52-05-00-00", "ค่าใช้จ่าย-เบิกสินค้าตัวอย่าง", eAG.getKeyString(), 3, AccType.ENTRY, aC99.getKeyString());
        AccChart aC105 = addAC(aCDb, pm, fisKeyString, "52-06-00-00", "ค่าโฆษณา", eAG.getKeyString(), 3, AccType.ENTRY, aC99.getKeyString());
        AccChart aC106 = addAC(aCDb, pm, fisKeyString, "53-00-00-00", "ค่าใช้จ่ายในการบริหาร", eAG.getKeyString(), 2, AccType.CONTROL, aC91.getKeyString());
        AccChart aC107 = addAC(aCDb, pm, fisKeyString, "53-01-00-00", "ค่าใช้จ่ายเกี่ยวกับพนักงาน", eAG.getKeyString(), 3, AccType.CONTROL, aC106.getKeyString());
        AccChart aC108 = addAC(aCDb, pm, fisKeyString, "53-01-01-00", "เงินเดือน", eAG.getKeyString(), 4, AccType.ENTRY, aC107.getKeyString());
        AccChart aC109 = addAC(aCDb, pm, fisKeyString, "53-01-02-00", "คอมมิชชั่น", eAG.getKeyString(), 4, AccType.ENTRY, aC107.getKeyString());
        AccChart aC110 = addAC(aCDb, pm, fisKeyString, "53-01-03-00", "ค่าเบี้ยเลี้ยง", eAG.getKeyString(), 4, AccType.ENTRY, aC107.getKeyString());
        AccChart aC111 = addAC(aCDb, pm, fisKeyString, "53-01-04-00", "ค่าล่วงเวลาพนักงาน", eAG.getKeyString(), 4, AccType.ENTRY, aC107.getKeyString());
        AccChart aC112 = addAC(aCDb, pm, fisKeyString, "53-01-05-00", "ค่ารักษาพยาบาล", eAG.getKeyString(), 4, AccType.ENTRY, aC107.getKeyString());
        AccChart aC113 = addAC(aCDb, pm, fisKeyString, "53-02-00-00", "ค่าภาษีและค่าธรรมเนียม", eAG.getKeyString(), 3, AccType.CONTROL, aC106.getKeyString());
        AccChart aC114 = addAC(aCDb, pm, fisKeyString, "53-02-01-00", "ภาษีเงินได้นิติบุคคล", eAG.getKeyString(), 4, AccType.ENTRY, aC113.getKeyString());
        AccChart aC115 = addAC(aCDb, pm, fisKeyString, "53-02-02-00", "ค่าธรรมเนียมเรียกเก็บ", eAG.getKeyString(), 4, AccType.ENTRY, aC113.getKeyString());
        AccChart aC116 = addAC(aCDb, pm, fisKeyString, "53-02-03-00", "ค่าธรรมเนียมหนังสือค้ำประกัน", eAG.getKeyString(), 4, AccType.ENTRY, aC113.getKeyString());
        AccChart aC117 = addAC(aCDb, pm, fisKeyString, "53-02-04-00", "ค่าตรวจสอบบัญชีและปรึกษากฎหมาย", eAG.getKeyString(), 4, AccType.ENTRY, aC113.getKeyString());
        AccChart aC118 = addAC(aCDb, pm, fisKeyString, "53-03-00-00", "ค่าใช้จ่ายสำนักงาน", eAG.getKeyString(), 3, AccType.CONTROL, aC106.getKeyString());
        AccChart aC119 = addAC(aCDb, pm, fisKeyString, "53-03-01-00", "ค่าวารสารและสมาชิก", eAG.getKeyString(), 4, AccType.ENTRY, aC118.getKeyString());
        AccChart aC120 = addAC(aCDb, pm, fisKeyString, "53-03-02-00", "ค่าซ่อมแซม", eAG.getKeyString(), 4, AccType.ENTRY, aC118.getKeyString());
        AccChart aC121 = addAC(aCDb, pm, fisKeyString, "53-03-03-00", "ค่าน้ำประปาและไฟฟ้า", eAG.getKeyString(), 4, AccType.ENTRY, aC118.getKeyString());
        AccChart aC122 = addAC(aCDb, pm, fisKeyString, "53-03-04-00", "ค่าโทรศัพท์", eAG.getKeyString(), 4, AccType.ENTRY, aC118.getKeyString());
        AccChart aC123 = addAC(aCDb, pm, fisKeyString, "53-03-05-00", "ค่าเสื่อมราคา", eAG.getKeyString(), 4, AccType.ENTRY, aC118.getKeyString());
        AccChart aC124 = addAC(aCDb, pm, fisKeyString, "53-03-06-00", "วัสดุสำนักงานใช้ไป", eAG.getKeyString(), 4, AccType.ENTRY, aC118.getKeyString());
        AccChart aC125 = addAC(aCDb, pm, fisKeyString, "53-04-00-00", "ค่าประกันภัย", eAG.getKeyString(), 3, AccType.CONTROL, aC106.getKeyString());
        AccChart aC126 = addAC(aCDb, pm, fisKeyString, "53-04-01-00", "ค่าประกันอัคคีภัย", eAG.getKeyString(), 4, AccType.ENTRY, aC125.getKeyString());
        AccChart aC127 = addAC(aCDb, pm, fisKeyString, "53-04-02-00", "ค่าประกันอุบัติเหตุ", eAG.getKeyString(), 4, AccType.ENTRY, aC125.getKeyString());
        AccChart aC128 = addAC(aCDb, pm, fisKeyString, "53-05-00-00", "ค่าใช้จ่ายอื่นๆ", eAG.getKeyString(), 3, AccType.CONTROL, aC106.getKeyString());
        AccChart aC129 = addAC(aCDb, pm, fisKeyString, "53-05-01-00", "ดอกเบี้ยจ่าย", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        AccChart aC130 = addAC(aCDb, pm, fisKeyString, "53-05-01-00", "ค่าใช้จ่ายเบ็ดเตล็ด", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        AccChart aC131 = addAC(aCDb, pm, fisKeyString, "53-05-01-00", "ส่วนลดเงินสดจ่าย", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        AccChart aC132 = addAC(aCDb, pm, fisKeyString, "53-05-01-00", "ค่ารับรอง", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        AccChart aC133 = addAC(aCDb, pm, fisKeyString, "53-05-01-00", "ค่าการกุศล", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        AccChart aC134 = addAC(aCDb, pm, fisKeyString, "53-05-01-00", "ค่ารับรองลูกค้า", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        
        SFinHeader sBal = new SFinHeader(null, "งบดุล", d);
        addFinItem(sBal, 100, Comm.TXT, "สินทรัพย์", CalCon.CAL, PrintCon.PRINT, PrintStyle.CENTER, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 120, Comm.TXT, "สินทรัพย์หมุนเวียน", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 130, Comm.ACCNO, aC4.getKeyString(), CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 141, Comm.ACCNO, aC6.getKeyString(), CalCon.CALIFPOS, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 142, Comm.ACCNO, aC7.getKeyString(), CalCon.CALIFPOS, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 143, Comm.ACCNO, aC8.getKeyString(), CalCon.CALIFPOS, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 151, Comm.ACCNO, aC10.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 152, Comm.ACCNO, aC11.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 153, Comm.ACCNO, aC12.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 161, Comm.ACCNO, aC14.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 162, Comm.ACCNO, aC15.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 163, Comm.ACCNO, aC16.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 170, Comm.PVAR3, "เงินฝากธนาคาร", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 181, Comm.ACCNO, aC18.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 182, Comm.ACCNO, aC19.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 183, Comm.ACCNO, aC20.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 184, Comm.ACCNO, aC21.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 190, Comm.PVAR3, "ลูกหนี้และตั๋วเงินรับ", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 201, Comm.ACCNO, aC23.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 202, Comm.ACCNO, aC24.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 203, Comm.ACCNO, aC25.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 210, Comm.PVAR3, "สินค้าคงเหลือ", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 221, Comm.ACCNO, aC28.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 222, Comm.ACCNO, aC29.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 223, Comm.ACCNO, aC30.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 224, Comm.ACCNO, aC31.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 225, Comm.ACCNO, aC32.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 226, Comm.ACCNO, aC34.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 227, Comm.ACCNO, aC35.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 228, Comm.ACCNO, aC36.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 229, Comm.ACCNO, aC37.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 230, Comm.ACCNO, aC38.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 240, Comm.PVAR3, "สินทรัพย์หมุนเวียนอื่นๆ", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 250, Comm.PVAR1, "รวมสินทรัพย์หมุนเวียน", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 261, Comm.ACCNO, aC44.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 262, Comm.ACCNO, aC45.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 263, Comm.ACCNO, aC46.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 264, Comm.ACCNO, aC47.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 265, Comm.ACCNO, aC49.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 266, Comm.ACCNO, aC50.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 267, Comm.ACCNO, aC51.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 270, Comm.PVAR3, "ที่ดิน อาคารและอุปกรณ์", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 280, Comm.PVAR1, "รวมสินทรัพย์", CalCon.CAL, PrintCon.PRINT, PrintStyle.TWOULINES, Operand.CLEAR, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        
        addFinItem(sBal, 290, Comm.TXT, "หนี้สินและส่วนของผู้ถือหุ้น", CalCon.CAL, PrintCon.PRINT, PrintStyle.CENTER, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 300, Comm.TXT, "หนี้สินหมุนเวียน", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 311, Comm.ACCNO, aC6.getKeyString(), CalCon.CALIFNE, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 312, Comm.ACCNO, aC7.getKeyString(), CalCon.CALIFNE, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 313, Comm.ACCNO, aC8.getKeyString(), CalCon.CALIFNE, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 320, Comm.PVAR3, "เงินเบิกเกินบัญชี    999-9-99999-1", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 331, Comm.ACCNO, aC60.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 332, Comm.ACCNO, aC61.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 340, Comm.PVAR3, "เจ้าหนี้การค้าและตั๋วจ่ายเงิน", CalCon.CAL, PrintCon.NOIFZERO, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 351, Comm.ACCNO, aC64.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 352, Comm.ACCNO, aC65.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 353, Comm.ACCNO, aC66.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 354, Comm.ACCNO, aC67.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 355, Comm.ACCNO, aC68.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 356, Comm.ACCNO, aC69.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 357, Comm.ACCNO, aC70.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 360, Comm.PVAR3, "หนี้สินหมุนเวียนอื่นๆ", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 370, Comm.PVAR1, "รวมหนี้สินหมุนเวียน", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 380, Comm.TXT, " ", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 390, Comm.PVAR1, "รวมหนี้สินหมุนเวียน", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 400, Comm.TXT, "ส่วนของผู้ถือหุ้น", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sBal, 411, Comm.ACCNO, aC74.getKeyString(), CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 412, Comm.ACCNO, aC75.getKeyString(), CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 413, Comm.ACCNO, aC76.getKeyString(), CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sBal, 420, Comm.PVAR3, "รวมส่วนของผู้ถือหุ้น", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sBal, 430, Comm.PVAR1, "รวมหนี้สินและส่วนของผู้ถือหุ้น", CalCon.CAL, PrintCon.PRINT, PrintStyle.TWOULINES, Operand.CLEAR, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        
        FinHeader bal = Db.addFinHeader(pm, fisKeyString, sBal);
        
        SFinHeader sPro = new SFinHeader(null, "งบกำไรขาดทุน", d);
        addFinItem(sPro, 100, Comm.TXT, "รายได้", CalCon.CAL, PrintCon.PRINT, PrintStyle.CENTER, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 111, Comm.ACCNO, aC79.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 112, Comm.ACCNO, aC80.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 113, Comm.ACCNO, aC81.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 120, Comm.PVAR3, "รายได้จากการขาย", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sPro, 131, Comm.ACCNO, aC83.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 132, Comm.ACCNO, aC84.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 133, Comm.ACCNO, aC85.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 134, Comm.ACCNO, aC86.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 135, Comm.ACCNO, aC87.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 136, Comm.ACCNO, aC88.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 137, Comm.ACCNO, aC89.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 138, Comm.ACCNO, aC90.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 140, Comm.PVAR3, "รายได้อื่นๆ", CalCon.CAL, PrintCon.PRINT, PrintStyle.ULINE, Operand.PLUS, Operand.BLANK, Operand.CLEAR, Operand.BLANK);
        addFinItem(sPro, 150, Comm.PVAR1, "รวมรายได้", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 160, Comm.TXT, "ค่าใช้จ่าย", CalCon.CAL, PrintCon.PRINT, PrintStyle.CENTER, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 171, Comm.ACCNO, aC93.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 172, Comm.ACCNO, aC95.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 173, Comm.ACCNO, aC96.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 174, Comm.ACCNO, aC97.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 175, Comm.ACCNO, aC98.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 180, Comm.PVAR3, "ต้นทุนขายสุทธิ", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.PLUS, Operand.CLEAR, Operand.BLANK);
        addFinItem(sPro, 191, Comm.ACCNO, aC100.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 192, Comm.ACCNO, aC101.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 193, Comm.ACCNO, aC102.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 194, Comm.ACCNO, aC103.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 195, Comm.ACCNO, aC104.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 196, Comm.ACCNO, aC105.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 200, Comm.PVAR3, "ค่าใช้จ่ายในการขาย", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.PLUS, Operand.CLEAR, Operand.BLANK);
        addFinItem(sPro, 210, Comm.TXT, "ค่าใช้จ่ายในการบริหาร", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 221, Comm.ACCNO, aC108.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 222, Comm.ACCNO, aC109.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 223, Comm.ACCNO, aC110.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 224, Comm.ACCNO, aC111.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 225, Comm.ACCNO, aC112.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 230, Comm.PVAR3, "ค่าใช้จ่ายเกี่ยวกับพนักงาน", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.CLEAR, Operand.PLUS);
        addFinItem(sPro, 241, Comm.ACCNO, aC119.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 242, Comm.ACCNO, aC120.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 243, Comm.ACCNO, aC121.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 244, Comm.ACCNO, aC122.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 245, Comm.ACCNO, aC123.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 246, Comm.ACCNO, aC124.getKeyString(), CalCon.CAL, PrintCon.NOPRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK);
        addFinItem(sPro, 250, Comm.PVAR3, "ค่าใช้จ่ายสำนักงาน", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.BLANK, Operand.CLEAR, Operand.PLUS);
        addFinItem(sPro, 260, Comm.PVAR4, "รวมค่าใช้จ่ายในการบริหาร", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.BLANK, Operand.PLUS, Operand.BLANK, Operand.CLEAR);
        addFinItem(sPro, 270, Comm.PVAR2, "รวมค่าใช้จ่าย", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.MINUS, Operand.CLEAR, Operand.BLANK, Operand.BLANK);
        addFinItem(sPro, 280, Comm.PVAR1, "กำไร (ขาดทุน)", CalCon.CAL, PrintCon.PRINT, PrintStyle.BLANK, Operand.CLEAR, Operand.BLANK, Operand.BLANK, Operand.BLANK);
        
        FinHeader pro = Db.addFinHeader(pm, fisKeyString, sPro);
    }
    
    private static void addFinItem(SFinHeader sFin, int seq, Comm comm, String arg, CalCon calCon, PrintCon printCon, PrintStyle printStyle, 
            Operand var1, Operand var2, Operand var3, Operand var4){
        SFinItem sFinItem = new SFinItem(null, seq, comm, arg, calCon, printCon, printStyle, var1, var2, var3, var4);
        sFin.addSFinItem(sFinItem);
    }

    private static JournalType addJT(Db<JournalType> jTDb, PersistenceManager pm, final String fisKeyString, final String name, 
            final String shortName, final Date d){
        JournalType jT = jTDb.add(pm, new DbAddCallback<JournalType>() {
            @Override
            public JournalType construct() {
                return new JournalType(fisKeyString, name, shortName, d);
            }
        });
        d.setTime(d.getTime() + 1);
        return jT;
    }

    private static DocType addDT(Db<DocType> dTDb, PersistenceManager pm, final String fisKeyString, final String jTKeyString, final String code, 
            final String name, final String jDesc, final Date d){
        DocType dT = dTDb.add(pm, new DbAddCallback<DocType>() {
            @Override
            public DocType construct() {
                return new DocType(fisKeyString, jTKeyString, code, name, jDesc, d);
            }
        });
        d.setTime(d.getTime() + 1);
        return dT;
    }

    private static AccGroup addAG(Db<AccGroup> aGDb, PersistenceManager pm, final String fisKeyString, final String name, final Date d){
        AccGroup aG = aGDb.add(pm, new DbAddCallback<AccGroup>() {
            @Override
            public AccGroup construct() {
                return new AccGroup(fisKeyString, name, d);
            }
        });
        d.setTime(d.getTime() + 1);
        return aG;
    }
    
    private static AccChart addAC(Db<AccChart> aCDb, PersistenceManager pm, final String fisKeyString, final String no, final String name, 
            final String aGKeyString, final int level, final AccType type, final String parentACKeyString){
        AccChart aC = aCDb.add(pm, new DbAddCallback<AccChart>() {
            @Override
            public AccChart construct() {
                return new AccChart(fisKeyString, aGKeyString, parentACKeyString, no, name, type, level, 0);
            }
        });
        return aC;
    }
}
