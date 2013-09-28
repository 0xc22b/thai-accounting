package gwt.client.view.desktop;

import gwt.client.Print;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.AccAmtDef;
import gwt.client.def.FisDef;
import gwt.client.def.JournalDef;
import gwt.client.ui.CustomFlexTable;
import gwt.client.view.ReportView;
import gwt.shared.Utils;
import gwt.shared.model.SAccChart.AccType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.Widget;

public class ReportViewImpl<T, J, A> extends Composite implements ReportView<T, J, A> {

    public interface Resources extends ClientBundle {
        @Source("ReportViewImpl.css")
        Style style();

        public interface Style extends CssResource {

        }
    }

    private Resources resources = GWT.create(Resources.class);

    private static final String DOC_TAG = "<!doctype html>";
    private static final String STYLE_TAG = "<link rel=stylesheet type=text/css media=all href=/css/printstyle-alpha.css>";

    private static final String STYLE_NAME_RIGHT = "right";
    private static final String STYLE_NAME_CENTER = "center";
    private static final String STYLE_NAME_ULINE = "uline";
    private static final String STYLE_NAME_DULINE = "duline";
    private static final String STYLE_NAME_AULINE = "auline";
    private static final String STYLE_NAME_ADULINE = "aduline";
    private static final String STYLE_NAME_PAGE_ALWAYS_BREAK_BEFORE = "page-always-break-before";

    public enum PrintStyle {
        BLANK,
        CENTER,
        ULINE,
        DULINE,
        AULINE,
        ADULINE
    }

    private static final TConstants constants = TCF.get();

    private FisDef<T> fisDef;
    private JournalDef<J> journalDef;
    private AccAmtDef<A> accAmtDef;

    private CustomFlexTable flexTable;
    private FlexCellFormatter flexCellFormatter;
    private RowFormatter flexRowFormatter;

