package gwt.server.account;

import gwt.server.account.Db.DbAddCallback;
import gwt.server.account.model.AccChart;
import gwt.server.account.model.AccGroup;
import gwt.server.account.model.DocType;
import gwt.server.account.model.FiscalYear;
import gwt.server.account.model.JournalType;
import gwt.shared.Utils;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFinHeader;
import gwt.shared.model.SFinItem;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
        AccChart aC9 = addAC(aCDb, pm, fisKeyString, "11-01-03-00", "เงินฝากออมทรัพย์", wAG.getKeyString(), 4, AccType.CONTROL, aC3.getKeyString());
        AccChart aC10 = addAC(aCDb, pm, fisKeyString, "11-01-03-01", "เงินฝากออมทรัพย์     999-9-9999-1", wAG.getKeyString(), 5, AccType.ENTRY, aC9.getKeyString());
        AccChart aC11 = addAC(aCDb, pm, fisKeyString, "11-01-03-02", "เงินฝากออมทรัพย์     999-9-9999-2", wAG.getKeyString(), 5, AccType.ENTRY, aC9.getKeyString());
        AccChart aC12 = addAC(aCDb, pm, fisKeyString, "11-01-03-03", "เงินฝากออมทรัพย์     999-9-9999-3", wAG.getKeyString(), 5, AccType.ENTRY, aC9.getKeyString());
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

        AccChart aC39N = addAC(aCDb, pm, fisKeyString, "12-00-00-00", "สินทรัพย์ไม่หมุนเวียน", wAG.getKeyString(), 2, AccType.CONTROL, aC1.getKeyString());
        AccChart aC39 = addAC(aCDb, pm, fisKeyString, "12-01-00-00", "ลูกหนี้เงินให้กู้ยืมแก่กรรมการและลูกจ้าง", wAG.getKeyString(), 3, AccType.CONTROL, aC39N.getKeyString());
        AccChart aC40 = addAC(aCDb, pm, fisKeyString, "12-01-01-00", "ลูกหนี้เงินให้กู้ยืม-นาย...", wAG.getKeyString(), 4, AccType.ENTRY, aC39.getKeyString());
        AccChart aC41 = addAC(aCDb, pm, fisKeyString, "12-03-00-00", "เงินลงทุนในบริษัทในเครือ", wAG.getKeyString(), 3, AccType.CONTROL, aC39N.getKeyString());
        AccChart aC42 = addAC(aCDb, pm, fisKeyString, "12-04-00-00", "ที่ดิน อาคารและอุปกรณ์สิทธิ", wAG.getKeyString(), 3, AccType.CONTROL, aC39N.getKeyString());
        AccChart aC43 = addAC(aCDb, pm, fisKeyString, "12-04-01-00", "ที่ดิน อาคาร ยานพาหนะและอุปกรณ์สิทธิ", wAG.getKeyString(), 4, AccType.CONTROL, aC42.getKeyString());
        AccChart aC44 = addAC(aCDb, pm, fisKeyString, "12-04-01-01", "ที่ดิน", wAG.getKeyString(), 5, AccType.ENTRY, aC43.getKeyString());
        AccChart aC45 = addAC(aCDb, pm, fisKeyString, "12-04-01-02", "อาคาร", wAG.getKeyString(), 5, AccType.ENTRY, aC43.getKeyString());
        AccChart aC46 = addAC(aCDb, pm, fisKeyString, "12-04-01-03", "อุปกรณ์สำนักงาน", wAG.getKeyString(), 5, AccType.ENTRY, aC43.getKeyString());
        AccChart aC47 = addAC(aCDb, pm, fisKeyString, "12-04-01-04", "ยานพาหนะ", wAG.getKeyString(), 5, AccType.ENTRY, aC43.getKeyString());
        AccChart aC48 = addAC(aCDb, pm, fisKeyString, "12-04-02-00", "ค่าเสื่อมราคาสะสม", wAG.getKeyString(), 4, AccType.CONTROL, aC42.getKeyString());
        AccChart aC49 = addAC(aCDb, pm, fisKeyString, "12-04-02-01", "ค่าเสื่อมราคาสะสม-อาคาร", wAG.getKeyString(), 5, AccType.ENTRY, aC48.getKeyString());
        AccChart aC50 = addAC(aCDb, pm, fisKeyString, "12-04-02-02", "ค่าเสื่อมราคาสะสม-อุปกรณ์สำนักงาน", wAG.getKeyString(), 5, AccType.ENTRY, aC48.getKeyString());
        AccChart aC51 = addAC(aCDb, pm, fisKeyString, "12-04-02-03", "ค่าเสื่อมราคาสะสม-ยานพาหนะ", wAG.getKeyString(), 5, AccType.ENTRY, aC48.getKeyString());
        AccChart aC52 = addAC(aCDb, pm, fisKeyString, "12-05-00-00", "สินทรัพย์อื่นๆ", wAG.getKeyString(), 3, AccType.CONTROL, aC39N.getKeyString());
        AccChart aC53 = addAC(aCDb, pm, fisKeyString, "12-05-01-00", "กรมธรรม์ประกันอัคคีภัย-สินค้าและอาคาร", wAG.getKeyString(), 4, AccType.ENTRY, aC52.getKeyString());
        AccChart aC54 = addAC(aCDb, pm, fisKeyString, "12-05-02-00", "กรมธรรม์ประกันอัคคีภัย-ยานพาหนะ", wAG.getKeyString(), 4, AccType.ENTRY, aC52.getKeyString());
        AccChart aC55 = addAC(aCDb, pm, fisKeyString, "12-05-03-00", "กรมธรรม์ประกันอุบัติเหตุพนักงาน", wAG.getKeyString(), 4, AccType.ENTRY, aC52.getKeyString());
        AccChart aC56 = addAC(aCDb, pm, fisKeyString, "12-05-04-00", "พันธบัตรโทรศัพท์", wAG.getKeyString(), 4, AccType.ENTRY, aC52.getKeyString());
        
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
        AccChart aC74 = addAC(aCDb, pm, fisKeyString, "31-00-00-00", "ทุน", fAG.getKeyString(), 2, AccType.CONTROL, aC73.getKeyString());
        AccChart aC74_1 = addAC(aCDb, pm, fisKeyString, "31-00-00-01", "ทุน-นาย ก.", fAG.getKeyString(), 3, AccType.ENTRY, aC74.getKeyString());
        AccChart aC74_2 = addAC(aCDb, pm, fisKeyString, "31-00-00-02", "ทุน-นาย ข.", fAG.getKeyString(), 3, AccType.ENTRY, aC74.getKeyString());
        AccChart aC75 = addAC(aCDb, pm, fisKeyString, "32-00-00-00", "กำไร(ขาดทุน)สะสม", fAG.getKeyString(), 2, AccType.ENTRY, aC73.getKeyString());
        //AccChart aC76 = addAC(aCDb, pm, fisKeyString, "33-00-00-00", "กำไร(ขาดทุน)", fAG.getKeyString(), 2, AccType.ENTRY, aC73.getKeyString());
        
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
        AccChart aC130 = addAC(aCDb, pm, fisKeyString, "53-05-02-00", "ค่าใช้จ่ายเบ็ดเตล็ด", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        AccChart aC131 = addAC(aCDb, pm, fisKeyString, "53-05-03-00", "ส่วนลดเงินสดจ่าย", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        AccChart aC132 = addAC(aCDb, pm, fisKeyString, "53-05-04-00", "ค่ารับรอง", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        AccChart aC133 = addAC(aCDb, pm, fisKeyString, "53-05-05-00", "ค่าการกุศล", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
        AccChart aC134 = addAC(aCDb, pm, fisKeyString, "53-05-06-00", "ค่ารับรองลูกค้า", eAG.getKeyString(), 4, AccType.ENTRY, aC128.getKeyString());
    }

    public static void createFromPrev(PersistenceManager pm,
            String comKeyString, String fisKeyString) {
        // Retrieve existing fiscal years
        List<Key> comKeyList = new ArrayList<Key>();
        comKeyList.add(KeyFactory.stringToKey(comKeyString));
        List<FiscalYear> dbFisList = Db.getFiscalYears(pm, comKeyList);

        // Get the latest one
        FiscalYear dbFis = null;
        for (FiscalYear fis : dbFisList) {
            if (!fis.getKeyString().equals(fisKeyString)){
                if (dbFis == null) {
                    dbFis = fis;
                } else {
                    if (Utils.compareDate(1, fis.getBeginMonth(),
                            fis.getBeginYear(), 30, dbFis.getBeginMonth(),
                            dbFis.getBeginYear()) > 0) {
                        dbFis = fis;
                    }
                }
            }
        }
        
        // copy setups to this fiscal year
        if (dbFis != null) {

            Date d = new Date();
            
            String dbFisKeyString = dbFis.getKeyString();
            SFiscalYear sDbFis = Db.getSetup(pm, dbFisKeyString);

            ArrayList<JournalType> jTList = new ArrayList<JournalType>();
            ArrayList<DocType> dTList = new ArrayList<DocType>();
            ArrayList<AccGroup> aGList = new ArrayList<AccGroup>();
            ArrayList<AccChart> aCList = new ArrayList<AccChart>();

            Db<JournalType> jTDb = new Db<JournalType>();
            for(SJournalType sJT : sDbFis.getSJournalTypeList()){
                jTList.add(addJT(jTDb, pm, fisKeyString, sJT.getName(), 
                        sJT.getShortName(), d));
            }
            
            Db<DocType> dTDb = new Db<DocType>();
            for(SDocType sDT : sDbFis.getSDocTypeList()){
                
                String jTName = null;
                for (SJournalType sJT : sDbFis.getSJournalTypeList()) {
                    if (sJT.getKeyString().equals(sDT.getJournalTypeKeyString())) {
                        jTName = sJT.getName();
                        break;
                    }
                }
                
                String jTKeyString = null;
                for (JournalType jT : jTList) {
                    if (jT.getName().equals(jTName)){
                        jTKeyString = jT.getKeyString();
                    }
                }
                
                dTList.add(addDT(dTDb, pm, fisKeyString, jTKeyString,
                        sDT.getCode(), sDT.getName(),
                        sDT.getJournalDesc(), d));
            }
            
            Db<AccGroup> aGDb = new Db<AccGroup>();
            for(SAccGrp sAG : sDbFis.getSAccGrpList()){
                aGList.add(addAG(aGDb, pm, fisKeyString, sAG.getName(), d));
            }

            // Need to sort first to make sure that parents are created before children
            sDbFis.sortSAccChartList();
            Db<AccChart> aCDb = new Db<AccChart>();
            for(SAccChart sAC : sDbFis.getSAccChartList()){
                String aGName = null;
                for (SAccGrp sAG : sDbFis.getSAccGrpList()) {
                    if (sAG.getKeyString().equals(sAC.getAccGroupKeyString())) {
                        aGName = sAG.getName();
                    }
                }
                String aGKeyString = null;
                for (AccGroup aG : aGList) {
                    if (aG.getName().equals(aGName)) {
                        aGKeyString = aG.getKeyString();
                    }
                }

                String parentACNo = null;
                for (SAccChart sParentAC : sDbFis.getSAccChartList()) {
                    if (sParentAC.getKeyString().equals(
                            sAC.getParentAccChartKeyString())) {
                        parentACNo = sParentAC.getNo();
                    }
                }
                String parentACKeyString = null;
                for (AccChart aC : aCList) {
                    if (aC.getNo().equals(parentACNo)) {
                        parentACKeyString = aC.getKeyString();
                    }
                }
                
                aCList.add(addAC(aCDb, pm, fisKeyString, sAC.getNo(), sAC.getName(),
                        aGKeyString, sAC.getLevel(),
                        sAC.getType(), parentACKeyString));
            }
            
            for(SFinHeader sFH : sDbFis.getSFinHeaderList()){
                SFinHeader sNewFH = new SFinHeader(null,
                        sFH.getName(), d);
                
                for(SFinItem sFI : sFH.getSFinItemList()){
                    String arg = sFI.getArg();
                    if (sFI.getComm() == Comm.ACCNO) {
                        String aCNo = null;
                        for (SAccChart sAC : sDbFis.getSAccChartList()) {
                            if (sAC.getKeyString().equals(arg)) {
                                aCNo = sAC.getNo();
                            }
                        }
                        
                        for (AccChart ac : aCList) {
                            if (ac.getNo().equals(aCNo)) {
                                arg = ac.getKeyString();
                            }
                        }
                    }
                    addFinItem(sNewFH, sFI.getSeq(), sFI.getComm(),
                            arg,  sFI.getCalCon(), sFI.getPrintCon(),
                            sFI.getPrintStyle(), sFI.getVar1(), 
                            sFI.getVar2(), sFI.getVar3(), sFI.getVar4());
                }
                
                Db.addFinHeader(pm, fisKeyString, sNewFH);
            }
        }
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

    private static DocType addDT(Db<DocType> dTDb, PersistenceManager pm,
            final String fisKeyString, final String jTKeyString, final String code, 
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

    private static void addFinItem(SFinHeader sFin, int seq, Comm comm, String arg,
            CalCon calCon, PrintCon printCon, PrintStyle printStyle, 
            Operand var1, Operand var2, Operand var3, Operand var4){
        SFinItem sFinItem = new SFinItem(null, seq, comm, arg, calCon, printCon, printStyle, var1, var2, var3, var4);
        sFin.addSFinItem(sFinItem);
    }
}
