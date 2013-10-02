package gwt.shared;

public class SConstants {

    public static final int ADD_FIS_WITH_NO_SETUP = 0;
    public static final int ADD_FIS_WITH_DEFAULT_SETUP = 1;
    public static final int ADD_FIS_WITH_PREVIOUS_SETUP = 2;

    public static final String PDF_PATH = "pdf";

    public static final String ACTION = "action";

    public static final String JOURNAL_ACTION = "journal";
    public static final String LEDGER_ACTION = "ledger";

    public static final String FIS_KEY_STRING = "fks";
    public static final String JOURNAL_TYPE_KEY_STRING = "jtks";

    public static final String BEGIN_DAY = "bd";
    public static final String BEGIN_MONTH = "bm";
    public static final String BEGIN_YEAR = "by";
    public static final String END_DAY = "ed";
    public static final String END_MONTH = "em";
    public static final String END_YEAR = "ey";

    public static final String BEGIN_ACC_NO = "ban";
    public static final String END_ACC_NO = "ean";

    public static final String COM_NAME = "cn";
    public static final String JOURNAL_TYPE_NAME = "jtn";
    
    public static final String DO_SHOW_ALL = "dsa";

    public static final String LANG = "lang";

    public static String TOTAL(String lang) {
        if (lang.equals("th")) {
            return "รวม";
        }
        return "total";
    }

    public static String WHOLE_TOTAL(String lang) {
        if (lang.equals("th")) {
            return "รวมทั้งสิ้น";
        }
        return "Whole total";
    }

    public static String BEGIN(String lang) {
        if (lang.equals("th")) {
            return "ตั้งแต่";
        }
        return "begin";
    }

    public static String END(String lang) {
        if (lang.equals("th")) {
            return "ถึง";
        }
        return "end";
    }

    public static String ACC_NO(String lang) {
        if (lang.equals("th")) {
            return "เลขที่บัญชี";
        }
        return "Account no.";
    }

    public static String ACC_NAME(String lang) {
        if (lang.equals("th")) {
            return "ชื่อบัญชี";
        }
        return "Account name";
    }

    public static String DESC(String lang) {
        if (lang.equals("th")) {
            return "คำอธิบาย";
        }
        return "Description";
    }

    public static String DEBIT(String lang) {
        if (lang.equals("th")) {
            return "เดบิท";
        }
        return "Debit";
    }

    public static String CREDIT(String lang) {
        if (lang.equals("th")) {
            return "เครดิต";
        }
        return "Credit";
    }

    public static String LEDGER(String lang) {
        if (lang.equals("th")) {
            return "รายงานแยกประเภท";
        }
        return "Ledger";
    }

    public static String DATE(String lang) {
        if (lang.equals("th")) {
            return "วันที่";
        }
        return "Date";
    }

    public static String JOURNAL_TYPE(String lang) {
        if (lang.equals("th")) {
            return "สมุดรายวัน";
        }
        return "Journal type";
    }

    public static String JOURNAL_NO(String lang) {
        if (lang.equals("th")) {
            return "ใบสำคัญ";
        }
        return "Journal no.";
    }

    public static String REMAINING(String lang) {
        if (lang.equals("th")) {
            return "ยอดคงเหลือ";
        }
        return "Remaining";
    }

    public static String JAN(String lang) {
        if (lang.equals("th")) {
            return "ม.ค.";
        }
        return "Jan";
    }

    public static String FEB(String lang) {
        if (lang.equals("th")) {
            return "ก.พ.";
        }
        return "Feb";
    }

    public static String MAR(String lang) {
        if (lang.equals("th")) {
            return "มี.ค.";
        }
        return "Mar";
    }

    public static String APR(String lang) {
        if (lang.equals("th")) {
            return "เม.ษ.";
        }
        return "Apr";
    }

    public static String MAY(String lang) {
        if (lang.equals("th")) {
            return "พ.ค.";
        }
        return "May";
    }

    public static String JUN(String lang) {
        if (lang.equals("th")) {
            return "มิ.ย.";
        }
        return "Jun";
    }

    public static String JUL(String lang) {
        if (lang.equals("th")) {
            return "ก.ค.";
        }
        return "Jul";
    }

    public static String AUG(String lang) {
        if (lang.equals("th")) {
            return "ส.ค.";
        }
        return "Aug";
    }

    public static String SEP(String lang) {
        if (lang.equals("th")) {
            return "ก.ย.";
        }
        return "Sep";
    }

    public static String OCT(String lang) {
        if (lang.equals("th")) {
            return "ต.ค.";
        }
        return "Oct";
    }

    public static String NOV(String lang) {
        if (lang.equals("th")) {
            return "พ.ย.";
        }
        return "Nov";
    }

    public static String DEC(String lang) {
        if (lang.equals("th")) {
            return "ธ.ค.";
        }
        return "Dec";
    }
}