    public ReportViewImpl(FisDef<T> fisDef, JournalDef<J> journalDef, AccAmtDef<A> accAmtDef) {

        this.fisDef = fisDef;
        this.journalDef = journalDef;
        this.accAmtDef = accAmtDef;

        // Inject the contents of the CSS file
        resources.style().ensureInjected();

        flexTable = new CustomFlexTable();
        initWidget(flexTable);

        flexCellFormatter = flexTable.getFlexCellFormatter();
        flexRowFormatter = flexTable.getRowFormatter();
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void init(Presenter presenter) {
        //this.presenter = presenter;

        flexTable.clear();
    }

    @Override
    public void onPrintBtnClicked() {
        Print.it(DOC_TAG, STYLE_TAG, flexTable);
    }

    @Override
    public void setChartData(T t, String comName) {
        flexTable.setStyleName("flexTable chart");

        // Set header
        flexTable.setHeaderHTML(0, 0, 3, comName);
        flexTable.setHeaderHTML(0, 1, 3, constants.date() + ": " + genTodayDate());
        flexTable.setHeaderHTML(1, 0, 6, constants.accChart());
        flexTable.setHeaderHTML(2, 0, 6, "&nbsp;");
        flexTable.setHeaderHTML(3, 0, 1, constants.accNo());
        flexTable.setHeaderHTML(3, 1, 1, constants.accName());
        flexTable.setHeaderHTML(3, 2, 1, constants.code());
        flexTable.setHeaderHTML(3, 3, 1, constants.level());
        flexTable.setHeaderHTML(3, 4, 1, constants.accType());
        flexTable.setHeaderHTML(3, 5, 1, constants.parentAccNo());

        // Set content
        for (int i = 0; i < fisDef.getACListSize(t); i++) {
            String keyString = fisDef.getACKeyString(t, i);

            flexTable.setHTML(i, 0, fisDef.getACNo(t, keyString));
            flexTable.setHTML(i, 1, genName(fisDef.getACName(t, keyString), fisDef.getACLevel(t, keyString)));
            flexTable.setHTML(i, 2, fisDef.getACAGName(t, keyString));
            flexTable.setHTML(i, 3, fisDef.getACLevel(t, keyString) + "");

            if (fisDef.getACType(t, keyString).equals(AccType.CONTROL)) {
                flexTable.setHTML(i, 4, constants.control());
            } else if (fisDef.getACType(t, keyString).equals(AccType.ENTRY)) {
                flexTable.setHTML(i, 4, constants.entry());
            } else {
                throw new AssertionError(fisDef.getACType(t, keyString));
            }

            flexTable.setHTML(i, 5, fisDef.getACParentACNo(t, keyString));

        }
    }

    @Override
    public void setJourData(T t, ArrayList<ArrayList<J>> mJList, ArrayList<int[]> months,
            String comName, String journalTypeName) {

        flexTable.setStyleName("flexTable journal");

        // Set header
        flexTable.setHeaderHTML(0, 0, 3, comName);
        flexTable.setHeaderHTML(0, 1, 2, genTodayDate());
        flexTable.setHeaderHTML(1, 0, 5, journalTypeName);

        int[] beginDate = months.get(0);
        int[] endDate = months.get(months.size() - 1);
        flexTable.setHeaderHTML(2, 0, 5, constants.begin() + " "
                + genFormalDate(beginDate[0], beginDate[1], beginDate[2]) + " " + constants.end()
                + " " + genFormalDate(endDate[0], endDate[1], endDate[2]));
        flexTable.setHeaderHTML(3, 0, 1, constants.accNo());
        flexTable.setHeaderHTML(3, 1, 1, constants.accName());
        flexTable.setHeaderHTML(3, 2, 1, constants.desc());
        flexTable.setHeaderHTML(3, 3, 1, constants.debit());
        flexTable.setHeaderHTML(3, 4, 1, constants.credit());

        // Set content
        int row = 0;
        double debitTotal = 0;
        double creditTotal = 0;
        for (int i = 0; i < months.size(); i++) {

            int[] month = months.get(i);
            // As async, need to find the right index of sJournalList of the month
            int mJListIndex = journalDef.getIndex(mJList, month[1], month[2]);
            if (mJListIndex == -1) {
                // Empty, no journal in this month;
                continue;
            }
            ArrayList<J> jList = journalDef.getList(mJList, mJListIndex);

            for (J j : jList) {
                // Check beginDay
                if (i == 0 && journalDef.getDay(j) < month[0]) {
                    continue;
                }
                // Check endDay
                if (i == months.size() - 1 && journalDef.getDay(j) > month[0]) {
                    continue;
                }

                flexTable.setHTML(row, 0, journalDef.getDay(j) + "/" + journalDef.getMonth(j)
                        + "/" + journalDef.getYear(j) + "&nbsp;&nbsp;&nbsp;&nbsp;"
                        + journalDef.getNo(j));
                flexCellFormatter.setColSpan(row, 0, 5);
                row += 1;

                double debit = 0;
                double credit = 0;
                for (int k = 0; k < journalDef.getItemListSize(j); k++) {

                    String accChartKeyString = journalDef.getItemACKeyString(j, k);
                    flexTable.setHTML(row, 0, fisDef.getACNo(t, accChartKeyString));
                    flexTable.setHTML(row, 1, fisDef.getACName(t, accChartKeyString));
                    flexTable.setHTML(row, 2, journalDef.getDesc(j));

                    double amt = journalDef.getItemAmt(j, k);
                    if (amt > 0) {
                        flexTable.setHTML(row, 3, NumberFormat.getFormat("#,##0.00").format(amt));
                        flexTable.setHTML(row, 4, "&nbsp;");

                        debit += amt;
                    } else {
                        flexTable.setHTML(row, 3, "&nbsp;");
                        flexTable.setHTML(row, 4,
                                NumberFormat.getFormat("#,##0.00").format(Math.abs(amt)));

                        credit += amt;
                    }
                    row += 1;
                }

                flexTable.setHTML(row, 0, "&nbsp;");
                flexTable.setHTML(row, 1, "&nbsp;");
                flexTable.setHTML(row, 2, constants.total());
                flexTable.setHTML(row, 3, NumberFormat.getFormat("#,##0.00").format(debit));
                flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(Math.abs(credit)));

                flexCellFormatter.addStyleName(row, 3, STYLE_NAME_AULINE);
                flexCellFormatter.addStyleName(row, 4, STYLE_NAME_AULINE);

                row += 1;

                debitTotal += debit;
                creditTotal += credit;
            }
        }

        flexTable.setHTML(row, 0, "&nbsp;");
        flexTable.setHTML(row, 1, "&nbsp;");
        flexTable.setHTML(row, 2, "&nbsp;");
        flexTable.setHTML(row, 3, "&nbsp;");
        flexTable.setHTML(row, 4, "&nbsp;");
        row += 1;

        flexTable.setHTML(row, 0, "&nbsp;");
        flexTable.setHTML(row, 1, "&nbsp;");
        flexTable.setHTML(row, 2, constants.wholeTotal());
        flexTable.setHTML(row, 3, NumberFormat.getFormat("#,##0.00").format(debitTotal));
        flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(Math.abs(creditTotal)));

