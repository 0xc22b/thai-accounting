package gwt.server.account;

import gwt.shared.Utils;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CreateSetup {

    @SuppressWarnings("unused")
    public static void createInEnglish(Connection conn, long fiscalYearId)
            throws SQLException {

        PreparedStatement addJTStatement = Db.getAddJTPreparedStatement(conn, fiscalYearId);
        long gJT = Db.addJT(addJTStatement, "general", "ge");
        long pJT = Db.addJT(addJTStatement, "pay", "pay");
        long rJT = Db.addJT(addJTStatement, "receive", "rec");
        long sJT = Db.addJT(addJTStatement, "sale", "sale");
        long bJT = Db.addJT(addJTStatement, "buy", "buy");

        PreparedStatement addDTStatement = Db.getAddDTPreparedStatement(conn, fiscalYearId);
        long gDT = Db.addDT(addDTStatement, gJT, "JV", "general", "");
        long pDT = Db.addDT(addDTStatement, pJT, "PV", "pay", "");
        long rDT = Db.addDT(addDTStatement, rJT, "RV", "receive", "");
        long sDT = Db.addDT(addDTStatement, sJT, "SV", "sale", "");
        long bDT = Db.addDT(addDTStatement, bJT, "UV", "buy", "");

        PreparedStatement addAGStatement = Db.getAddAGPreparedStatement(conn, fiscalYearId);
        long wAG = Db.addAG(addAGStatement, "asset");
        long dAG = Db.addAG(addAGStatement, "debt");
        long fAG = Db.addAG(addAGStatement, "budget");
        long rAG = Db.addAG(addAGStatement, "income");
        long eAG = Db.addAG(addAGStatement, "expense");

        PreparedStatement addACStatement = Db.getAddACPreparedStatement(conn, fiscalYearId);
        long aC1 = Db.addAC(addACStatement, "10-00-00-00", "assets", wAG, 1, AccType.CONTROL, 0);
        long aC2 = Db.addAC(addACStatement, "11-00-00-00", "live assets", wAG, 2, AccType.CONTROL, aC1);
        long aC3 = Db.addAC(addACStatement, "11-01-00-00", "cash and savings", wAG, 3, AccType.CONTROL, aC2);
        long aC4 = Db.addAC(addACStatement, "11-01-01-00", "cash", wAG, 4, AccType.ENTRY, aC3);
        long aC5 = Db.addAC(addACStatement, "11-01-02-00", "current accounts", wAG, 4, AccType.CONTROL, aC3);
        long aC6 = Db.addAC(addACStatement, "11-01-02-01", "current account no. 999-9-9999-1", wAG, 5, AccType.ENTRY, aC5);
        long aC9 = Db.addAC(addACStatement, "11-01-03-00", "saving accounts", wAG, 4, AccType.CONTROL, aC3);
        long aC10 = Db.addAC(addACStatement, "11-01-03-01", "saving accounts no. 999-9-9999-1", wAG, 5, AccType.ENTRY, aC9);
        long aC22 = Db.addAC(addACStatement, "11-04-00-00", "remaining products", wAG, 3, AccType.CONTROL, aC2);
        long aC23 = Db.addAC(addACStatement, "11-04-01-00", "remaining materials", wAG, 4, AccType.ENTRY, aC22);
        long aC26 = Db.addAC(addACStatement, "11-05-00-00", "other assets", wAG, 3, AccType.CONTROL, aC2);
        long aC27 = Db.addAC(addACStatement, "11-05-01-00", "advanced expenses", wAG, 4, AccType.CONTROL, aC26);
        long aC28 = Db.addAC(addACStatement, "11-05-01-01", "advanced expenses-products", wAG, 5, AccType.ENTRY, aC27);
        long aC29 = Db.addAC(addACStatement, "11-05-01-02", "advanced tax", wAG, 5, AccType.ENTRY, aC27);
        long aC30 = Db.addAC(addACStatement, "11-05-01-03", "others advanced expenses", wAG, 5, AccType.ENTRY, aC27);

        long aC57 = Db.addAC(addACStatement, "20-00-00-00", "debts", dAG, 1, AccType.CONTROL, 0);
        long aC58 = Db.addAC(addACStatement, "21-00-00-00", "live debts", dAG, 2, AccType.CONTROL, aC57);
        long aC59 = Db.addAC(addACStatement, "21-02-00-00", "creditors and checks", dAG, 3, AccType.CONTROL, aC58);
        long aC60 = Db.addAC(addACStatement, "21-02-01-00", "creditors", dAG, 4, AccType.ENTRY, aC59);
        long aC61 = Db.addAC(addACStatement, "21-02-02-00", "advance checks", dAG, 4, AccType.ENTRY, aC59);
        long aC62 = Db.addAC(addACStatement, "21-03-00-00", "other debts", dAG, 3, AccType.CONTROL, aC58);
        long aC63 = Db.addAC(addACStatement, "21-03-01-00", "pending expenses", dAG, 4, AccType.CONTROL, aC62);
        long aC64 = Db.addAC(addACStatement, "21-03-01-01", "phone expenses", dAG, 5, AccType.ENTRY, aC63);
        long aC65 = Db.addAC(addACStatement, "21-03-01-02", "electric expenses", dAG, 5, AccType.ENTRY, aC63);

        long aC73 = Db.addAC(addACStatement, "30-00-00-00", "shareholdors", fAG, 1, AccType.CONTROL, 0);
        long aC74 = Db.addAC(addACStatement, "31-00-00-00", "budget", fAG, 2, AccType.ENTRY, aC73);
        long aC75 = Db.addAC(addACStatement, "32-00-00-00", "cumulative profit(loss)", fAG, 2, AccType.ENTRY, aC73);
        long aC76 = Db.addAC(addACStatement, "33-00-00-00", "profit(loss)", fAG, 2, AccType.ENTRY, aC73);

        long aC77 = Db.addAC(addACStatement, "40-00-00-00", "income", rAG, 1, AccType.CONTROL, 0);
        long aC78 = Db.addAC(addACStatement, "41-00-00-00", "income from products", rAG, 2, AccType.CONTROL, aC77);
        long aC79 = Db.addAC(addACStatement, "41-01-00-00", "income from sale", rAG, 3, AccType.ENTRY, aC78);
        long aC80 = Db.addAC(addACStatement, "41-02-00-00", "returned products", rAG, 3, AccType.ENTRY, aC78);
        long aC81 = Db.addAC(addACStatement, "41-03-00-00", "discount", rAG, 3, AccType.ENTRY, aC78);
        long aC82 = Db.addAC(addACStatement, "42-00-00-00", "other income", rAG, 2, AccType.CONTROL, aC77);
        long aC83 = Db.addAC(addACStatement, "42-01-00-00", "income from remnant", rAG, 3, AccType.ENTRY, aC82);
        long aC84 = Db.addAC(addACStatement, "42-02-00-00", "interest", rAG, 3, AccType.ENTRY, aC82);

        long aC91 = Db.addAC(addACStatement, "50-00-00-00", "total expenses", eAG, 1, AccType.CONTROL, 0);
        long aC92 = Db.addAC(addACStatement, "51-00-00-00", "total cost", eAG, 2, AccType.CONTROL, aC91);
        long aC93 = Db.addAC(addACStatement, "51-01-00-00", "cost", eAG, 3, AccType.ENTRY, aC92);
        long aC94 = Db.addAC(addACStatement, "51-03-00-00", "total buy", eAG, 3, AccType.CONTROL, aC92);
        long aC95 = Db.addAC(addACStatement, "51-03-01-00", "buy", eAG, 4, AccType.ENTRY, aC94);
        long aC99 = Db.addAC(addACStatement, "52-00-00-00", "sale expenses", eAG, 2, AccType.CONTROL, aC91);
        long aC100 = Db.addAC(addACStatement, "52-01-00-00", "gas", eAG, 3, AccType.ENTRY, aC99);
        long aC101 = Db.addAC(addACStatement, "52-02-00-00", "transportation", eAG, 3, AccType.ENTRY, aC99);
        long aC105 = Db.addAC(addACStatement, "52-06-00-00", "ad fee", eAG, 3, AccType.ENTRY, aC99);
        long aC106 = Db.addAC(addACStatement, "53-00-00-00", "managing expenses", eAG, 2, AccType.CONTROL, aC91);
        long aC107 = Db.addAC(addACStatement, "53-01-00-00", "employee expenses", eAG, 3, AccType.CONTROL, aC106);
        long aC108 = Db.addAC(addACStatement, "53-01-01-00", "salary", eAG, 4, AccType.ENTRY, aC107);
    }

    @SuppressWarnings("unused")
    public static void createInThai(Connection conn, final long fiscalYearId)
            throws SQLException {

        PreparedStatement addJTStatement = Db.getAddJTPreparedStatement(conn, fiscalYearId);
        long gJT = Db.addJT(addJTStatement, "รายวันทั่วไป", "ทป.");
        long pJT = Db.addJT(addJTStatement, "รายวันจ่าย", "จ่าย");
        long rJT = Db.addJT(addJTStatement, "รายวันรับ", "รับ");
        long sJT = Db.addJT(addJTStatement, "รายวันขาย", "ขาย");
        long bJT = Db.addJT(addJTStatement, "รายวันซื้อ", "ซื้อ");

        PreparedStatement addDTStatement = Db.getAddDTPreparedStatement(conn, fiscalYearId);
        long gDT = Db.addDT(addDTStatement, gJT, "JV", "ทั่วไป", "");
        long pDT = Db.addDT(addDTStatement, pJT, "PV", "จ่าย", "");
        long rDT = Db.addDT(addDTStatement, rJT, "RV", "รับ", "");
        long sDT = Db.addDT(addDTStatement, sJT, "SV", "ขาย", "");
        long bDT = Db.addDT(addDTStatement, bJT, "UV", "ซื้อ", "");

        PreparedStatement addAGStatement = Db.getAddAGPreparedStatement(conn, fiscalYearId);
        long wAG = Db.addAG(addAGStatement, "ส/ท");
        long dAG = Db.addAG(addAGStatement, "หนี้สิน");
        long fAG = Db.addAG(addAGStatement, "ทุน");
        long rAG = Db.addAG(addAGStatement, "รายได้");
        long eAG = Db.addAG(addAGStatement, "คชจ.");

        PreparedStatement addACStatement = Db.getAddACPreparedStatement(conn, fiscalYearId);
        long aC1 = Db.addAC(addACStatement, "10-00-00-00", "สินทรัพย์", wAG, 1, AccType.CONTROL, 0);
        long aC2 = Db.addAC(addACStatement, "11-00-00-00", "สินทรัพย์หมุนเวียน", wAG, 2, AccType.CONTROL, aC1);
        long aC3 = Db.addAC(addACStatement, "11-01-00-00", "เงินสดและเงินฝากธนาคาร", wAG, 3, AccType.CONTROL, aC2);
        long aC4 = Db.addAC(addACStatement, "11-01-01-00", "เงินสด", wAG, 4, AccType.ENTRY, aC3);
        long aC5 = Db.addAC(addACStatement, "11-01-02-00", "เงินฝากกระแสรายวัน", wAG, 4, AccType.CONTROL, aC3);
        long aC6 = Db.addAC(addACStatement, "11-01-02-01", "เงินฝากกระแสรายวัน    999-9-9999-1", wAG, 5, AccType.ENTRY, aC5);
        long aC7 = Db.addAC(addACStatement, "11-01-02-02", "เงินฝากกระแสรายวัน    999-9-9999-2", wAG, 5, AccType.ENTRY, aC5);
        long aC8 = Db.addAC(addACStatement, "11-01-02-03", "เงินฝากกระแสรายวัน    999-9-9999-3", wAG, 5, AccType.ENTRY, aC5);
        long aC9 = Db.addAC(addACStatement, "11-01-03-00", "เงินฝากออมทรัพย์", wAG, 4, AccType.CONTROL, aC3);
        long aC10 = Db.addAC(addACStatement, "11-01-03-01", "เงินฝากออมทรัพย์     999-9-9999-1", wAG, 5, AccType.ENTRY, aC9);
        long aC11 = Db.addAC(addACStatement, "11-01-03-02", "เงินฝากออมทรัพย์     999-9-9999-2", wAG, 5, AccType.ENTRY, aC9);
        long aC12 = Db.addAC(addACStatement, "11-01-03-03", "เงินฝากออมทรัพย์     999-9-9999-3", wAG, 5, AccType.ENTRY, aC9);
        long aC13 = Db.addAC(addACStatement, "11-01-04-00", "เงินฝากประจำ", wAG, 4, AccType.CONTROL, aC3);
        long aC14 = Db.addAC(addACStatement, "11-01-04-01", "เงินฝากประจำ     999-9-9999-1 ", wAG, 5, AccType.ENTRY, aC13);
        long aC15 = Db.addAC(addACStatement, "11-01-04-02", "เงินฝากประจำ     999-9-9999-2 ", wAG, 5, AccType.ENTRY, aC13);
        long aC16 = Db.addAC(addACStatement, "11-01-04-03", "เงินฝากประจำ     999-9-9999-3 ", wAG, 5, AccType.ENTRY, aC13);
        long aC17 = Db.addAC(addACStatement, "11-02-00-00", "ลูกหนี้การค้าและตั๋วเงินรับ", wAG, 3, AccType.CONTROL, aC2);
        long aC18 = Db.addAC(addACStatement, "11-02-01-00", "ลูกหนี้การค้า", wAG, 4, AccType.ENTRY, aC17);
        long aC19 = Db.addAC(addACStatement, "11-02-02-00", "เช็ครับลงวันที่ล่วงหน้า", wAG, 4, AccType.ENTRY, aC17);
        long aC20 = Db.addAC(addACStatement, "11-02-03-00", "สำรองหนี้สูญ", wAG, 4, AccType.ENTRY, aC17);
        long aC21 = Db.addAC(addACStatement, "11-02-04-00", "ลูกหนี้อื่นๆ", wAG, 4, AccType.ENTRY, aC17);
        long aC22 = Db.addAC(addACStatement, "11-04-00-00", "สินค้าคงเหลือ", wAG, 3, AccType.CONTROL, aC2);
        long aC23 = Db.addAC(addACStatement, "11-04-01-00", "วัตถุดิบคงเหลือ", wAG, 4, AccType.ENTRY, aC22);
        long aC24 = Db.addAC(addACStatement, "11-04-02-00", "สินค้าสำเร็จรูปคงเหลือ", wAG, 4, AccType.ENTRY, aC22);
        long aC25 = Db.addAC(addACStatement, "11-04-03-00", "งานระหว่างทำ", wAG, 4, AccType.ENTRY, aC22);
        long aC26 = Db.addAC(addACStatement, "11-05-00-00", "สินทรัพย์หมุนเวียนอื่นๆ", wAG, 3, AccType.CONTROL, aC2);
        long aC27 = Db.addAC(addACStatement, "11-05-01-00", "ค่าใช้จ่ายจ่ายล่วงหน้า", wAG, 4, AccType.CONTROL, aC26);
        long aC28 = Db.addAC(addACStatement, "11-05-01-01", "ค่าใช้จ่ายจ่ายล่วงหน้า-ค่าสินค้า", wAG, 5, AccType.ENTRY, aC27);
        long aC29 = Db.addAC(addACStatement, "11-05-01-02", "ภาษีนิติบุคคลจ่ายล่วงหน้า", wAG, 5, AccType.ENTRY, aC27);
        long aC30 = Db.addAC(addACStatement, "11-05-01-03", "ค่าใช้จ่ายล่วงหน้าอื่นๆ", wAG, 5, AccType.ENTRY, aC27);
        long aC31 = Db.addAC(addACStatement, "11-05-02-00", "เงินทดลองจ่ายพนักงาน", wAG, 4, AccType.ENTRY, aC26);
        long aC32 = Db.addAC(addACStatement, "11-05-03-00", "รายได้ค้างรับ", wAG, 4, AccType.ENTRY, aC26);
        long aC33 = Db.addAC(addACStatement, "11-05-04-00", "ภาษีมูลค่าเพิ่ม", wAG, 4, AccType.CONTROL, aC26);
        long aC34 = Db.addAC(addACStatement, "11-05-04-01", "ภาษีซื้อ", wAG, 5, AccType.ENTRY, aC33);
        long aC35 = Db.addAC(addACStatement, "11-05-04-02", "ภาษีขาย", wAG, 5, AccType.ENTRY, aC33);
        long aC36 = Db.addAC(addACStatement, "11-05-04-03", "ภาษีขาย-รอเรียกเก็บ", wAG, 5, AccType.ENTRY, aC33);
        long aC37 = Db.addAC(addACStatement, "11-05-04-04", "ภาษีซื้อ-ยังไม่ถึงกำหนด", wAG, 5, AccType.ENTRY, aC33);
        long aC38 = Db.addAC(addACStatement, "11-05-05-00", "ลูกหนี้-กรมสรรพากร", wAG, 4, AccType.ENTRY, aC26);

        long aC39N = Db.addAC(addACStatement, "12-00-00-00", "สินทรัพย์ไม่หมุนเวียน", wAG, 2, AccType.CONTROL, aC1);
        long aC39 = Db.addAC(addACStatement, "12-01-00-00", "ลูกหนี้เงินให้กู้ยืมแก่กรรมการและลูกจ้าง", wAG, 3, AccType.CONTROL, aC39N);
        long aC40 = Db.addAC(addACStatement, "12-01-01-00", "ลูกหนี้เงินให้กู้ยืม-นาย...", wAG, 4, AccType.ENTRY, aC39);
        long aC41 = Db.addAC(addACStatement, "12-03-00-00", "เงินลงทุนในบริษัทในเครือ", wAG, 3, AccType.CONTROL, aC39N);
        long aC42 = Db.addAC(addACStatement, "12-04-00-00", "ที่ดิน อาคารและอุปกรณ์สิทธิ", wAG, 3, AccType.CONTROL, aC39N);
        long aC43 = Db.addAC(addACStatement, "12-04-01-00", "ที่ดิน อาคาร ยานพาหนะและอุปกรณ์สิทธิ", wAG, 4, AccType.CONTROL, aC42);
        long aC44 = Db.addAC(addACStatement, "12-04-01-01", "ที่ดิน", wAG, 5, AccType.ENTRY, aC43);
        long aC45 = Db.addAC(addACStatement, "12-04-01-02", "อาคาร", wAG, 5, AccType.ENTRY, aC43);
        long aC46 = Db.addAC(addACStatement, "12-04-01-03", "อุปกรณ์สำนักงาน", wAG, 5, AccType.ENTRY, aC43);
        long aC47 = Db.addAC(addACStatement, "12-04-01-04", "ยานพาหนะ", wAG, 5, AccType.ENTRY, aC43);
        long aC48 = Db.addAC(addACStatement, "12-04-02-00", "ค่าเสื่อมราคาสะสม", wAG, 4, AccType.CONTROL, aC42);
        long aC49 = Db.addAC(addACStatement, "12-04-02-01", "ค่าเสื่อมราคาสะสม-อาคาร", wAG, 5, AccType.ENTRY, aC48);
        long aC50 = Db.addAC(addACStatement, "12-04-02-02", "ค่าเสื่อมราคาสะสม-อุปกรณ์สำนักงาน", wAG, 5, AccType.ENTRY, aC48);
        long aC51 = Db.addAC(addACStatement, "12-04-02-03", "ค่าเสื่อมราคาสะสม-ยานพาหนะ", wAG, 5, AccType.ENTRY, aC48);
        long aC52 = Db.addAC(addACStatement, "12-05-00-00", "สินทรัพย์อื่นๆ", wAG, 3, AccType.CONTROL, aC39N);
        long aC53 = Db.addAC(addACStatement, "12-05-01-00", "กรมธรรม์ประกันอัคคีภัย-สินค้าและอาคาร", wAG, 4, AccType.ENTRY, aC52);
        long aC54 = Db.addAC(addACStatement, "12-05-02-00", "กรมธรรม์ประกันอัคคีภัย-ยานพาหนะ", wAG, 4, AccType.ENTRY, aC52);
        long aC55 = Db.addAC(addACStatement, "12-05-03-00", "กรมธรรม์ประกันอุบัติเหตุพนักงาน", wAG, 4, AccType.ENTRY, aC52);
        long aC56 = Db.addAC(addACStatement, "12-05-04-00", "พันธบัตรโทรศัพท์", wAG, 4, AccType.ENTRY, aC52);

        long aC57 = Db.addAC(addACStatement, "20-00-00-00", "หนี้สิน", dAG, 1, AccType.CONTROL, 0);
        long aC58 = Db.addAC(addACStatement, "21-00-00-00", "หนี้สินหมุนเวียน", dAG, 2, AccType.CONTROL, aC57);
        long aC59 = Db.addAC(addACStatement, "21-02-00-00", "เจ้าหนี้การค้าและตั๋วเงินจ่าย", dAG, 3, AccType.CONTROL, aC58);
        long aC60 = Db.addAC(addACStatement, "21-02-01-00", "เจ้าหนี้การค้า", dAG, 4, AccType.ENTRY, aC59);
        long aC61 = Db.addAC(addACStatement, "21-02-02-00", "เช็คจ่ายล่วงหน้า", dAG, 4, AccType.ENTRY, aC59);
        long aC62 = Db.addAC(addACStatement, "21-03-00-00", "หนี้สินหมุนเวียนอื่น", dAG, 3, AccType.CONTROL, aC58);
        long aC63 = Db.addAC(addACStatement, "21-03-01-00", "ค่าใช้จ่ายค้างจ่าย", dAG, 4, AccType.CONTROL, aC62);
        long aC64 = Db.addAC(addACStatement, "21-03-01-01", "ค่าใช้จ่ายค้างจ่าย-ค่าโทรศัพท์", dAG, 5, AccType.ENTRY, aC63);
        long aC65 = Db.addAC(addACStatement, "21-03-01-02", "ค่าใช้จ่ายค้างจ่าย-ค่าไฟฟ้าและประปา", dAG, 5, AccType.ENTRY, aC63);
        long aC66 = Db.addAC(addACStatement, "21-03-02-00", "ดอกเบี้ยเงินกู้", dAG, 4, AccType.ENTRY, aC62);
        long aC67 = Db.addAC(addACStatement, "21-03-03-00", "ภาษีเงินได้หัก ณ ที่จ่ายค้างจ่าย", dAG, 4, AccType.ENTRY, aC62);
        long aC68 = Db.addAC(addACStatement, "21-03-04-00", "คชจ.ค้างจ่ายอื่นๆ", dAG, 4, AccType.ENTRY, aC62);
        long aC69 = Db.addAC(addACStatement, "21-03-05-00", "รายได้รับล่วงหน้า", dAG, 4, AccType.ENTRY, aC62);
        long aC70 = Db.addAC(addACStatement, "21-03-06-00", "เจ้าหนี้กรมสรรพากร", dAG, 4, AccType.ENTRY, aC62);
        long aC71 = Db.addAC(addACStatement, "22-00-00-00", "หนี้สินอื่นๆ", dAG, 2, AccType.CONTROL, aC57);
        long aC72 = Db.addAC(addACStatement, "22-01-00-00", "หนี้สินอื่นๆ", dAG, 3, AccType.ENTRY, aC71);

        long aC73 = Db.addAC(addACStatement, "30-00-00-00", "ส่วนของผู้ถือหุ้น", fAG, 1, AccType.CONTROL, 0);
        long aC74 = Db.addAC(addACStatement, "31-00-00-00", "ทุน", fAG, 2, AccType.CONTROL, aC73);
        long aC74_1 = Db.addAC(addACStatement, "31-00-00-01", "ทุน-นาย ก.", fAG, 3, AccType.ENTRY, aC74);
        long aC74_2 = Db.addAC(addACStatement, "31-00-00-02", "ทุน-นาย ข.", fAG, 3, AccType.ENTRY, aC74);
        long aC75 = Db.addAC(addACStatement, "32-00-00-00", "กำไร(ขาดทุน)สะสม", fAG, 2, AccType.ENTRY, aC73);

        long aC77 = Db.addAC(addACStatement, "40-00-00-00", "รายได้", rAG, 1, AccType.CONTROL, 0);
        long aC78 = Db.addAC(addACStatement, "41-00-00-00", "รายได้จากการขายสินค้า-สุทธิ", rAG, 2, AccType.CONTROL, aC77);
        long aC79 = Db.addAC(addACStatement, "41-01-00-00", "รายได้จากการขาย", rAG, 3, AccType.ENTRY, aC78);
        long aC80 = Db.addAC(addACStatement, "41-02-00-00", "รับคืนสินค้า", rAG, 3, AccType.ENTRY, aC78);
        long aC81 = Db.addAC(addACStatement, "41-03-00-00", "ส่วนลดจ่าย", rAG, 3, AccType.ENTRY, aC78);
        long aC82 = Db.addAC(addACStatement, "42-00-00-00", "รายได้อื่นๆ", rAG, 2, AccType.CONTROL, aC77);
        long aC83 = Db.addAC(addACStatement, "42-01-00-00", "รายได้จากการขายเศษวัสดุ", rAG, 3, AccType.ENTRY, aC82);
        long aC84 = Db.addAC(addACStatement, "42-02-00-00", "ดอกเบี้ยรับ", rAG, 3, AccType.ENTRY, aC82);
        long aC85 = Db.addAC(addACStatement, "42-03-00-00", "กำไร(ขาดทุน)จากการจำหน่ายทรัพย์สิน", rAG, 3, AccType.ENTRY, aC82);
        long aC86 = Db.addAC(addACStatement, "42-04-00-00", "ส่วนลดเงินสดรับ", rAG, 3, AccType.ENTRY, aC82);
        long aC87 = Db.addAC(addACStatement, "42-05-00-00", "รายได้อื่นๆ", rAG, 3, AccType.ENTRY, aC82);
        long aC88 = Db.addAC(addACStatement, "42-06-00-00", "รายได้จากการปรับแบบฟอร์ม", rAG, 3, AccType.ENTRY, aC82);
        long aC89 = Db.addAC(addACStatement, "42-07-00-00", "รายได้ให้เช่าที่จอดรถ", rAG, 3, AccType.ENTRY, aC82);
        long aC90 = Db.addAC(addACStatement, "42-08-00-00", "รายได้บริการให้คำปรึกษา", rAG, 3, AccType.ENTRY, aC82);

        long aC91 = Db.addAC(addACStatement, "50-00-00-00", "ค่าใช้จ่าย", eAG, 1, AccType.CONTROL, 0);
        long aC92 = Db.addAC(addACStatement, "51-00-00-00", "ต้นทุนขายสุทธิ", eAG, 2, AccType.CONTROL, aC91);
        long aC93 = Db.addAC(addACStatement, "51-01-00-00", "ต้นทุนสินค้าเพื่อขาย", eAG, 3, AccType.ENTRY, aC92);
        long aC94 = Db.addAC(addACStatement, "51-03-00-00", "ซื้อสุทธิ", eAG, 3, AccType.CONTROL, aC92);
        long aC95 = Db.addAC(addACStatement, "51-03-01-00", "ซื้อ", eAG, 4, AccType.ENTRY, aC94);
        long aC96 = Db.addAC(addACStatement, "51-03-02-00", "ส่วนลดรับ", eAG, 4, AccType.ENTRY, aC94);
        long aC97 = Db.addAC(addACStatement, "51-03-03-00", "ส่งคืนสินค้า", eAG, 4, AccType.ENTRY, aC94);
        long aC98 = Db.addAC(addACStatement, "51-05-00-00", "สินค้าตัวอย่าง", eAG, 3, AccType.ENTRY, aC92);
        long aC99 = Db.addAC(addACStatement, "52-00-00-00", "ค่าใช้จ่ายในการขาย", eAG, 2, AccType.CONTROL, aC91);
        long aC100 = Db.addAC(addACStatement, "52-01-00-00", "ค่าน้ำมันรถ", eAG, 3, AccType.ENTRY, aC99);
        long aC101 = Db.addAC(addACStatement, "52-02-00-00", "ค่าขนส่ง", eAG, 3, AccType.ENTRY, aC99);
        long aC102 = Db.addAC(addACStatement, "52-03-00-00", "คชจ.ในการขาย-ค่าติดต่อสื่อสาร", eAG, 3, AccType.ENTRY, aC99);
        long aC103 = Db.addAC(addACStatement, "52-04-00-00", "คชจ.ในการขาย-ค่าโทรศัพท์", eAG, 3, AccType.ENTRY, aC99);
        long aC104 = Db.addAC(addACStatement, "52-05-00-00", "ค่าใช้จ่าย-เบิกสินค้าตัวอย่าง", eAG, 3, AccType.ENTRY, aC99);
        long aC105 = Db.addAC(addACStatement, "52-06-00-00", "ค่าโฆษณา", eAG, 3, AccType.ENTRY, aC99);
        long aC106 = Db.addAC(addACStatement, "53-00-00-00", "ค่าใช้จ่ายในการบริหาร", eAG, 2, AccType.CONTROL, aC91);
        long aC107 = Db.addAC(addACStatement, "53-01-00-00", "ค่าใช้จ่ายเกี่ยวกับพนักงาน", eAG, 3, AccType.CONTROL, aC106);
        long aC108 = Db.addAC(addACStatement, "53-01-01-00", "เงินเดือน", eAG, 4, AccType.ENTRY, aC107);
        long aC109 = Db.addAC(addACStatement, "53-01-02-00", "คอมมิชชั่น", eAG, 4, AccType.ENTRY, aC107);
        long aC110 = Db.addAC(addACStatement, "53-01-03-00", "ค่าเบี้ยเลี้ยง", eAG, 4, AccType.ENTRY, aC107);
        long aC111 = Db.addAC(addACStatement, "53-01-04-00", "ค่าล่วงเวลาพนักงาน", eAG, 4, AccType.ENTRY, aC107);
        long aC112 = Db.addAC(addACStatement, "53-01-05-00", "ค่ารักษาพยาบาล", eAG, 4, AccType.ENTRY, aC107);
        long aC113 = Db.addAC(addACStatement, "53-02-00-00", "ค่าภาษีและค่าธรรมเนียม", eAG, 3, AccType.CONTROL, aC106);
        long aC114 = Db.addAC(addACStatement, "53-02-01-00", "ภาษีเงินได้นิติบุคคล", eAG, 4, AccType.ENTRY, aC113);
        long aC115 = Db.addAC(addACStatement, "53-02-02-00", "ค่าธรรมเนียมเรียกเก็บ", eAG, 4, AccType.ENTRY, aC113);
        long aC116 = Db.addAC(addACStatement, "53-02-03-00", "ค่าธรรมเนียมหนังสือค้ำประกัน", eAG, 4, AccType.ENTRY, aC113);
        long aC117 = Db.addAC(addACStatement, "53-02-04-00", "ค่าตรวจสอบบัญชีและปรึกษากฎหมาย", eAG, 4, AccType.ENTRY, aC113);
        long aC118 = Db.addAC(addACStatement, "53-03-00-00", "ค่าใช้จ่ายสำนักงาน", eAG, 3, AccType.CONTROL, aC106);
        long aC119 = Db.addAC(addACStatement, "53-03-01-00", "ค่าวารสารและสมาชิก", eAG, 4, AccType.ENTRY, aC118);
        long aC120 = Db.addAC(addACStatement, "53-03-02-00", "ค่าซ่อมแซม", eAG, 4, AccType.ENTRY, aC118);
        long aC121 = Db.addAC(addACStatement, "53-03-03-00", "ค่าน้ำประปาและไฟฟ้า", eAG, 4, AccType.ENTRY, aC118);
        long aC122 = Db.addAC(addACStatement, "53-03-04-00", "ค่าโทรศัพท์", eAG, 4, AccType.ENTRY, aC118);
        long aC123 = Db.addAC(addACStatement, "53-03-05-00", "ค่าเสื่อมราคา", eAG, 4, AccType.ENTRY, aC118);
        long aC124 = Db.addAC(addACStatement, "53-03-06-00", "วัสดุสำนักงานใช้ไป", eAG, 4, AccType.ENTRY, aC118);
        long aC125 = Db.addAC(addACStatement, "53-04-00-00", "ค่าประกันภัย", eAG, 3, AccType.CONTROL, aC106);
        long aC126 = Db.addAC(addACStatement, "53-04-01-00", "ค่าประกันอัคคีภัย", eAG, 4, AccType.ENTRY, aC125);
        long aC127 = Db.addAC(addACStatement, "53-04-02-00", "ค่าประกันอุบัติเหตุ", eAG, 4, AccType.ENTRY, aC125);
        long aC128 = Db.addAC(addACStatement, "53-05-00-00", "ค่าใช้จ่ายอื่นๆ", eAG, 3, AccType.CONTROL, aC106);
        long aC129 = Db.addAC(addACStatement, "53-05-01-00", "ดอกเบี้ยจ่าย", eAG, 4, AccType.ENTRY, aC128);
        long aC130 = Db.addAC(addACStatement, "53-05-02-00", "ค่าใช้จ่ายเบ็ดเตล็ด", eAG, 4, AccType.ENTRY, aC128);
        long aC131 = Db.addAC(addACStatement, "53-05-03-00", "ส่วนลดเงินสดจ่าย", eAG, 4, AccType.ENTRY, aC128);
        long aC132 = Db.addAC(addACStatement, "53-05-04-00", "ค่ารับรอง", eAG, 4, AccType.ENTRY, aC128);
        long aC133 = Db.addAC(addACStatement, "53-05-05-00", "ค่าการกุศล", eAG, 4, AccType.ENTRY, aC128);
        long aC134 = Db.addAC(addACStatement, "53-05-06-00", "ค่ารับรองลูกค้า", eAG, 4, AccType.ENTRY, aC128);
    }

    public static void createFromPrev(Connection conn, long comId, long fisId) throws SQLException {

        // Retrieve existing fiscal years
        String fisSql = "SELECT * FROM fiscal_year WHERE com_id = ? ORDER BY begin_month ASC, begin_year ASC";
        PreparedStatement fisStatement = conn.prepareStatement(fisSql);
        fisStatement.setLong(1, comId);
        ResultSet fisRs = fisStatement.executeQuery();

        long dbFisId = 0;
        int dbFisBeginMonth = 0;
        int dbFisBeginYear = 0;

        // Get the latest fiscal year
        while (fisRs.next()) {
            long id = fisRs.getLong(Db.ID);
            int beginMonth = fisRs.getInt(Db.BEGIN_MONTH);
            int beginYear = fisRs.getInt(Db.BEGIN_YEAR);
            if (id != fisId) {
                if (dbFisId == 0) {
                    dbFisId = id;
                    dbFisBeginMonth = beginMonth;
                    dbFisBeginYear = beginYear;
                } else {
                    if (Utils.compareDate(1, beginMonth, beginYear,
                            30, dbFisBeginMonth, dbFisBeginYear) > 0) {
                        dbFisId = id;
                        dbFisBeginMonth = beginMonth;
                        dbFisBeginYear = beginYear;
                    }
                }
            }
        }

        if (dbFisId == 0) {
            return;
        }

        // copy setups to this fiscal year

        SFiscalYear sDbFis = Db.getSetup(conn, dbFisId);

        HashMap<String, Long> jTIdMap = new HashMap<String, Long>();
        HashMap<String, Long> aGIdMap = new HashMap<String, Long>();
        HashMap<String, Long> aCIdMap = new HashMap<String, Long>();

        PreparedStatement addJTStatement = Db.getAddJTPreparedStatement(conn, fisId);
        for(SJournalType sDbJT : sDbFis.getSJournalTypeList()){
            long jTId = Db.addJT(addJTStatement, sDbJT.getName(), sDbJT.getShortName());
            jTIdMap.put(sDbJT.getKeyString(), jTId);
        }

        PreparedStatement addDTStatement = Db.getAddDTPreparedStatement(conn, fisId);
        for(SDocType sDbDT : sDbFis.getSDocTypeList()){
            long jTId = jTIdMap.get(sDbDT.getJournalTypeKeyString());
            Db.addDT(addDTStatement, jTId, sDbDT.getCode(), sDbDT.getName(), sDbDT.getJournalDesc());
        }

        PreparedStatement addAGStatement = Db.getAddAGPreparedStatement(conn, fisId);
        for(SAccGrp sDbAG : sDbFis.getSAccGrpList()){
            long aGId = Db.addAG(addAGStatement, sDbAG.getName());
            aGIdMap.put(sDbAG.getKeyString(), aGId);
        }

        // Need to sort first to make sure that parents are created before children
        sDbFis.sortSAccChartList();

        PreparedStatement addACStatement = Db.getAddACPreparedStatement(conn, fisId);
        for(SAccChart sDbAC : sDbFis.getSAccChartList()){
            long aGId = aGIdMap.get(sDbAC.getAccGroupKeyString());

            long parentACId = 0;
            if (sDbAC.getParentAccChartKeyString() != null) {
                parentACId = aCIdMap.get(sDbAC.getParentAccChartKeyString());
            }

            long aCId = Db.addAC(addACStatement, sDbAC.getNo(), sDbAC.getName(), aGId,
                    sDbAC.getLevel(), sDbAC.getType(), parentACId);
            aCIdMap.put(sDbAC.getKeyString(), aCId);
        }
    }
}