        flexCellFormatter.addStyleName(row, 3, STYLE_NAME_DULINE);
        flexCellFormatter.addStyleName(row, 4, STYLE_NAME_DULINE);
    }

    @Override
    public void setLedgerData(T t, HashMap<String, J> aJList, int[] dates, String comName,
            String beginACNo, String endACNo, boolean doShowAll) {

        flexTable.setStyleName("flexTable ledger");

        // Set header
        flexTable.setHeaderHTML(0, 0, 4, comName);
        flexTable.setHeaderHTML(0, 1, 3, genTodayDate());
        flexTable.setHeaderHTML(1, 0, 7, constants.ledger());

        flexTable.setHeaderHTML(2, 0, 7, constants.accNo() + " " + beginACNo + " - " + endACNo
                + " (" + genFormalDate(dates[0], dates[1], dates[2])
                + " - " + genFormalDate(dates[3], dates[4], dates[5]) + ")");

        flexTable.setHeaderHTML(3, 0, 1, constants.date());
        flexTable.setHeaderHTML(3, 1, 1, constants.journalType());
        flexTable.setHeaderHTML(3, 2, 1, constants.journalNo());
        flexTable.setHeaderHTML(3, 3, 1, constants.desc());
        flexTable.setHeaderHTML(3, 4, 1, constants.debit());
        flexTable.setHeaderHTML(3, 5, 1, constants.credit());
        flexTable.setHeaderHTML(3, 6, 1, constants.remaining());

        // Set content
        int row = 0;
        double debitTotal = 0;
        double creditTotal = 0;

        // Some accounts have a beginning but journals so need to loop all accounts.
        for (int i = 0; i < fisDef.getACListSize(t); i++) {

            if (!fisDef.getACIsEntry(t, i)) {
                continue;
            }

            String acNo = fisDef.getACNo(t, i);

            if (acNo.compareTo(beginACNo) < 0) {
                continue;
            }

            if (acNo.compareTo(endACNo) > 0) {
                continue;
            }

            String acKeyString = fisDef.getACKeyString(t, i);

            // Check if there is data
            boolean doesHaveData = aJList.containsKey(acKeyString);

            double beginning = fisDef.getACBeginning(t, acKeyString);

            if (!doShowAll && Utils.isZero(beginning, 2) && !doesHaveData) {
                continue;
            }

            String formattedBeginning = NumberFormat.getFormat(
                    "#,##0.00;(#,##0.00)").format(Math.abs(beginning));

            flexTable.setHTML(row, 0, acNo + "&nbsp;&nbsp;&nbsp;&nbsp;"
                    + fisDef.getACName(t, acKeyString));
            flexCellFormatter.setColSpan(row, 0, 6);
            flexTable.setHTML(row, 1, formattedBeginning);
            flexCellFormatter.addStyleName(row, 1, STYLE_NAME_RIGHT);

            row += 1;

            J j = aJList.get(acKeyString);

            double debit = 0;
            double credit = 0;

            if (j != null) {    // Can be null if doShowAll = true
                for (int k = 0; k < journalDef.getItemListSize(j); k++) {

                    flexTable.setHTML(row, 0, journalDef.getItemDay(j, k) + "/"
                            + journalDef.getItemMonth(j, k) + "/"
                            + journalDef.getItemYear(j, k));
                    flexTable.setHTML(row, 1, journalDef.getItemJTShortName(j, k));
                    flexTable.setHTML(row, 2, journalDef.getItemJNo(j, k));
                    flexTable.setHTML(row, 3, journalDef.getItemJDesc(j, k));
    
                    double amt = journalDef.getItemAmt(j, k);
                    String formattedAmt = NumberFormat.getFormat("#,##0.00").format(
                            Math.abs(amt));
                    if (amt > 0) {
                        flexTable.setHTML(row, 4, formattedAmt);
                        flexTable.setHTML(row, 5, "&nbsp;");
    
                        beginning += amt;
                        debit += amt;
                    } else {
                        flexTable.setHTML(row, 4, "&nbsp;");
                        flexTable.setHTML(row, 5, formattedAmt);
    
                        beginning += amt;
                        credit += amt;
                    }
    
                    formattedBeginning = NumberFormat.getFormat(
                            "#,##0.00;(#,##0.00)").format(Math.abs(beginning));
                    flexTable.setHTML(row, 6, formattedBeginning);
                    row += 1;
                }
            }

            flexTable.setHTML(row, 0, "&nbsp;");
            flexTable.setHTML(row, 1, "&nbsp;");
            flexTable.setHTML(row, 2, "&nbsp;");
            flexTable.setHTML(row, 3, constants.total());
            flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(debit));
            flexTable.setHTML(row, 5, NumberFormat.getFormat("#,##0.00").format(Math.abs(credit)));
            flexTable.setHTML(row, 6, "&nbsp;");

            flexCellFormatter.addStyleName(row, 4, STYLE_NAME_AULINE);
            flexCellFormatter.addStyleName(row, 5, STYLE_NAME_AULINE);
            row += 1;

            flexTable.setHTML(row, 0, "&nbsp;");
            flexTable.setHTML(row, 1, "&nbsp;");
            flexTable.setHTML(row, 2, "&nbsp;");
            flexTable.setHTML(row, 3, "&nbsp;");
            flexTable.setHTML(row, 4, "&nbsp;");
            flexTable.setHTML(row, 5, "&nbsp;");
            flexTable.setHTML(row, 6, "&nbsp;");
            row += 1;

            debitTotal += debit;
            creditTotal += credit;
        }

        flexTable.setHTML(row, 0, "&nbsp;");
        flexTable.setHTML(row, 1, "&nbsp;");
        flexTable.setHTML(row, 2, "&nbsp;");
        flexTable.setHTML(row, 3, constants.wholeTotal());
        flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(debitTotal));
        flexTable.setHTML(row, 5, NumberFormat.getFormat("#,##0.00").format(Math.abs(creditTotal)));
        flexTable.setHTML(row, 6, "&nbsp;");

        flexCellFormatter.addStyleName(row, 4, STYLE_NAME_DULINE);
        flexCellFormatter.addStyleName(row, 5, STYLE_NAME_DULINE);
    }

    @Override
    public void setTrialData(T t, Map<String, A> aMap, String comName, boolean doShowAll) {
        flexTable.setStyleName("flexTable trial");

        // Set header
        flexTable.setHeaderHTML(0, 0, 2, comName);
        flexTable.setHeaderHTML(0, 1, 2, genTodayDate());
        flexTable.setHeaderHTML(1, 0, 4, constants.trial());
        flexTable.setHeaderHTML(2, 0, 4, constants.fisList() + ": " + fisDef.getFBeginYear(t));
        flexTable.setHeaderHTML(3, 0, 1, constants.accNo());
        flexTable.setHeaderHTML(3, 1, 1, constants.accName());
        flexTable.setHeaderHTML(3, 2, 1, constants.debit());
        flexTable.setHeaderHTML(3, 3, 1, constants.credit());

        // Set content
        int row = 0;
        double amt;
        double debitTotal = 0.0;
        double creditTotal = 0.0;

        String formatedAmt;

        for (int i = 0; i < fisDef.getACListSize(t); i++) {
            if (fisDef.getACIsEntry(t, i)) {
                String accChartKeyString = fisDef.getACKeyString(t, i);

                amt = fisDef.getACBeginning(t, accChartKeyString);
                amt += accAmtDef.getAmt(aMap, accChartKeyString);

                if (!doShowAll && Utils.isZero(amt, 2)) {
                    continue;
                }

                formatedAmt = NumberFormat.getFormat("#,##0.00").format(
                        Math.abs(amt));

                flexTable.setHTML(row, 0, fisDef.getACNo(t, i));
                flexTable.setHTML(row, 1, fisDef.getACName(t, i));
                flexTable.setHTML(row, 2, amt > 0 ? formatedAmt : "");
                flexTable.setHTML(row, 3, amt > 0 ? "" : formatedAmt);
                row += 1;

                if (amt > 0) {
                    debitTotal += amt;
                } else {
                    creditTotal += amt;
                }
            }
        }

        flexTable.setHTML(row, 0, "&nbsp;");
        flexTable.setHTML(row, 1, constants.wholeTotal());
        flexTable.setHTML(row, 2, NumberFormat.getFormat("#,##0.00").format(debitTotal));
        flexTable.setHTML(row, 3, NumberFormat.getFormat("#,##0.00").format(Math.abs(creditTotal)));

        flexCellFormatter.addStyleName(row, 2, STYLE_NAME_ADULINE);
        flexCellFormatter.addStyleName(row, 3, STYLE_NAME_ADULINE);
    }

    @Override
    public void setBalanceData(final T t, Map<String, A> aMap, final String comName,
            String assetACKeyString, String debtACKeyString,
            String shareholderACKeyString, String accruedProfitACKeyString,
            String incomeACKeyString, String expenseACKeyString,
            boolean doShowAll, boolean doesSplit) {

        flexTable.setStyleName("flexTable finance");

        // Set header
        flexTable.setHeaderHTML(0, 0, 4, comName);
        flexTable.setHeaderHTML(1, 0, 4, constants.balanceSheet());
        int month = fisDef.getFEndMonth(t);
        int year = fisDef.getFEndYear(t);
        flexTable.setHeaderHTML(2, 0, 4, constants.end() + " "
                + genFormalDate(Utils.getLastDay(month, year), month, year));

        List<CumulativeAC<T>> assetCACs = getCumulativeACList(t, aMap, assetACKeyString, null, 0.0);
        List<CumulativeAC<T>> debtCACs = getCumulativeACList(t, aMap, debtACKeyString, null, 0.0);

        List<CumulativeAC<T>> incomeCACs = getCumulativeACList(t, aMap, incomeACKeyString,
                null, 0.0);
        List<CumulativeAC<T>> expenseCACs = getCumulativeACList(t, aMap, expenseACKeyString,
                null, 0.0);

        // Total profit (loss)
        double profit = 0.0;
        if (!incomeCACs.isEmpty()) {
            profit = incomeCACs.get(0).amt;
        }
        if (!expenseCACs.isEmpty()) {
            profit += expenseCACs.get(0).amt;
        }

        List<CumulativeAC<T>> shareholderCACs = getCumulativeACList(
                t, aMap, shareholderACKeyString, accruedProfitACKeyString, profit);

        int row = 0;
        row = printCACs(row, assetCACs, doShowAll, true, true,
                PrintStyle.DULINE, true, PrintStyle.AULINE);

        row = printLine(row, constants.debtAndShareholder(), 0, false,
                PrintStyle.CENTER, false);

        if (doesSplit) {
            flexRowFormatter.addStyleName(row - 1, STYLE_NAME_PAGE_ALWAYS_BREAK_BEFORE);
        }

        // Blank line
        row = printLine(row, "&nbsp;", 0.0, false, PrintStyle.BLANK,
                false);

        row = printCACs(row, debtCACs, doShowAll, false, false,
                PrintStyle.ULINE, true, PrintStyle.AULINE);
        row = printSecondLevelCACs(row, shareholderCACs.get(0),
                shareholderCACs, doShowAll, false, false, true, PrintStyle.AULINE);

        CumulativeAC<T> debtCAC = debtCACs.get(0);
        CumulativeAC<T> shareholderCAC = shareholderCACs.get(0);

        // Print total debts and shareholders
        row = printLine(row, constants.total() + constants.debtAndShareholder(),
                debtCAC.amt + shareholderCAC.amt, true, PrintStyle.DULINE,
                false);

        // Blank line
        row = printLine(row, "&nbsp;", 0.0, false, PrintStyle.BLANK,
                false);
    }

    @Override
    public void setProfitData(T t, Map<String, A> aMap, String comName, String incomeACKeyString,
            String expenseACKeyString, boolean doShowAll, boolean doesSplit) {

        flexTable.setStyleName("flexTable finance");

        // Set header
        flexTable.setHeaderHTML(0, 0, 4, comName);
        flexTable.setHeaderHTML(1, 0, 4, constants.profitReport());
        int month = fisDef.getFEndMonth(t);
        int year = fisDef.getFEndYear(t);
        flexTable.setHeaderHTML(2, 0, 4, constants.end() + " " + genFormalDate(
                Utils.getLastDay(month, year), month, year));

        List<CumulativeAC<T>> incomeCACs = getCumulativeACList(t, aMap, incomeACKeyString,
                null, 0.0);
        List<CumulativeAC<T>> expenseCACs = getCumulativeACList(t, aMap, expenseACKeyString,
                null, 0.0);

        // Total profit (loss)
        double profit = 0.0;
        if (!incomeCACs.isEmpty()) {
            profit = incomeCACs.get(0).amt;
        }
        if (!expenseCACs.isEmpty()) {
            profit += expenseCACs.get(0).amt;
        }

        int row = 0;
        row = printCACs(row, incomeCACs, doShowAll, true, false,
                PrintStyle.AULINE, false, PrintStyle.BLANK);
        int brokenRow = row;
        row = printCACs(row, expenseCACs, doShowAll, true, true,
                PrintStyle.AULINE, false, PrintStyle.BLANK);

        if (doesSplit) {
            flexRowFormatter.addStyleName(brokenRow,
                    STYLE_NAME_PAGE_ALWAYS_BREAK_BEFORE);
        }

        // Print total profit
        row = printLine(row, constants.profit(), profit, true,
                PrintStyle.DULINE, false);

        // Blank line
        row = printLine(row, "&nbsp;", 0.0, false, PrintStyle.BLANK,
                true);
    }

    @Override
    public void setCostData(T t, Map<String, A> aMap, String comName, String costACKeyString,
            boolean doShowAll) {

        flexTable.setStyleName("flexTable finance");

        // Set header
        flexTable.setHeaderHTML(0, 0, 4, comName);
        flexTable.setHeaderHTML(1, 0, 4, constants.costReport());
        int month = fisDef.getFEndMonth(t);
        int year = fisDef.getFEndYear(t);
        flexTable.setHeaderHTML(2, 0, 4, constants.end() + " " + genFormalDate(
                Utils.getLastDay(month, year), month, year));

        List<CumulativeAC<T>> costCACs = getCumulativeACList(t, aMap, costACKeyString, null, 0.0);

        int row = 0;
        row = printSecondLevelCACs(row, costCACs.get(0), costCACs, doShowAll,
                true, true, true, PrintStyle.ADULINE);
    }

    private static class CumulativeAC<T> {
        String keyString;
        //String no;
        String name;
        AccType type;
        int level;
        double beginning;
        double amt;

        public CumulativeAC(FisDef<T> fisDef, T t, int i) {
            this.keyString = fisDef.getACKeyString(t, i);
            //this.no = fisDef.getACNo(t, i);
            this.name = fisDef.getACName(t, i);
            this.type = fisDef.getACType(t, i);
            this.level = fisDef.getACLevel(t, i);
            this.beginning = fisDef.getACBeginning(t, i);
            this.amt = 0.0;
        }
    }

    private List<CumulativeAC<T>> getCumulativeACList(T t, Map<String, A> aMap,
            String rootACKeyString, String adjustedACKeyString,
            double adjustedValue) {
        // Important that the account charts need to be sorted!!
        //     as here we use level instead of parentACKeyString.

        // Kind of a hack to have adjustedACKeyString and adjustedValue

        // Try to find the root account chart (must be AccType.CONTROL).
        int rootIndex = -1;
        for (int i = 0; i < fisDef.getACListSize(t); i++) {
            if (fisDef.getACKeyString(t, i).equals(rootACKeyString)) {
                rootIndex = i;
                break;
            }
        }
        if (rootIndex == -1) {
            return null;
        }

        List<CumulativeAC<T>> cACList = new ArrayList<CumulativeAC<T>>();

        CumulativeAC<T> rootCAC = new CumulativeAC<T>(fisDef, t, rootIndex);
        cACList.add(rootCAC);

        for (int i = rootIndex + 1; i < fisDef.getACListSize(t); i++) {
            if (fisDef.getACLevel(t, i) <= rootCAC.level) {
                break;
            }

            CumulativeAC<T> cAC = new CumulativeAC<T>(fisDef, t, i);

            // Populate values
            if (cAC.type == AccType.ENTRY) {
                cAC.amt = cAC.beginning;
                cAC.amt += accAmtDef.getAmt(aMap, cAC.keyString);
            }

            // Adjust values
            if (adjustedACKeyString != null
                    && adjustedACKeyString.equals(cAC.keyString)) {
                cAC.amt += adjustedValue;
            }

            cACList.add(cAC);
        }

        // Cumulate amounts to all parents
        cumulateAmount(rootCAC, cACList);

        return cACList;
    }

    private double cumulateAmount(CumulativeAC<T> cAC,
            List<CumulativeAC<T>> cACList) {
        if (cAC.type == AccType.ENTRY) {
            return cAC.amt;
        }

        cAC.amt = 0.0;
        for (CumulativeAC<T> child : getDirectCACChildren(cAC, cACList)) {
            cAC.amt += cumulateAmount(child, cACList);
        }
        return cAC.amt;
    }

    private List<CumulativeAC<T>> getDirectCACChildren(CumulativeAC<T> parentCAC,
            List<CumulativeAC<T>> cACList) {

        int parentIndex = cACList.indexOf(parentCAC);
        if (parentIndex < 0) return null;

        List<CumulativeAC<T>> children = new ArrayList<CumulativeAC<T>>();
        for (int i = parentIndex + 1; i < cACList.size(); i++) {
            CumulativeAC<T> cAC = cACList.get(i);
            if (cAC.level <= parentCAC.level) {
                break;
            }

            if (cAC.level == parentCAC.level + 1) {
                children.add(cAC);
            }
        }
        return children;
    }

    private int printCACs(int row, List<CumulativeAC<T>> cACs,
            boolean doShowAll, boolean doesPrintRoot, boolean doesShowPlus,
            PrintStyle totalPrintStyle, boolean doesPrintSecondLevelTotal,
            PrintStyle secondLevelTotalPrintStyle) {
        // Shareholder account chart groups is not working with this format of report
        //     as it needs to have some values from other account chart groups.
        //     Use printSecondLevelCACS instead.

        // Print total for level 1 and level 2

        CumulativeAC<T> rootCAC = cACs.get(0);
        if (doesPrintRoot) {
            row = printLine(row, rootCAC.name, 0, false, PrintStyle.CENTER,
                    doesShowPlus);

            // Blank line
            row = printLine(row, "&nbsp;", 0.0, false, PrintStyle.BLANK,
                    doesShowPlus);
        }

        List<CumulativeAC<T>> directCACChildren = getDirectCACChildren(
                rootCAC, cACs);
        for (CumulativeAC<T> directCACChild : directCACChildren) {
            row = printSecondLevelCACs(row, directCACChild, cACs, doShowAll,
                    doesShowPlus, true, doesPrintSecondLevelTotal, secondLevelTotalPrintStyle);
        }

        // Total for level 1
        row = printLine(row, constants.total() + rootCAC.name, rootCAC.amt, true,
                totalPrintStyle, doesShowPlus);

        // Blank line
        row = printLine(row, "&nbsp;", 0.0, false, PrintStyle.BLANK,
                doesShowPlus);

        return row;
    }

    private int printSecondLevelCACs(int row, CumulativeAC<T> secondLevelCAC,
            List<CumulativeAC<T>> cACs, boolean doShowAll, boolean doesShowPlus,
            boolean doesAdjustLevel, boolean doesPrintTotal, PrintStyle totalPrintStyle) {
        int startIndex = cACs.indexOf(secondLevelCAC);
        if (startIndex < 0) {
            return row;
        }

        int startRow = row;

        CumulativeAC<T> cAC;
        int level;
        for (int i = startIndex; i < cACs.size(); i++) {
            cAC = cACs.get(i);
            if (i != startIndex && cAC.level <= secondLevelCAC.level) {
                break;
            }

            if (Utils.isZero(cAC.amt, 2)) {
                // TODO: Find next i
                continue;
            }

            level = doesAdjustLevel ? cAC.level - 1 : cAC.level;

            if (doShowAll) {
                if (cAC.type == AccType.CONTROL) {
                    row = printLine(row, genName(cAC.name, level), 0,
                            false, PrintStyle.BLANK, doesShowPlus);
                } else if (cAC.type == AccType.ENTRY) {
                    row = printLine(row, genName(cAC.name, level),
                            cAC.amt, true, PrintStyle.BLANK, doesShowPlus);
                } else {
                    throw new AssertionError();
                }
            } else {
                if (cAC.type == AccType.CONTROL) {
                    if (isDeepestControlAC(cACs, i)) {
                        // The control deepest level, print with value
                        row = printLine(row, genName(cAC.name, level),
                                cAC.amt, true, PrintStyle.BLANK, doesShowPlus);
                    } else {
                        // Print only name
                        row = printLine(row, genName(cAC.name, level),
                                0, false, PrintStyle.BLANK, doesShowPlus);
                    }
                } else if (cAC.type == AccType.ENTRY) {
                    // Print if there are entries with AccType.CONTROL
                    //     in the same level
                    if (isThereSameLevelControlAC(cACs, i)) {
                        row = printLine(row, genName(cAC.name, level),
                                cAC.amt, true, PrintStyle.BLANK, doesShowPlus);
                    }
                } else {
                    throw new AssertionError();
                }
            }
        }

        if (row > startRow && doesPrintTotal) {
            // Always at the first level
            row = printLine(row, constants.total() + secondLevelCAC.name,
                    secondLevelCAC.amt, true, totalPrintStyle, doesShowPlus);

            // Blank line
            row = printLine(row, "&nbsp;", 0.0, false, PrintStyle.BLANK,
                    doesShowPlus);
        }

        return row;
    }

    private boolean isDeepestControlAC(List<CumulativeAC<T>> cACs, int i) {
        CumulativeAC<T> cAC = cACs.get(i);
        for (int j = i + 1; j < cACs.size(); j++) {
            CumulativeAC<T> tCAC = cACs.get(j);
            if (tCAC.level <= cAC.level) {
                break;
            }
            if (tCAC.type == AccType.CONTROL) {
                return false;
            }
        }
        return true;
    }

    private boolean isThereSameLevelControlAC(List<CumulativeAC<T>> cACs, int i) {
        CumulativeAC<T> cAC = cACs.get(i);
        // Look down
        for (int j = i + 1; j < cACs.size(); j++) {
            CumulativeAC<T> tCAC = cACs.get(j);
            if (tCAC.level < cAC.level) {
                break;
            }
            if (tCAC.level == cAC.level
                    && tCAC.type == AccType.CONTROL) {
                return true;
            }
        }

        // Look up
        for (int j = i - 1; j >= 0; j--) {
            CumulativeAC<T> tCAC = cACs.get(j);
            if (tCAC.level < cAC.level) {
                break;
            }
            if (tCAC.level == cAC.level
                    && tCAC.type == AccType.CONTROL) {
                return true;
            }
        }

        return false;
    }

    private int printLine(int row, String text, double value, boolean isValue,
            PrintStyle printStyle, boolean doesShowPlus){
        if (printStyle == PrintStyle.CENTER) {
            flexTable.setHTML(row, 0, text);
            flexTable.setHTML(row, 1, "&nbsp;");
            flexCellFormatter.addStyleName(row, 0, STYLE_NAME_CENTER);
        } else {
            flexTable.setHTML(row, 0, text);

            if(isValue){
                value = doesShowPlus ? value : value * -1;
                String formattedValue = NumberFormat.getFormat(
                        "#,##0.00;(#,##0.00)").format(value);

                flexTable.setHTML(row, 1, formattedValue);

                if (printStyle.equals(PrintStyle.ULINE)) {
                    flexCellFormatter.addStyleName(row, 1, STYLE_NAME_ULINE);
                } else if (printStyle.equals(PrintStyle.DULINE)) {
                    flexCellFormatter.addStyleName(row, 1, STYLE_NAME_DULINE);
                } else if (printStyle.equals(PrintStyle.AULINE)) {
                    flexCellFormatter.addStyleName(row, 1, STYLE_NAME_AULINE);
                } else if (printStyle.equals(PrintStyle.ADULINE)) {
                    flexCellFormatter.addStyleName(row, 1, STYLE_NAME_ADULINE);
                }
            }else{
                flexTable.setHTML(row, 1, "&nbsp;");
            }
        }

        //flexTable.setHTML(row, 2, "");
        //flexTable.setHTML(row, 3, "");

        return row + 1;
    }

    private String genName(String name, int level) {
        String s = "";
        for (int i = 1; i < level; i++) {
            s += "&nbsp;&nbsp;&nbsp;&nbsp;";
        }
        return s + name;
    }

    private String genTodayDate(){
        String dateS = DateTimeFormat.getFormat("dd/MM/yyyy").format(new Date());
        //TODO: Convert to Thai year using company/user settings.
        String yearS = dateS.split("/")[2];
        int year = Integer.parseInt(yearS) + 543;
        return dateS.substring(0, 6) + year;
    }

    private String genFormalDate(int day, int month, int year){
        String s = "";
        s += day + " ";

        switch(month){
        case 1:
            s += constants.jan() + " ";
            break;
        case 2:
            s += constants.feb() + " ";
            break;
        case 3:
            s += constants.mar() + " ";
            break;
        case 4:
            s += constants.apr() + " ";
            break;
        case 5:
            s += constants.may() + " ";
            break;
        case 6:
            s += constants.jun() + " ";
            break;
        case 7:
            s += constants.jul() + " ";
            break;
        case 8:
            s += constants.aug() + " ";
            break;
        case 9:
            s += constants.sep() + " ";
            break;
        case 10:
            s += constants.oct() + " ";
            break;
        case 11:
            s += constants.nov() + " ";
            break;
        case 12:
            s += constants.dec() + " ";
            break;
        default:
            throw new AssertionError(month);
        }

        s += year;

        return s;
    }
}
