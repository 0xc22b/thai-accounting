package gwt.client.view.desktop;

import java.util.Date;

import gwt.client.Print;
import gwt.client.TCF;
import gwt.client.TConstants;
import gwt.client.def.FisDef;
import gwt.client.place.AllPlace;
import gwt.client.ui.CustomFlexTable;
import gwt.client.view.ReportView;
import gwt.shared.Utils;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SFinItem.CalCon;
import gwt.shared.model.SFinItem.Comm;
import gwt.shared.model.SFinItem.Operand;
import gwt.shared.model.SFinItem.PrintCon;
import gwt.shared.model.SFinItem.PrintStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.Widget;

public class ReportViewImpl<T> extends Composite implements ReportView<T> {

    public interface Resources extends ClientBundle {
        @Source("ReportViewImpl.css")
        Style style();

        public interface Style extends CssResource {

        }
    }

    private Resources resources = GWT.create(Resources.class);

    private static final String docTag = "<!doctype html>";
    private static final String styleTag = "<link rel=stylesheet type=text/css media=all href=/css/printstyle-alpha.css>";

    private static final String center = "center";
    private static final String uline = "uline";
    private static final String twoulines = "twoulines";
    
    private static final TConstants constants = TCF.get();

    private FisDef<T> fisDef;

    private CustomFlexTable flexTable;
    private FlexCellFormatter flexCellFormatter;

    public ReportViewImpl(FisDef<T> fisDef) {

        this.fisDef = fisDef;

        // Inject the contents of the CSS file
        resources.style().ensureInjected();

        flexTable = new CustomFlexTable();
        initWidget(flexTable);

        flexCellFormatter = flexTable.getFlexCellFormatter();
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
        Print.it(docTag, styleTag, flexTable);
        Print.printFrame();
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
            flexTable.setHTML(i, 1, genAccName(fisDef.getACName(t, keyString), fisDef.getACLevel(t, keyString)));
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
    public void setJourData(T t, String comName, String journalTypeKeyString, int beginDay, int beginMonth, int beginYear, int endDay, int endMonth,
            int endYear) {
        flexTable.setStyleName("flexTable journal");
        
        // Set header
        flexTable.setHeaderHTML(0, 0, 3, comName);
        flexTable.setHeaderHTML(0, 1, 2, genTodayDate());
        flexTable.setHeaderHTML(1, 0, 5, fisDef.getJTName(t, journalTypeKeyString));

        int[] dates = getBeginDateEndDate(t, beginDay, beginMonth, beginYear, endDay, endMonth, endYear);
        flexTable.setHeaderHTML(2, 0, 5, constants.begin() + " " + genFormalDate(dates[0], dates[1], dates[2]) + " " + constants.end() + 
                " " + genFormalDate(dates[3], dates[4], dates[5]));
        flexTable.setHeaderHTML(3, 0, 1, constants.accNo());
        flexTable.setHeaderHTML(3, 1, 1, constants.accName());
        flexTable.setHeaderHTML(3, 2, 1, constants.desc());
        flexTable.setHeaderHTML(3, 3, 1, constants.debit());
        flexTable.setHeaderHTML(3, 4, 1, constants.credit());

        // Set content
        int row = 0;
        double debitTotal = 0;
        double creditTotal = 0;
        for (int i = 0; i < fisDef.getJListSize(t); i++) {
            String keyString = fisDef.getJKeyString(t, i);
            if (fisDef.getJJTKeyString(t, keyString).equals(journalTypeKeyString)) {

                if ((beginDay > 0 && beginMonth > 0 && beginYear > 0) && (fisDef.getJCompareDate(t, keyString, beginDay, beginMonth, beginYear) < 0)) {
                    continue;
                }

                if ((endDay > 0 && endMonth > 0 && endYear > 0) && (fisDef.getJCompareDate(t, keyString, endDay, endMonth, endYear) > 0)) {
                    continue;
                }

                flexTable.setHTML(row, 0, fisDef.getJDay(t, keyString) + "/" + fisDef.getJMonth(t, keyString) + "/" + fisDef.getJYear(t, keyString)
                        + "&nbsp;&nbsp;&nbsp;&nbsp;" + fisDef.getJNo(t, keyString));
                flexCellFormatter.setColSpan(row, 0, 5);
                row += 1;

                double debit = 0;
                double credit = 0;
                for (int j = 0; j < fisDef.getJItemListSize(t, keyString); j++) {

                    flexTable.setHTML(row, 0, fisDef.getJItemACNo(t, keyString, j));
                    flexTable.setHTML(row, 1, fisDef.getJItemACName(t, keyString, j));
                    flexTable.setHTML(row, 2, fisDef.getJDesc(t, keyString));
                    if (fisDef.getJItemAmt(t, keyString, j) > 0) {
                        flexTable.setHTML(row, 3, NumberFormat.getFormat("#,##0.00").format(fisDef.getJItemAmt(t, keyString, j)));
                        flexTable.setHTML(row, 4, "&nbsp;");

                        debit += fisDef.getJItemAmt(t, keyString, j);
                    } else {
                        flexTable.setHTML(row, 3, "&nbsp;");
                        flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(Math.abs(fisDef.getJItemAmt(t, keyString, j))));

                        credit += fisDef.getJItemAmt(t, keyString, j);
                    }
                    row += 1;
                }

                flexTable.setHTML(row, 0, "&nbsp;");
                flexTable.setHTML(row, 1, "&nbsp;");
                flexTable.setHTML(row, 2, constants.total());
                flexTable.setHTML(row, 3, NumberFormat.getFormat("#,##0.00").format(debit));
                flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(Math.abs(credit)));
                row += 1;

                debitTotal += debit;
                creditTotal += credit;
            }
        }

        flexTable.setHTML(row, 0, "&nbsp;");
        flexTable.setHTML(row, 1, "&nbsp;");
        flexTable.setHTML(row, 2, constants.wholeTotal());
        flexTable.setHTML(row, 3, NumberFormat.getFormat("#,##0.00").format(debitTotal));
        flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(Math.abs(creditTotal)));

    }

    @Override
    public void setLedgerData(T t, String comName, String beginAccChartKeyString, String endAccChartKeyString, int beginDay, int beginMonth,
            int beginYear, int endDay, int endMonth, int endYear) {
        flexTable.setStyleName("flexTable ledger");
        
        // Set header
        flexTable.setHeaderHTML(0, 0, 4, comName);
        flexTable.setHeaderHTML(0, 1, 3, genTodayDate());
        flexTable.setHeaderHTML(1, 0, 7, constants.ledger());

        int[] dates = getBeginDateEndDate(t, beginDay, beginMonth, beginYear, endDay, endMonth, endYear);
        String[] accNos = getBeginAccNoEndAccNo(t, beginAccChartKeyString, endAccChartKeyString);
        flexTable.setHeaderHTML(2, 0, 7, constants.accNo() + " " + accNos[0] + " - " + accNos[1] + " (" + genFormalDate(dates[0], dates[1], dates[2])
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
        double beginning;
        double debit;
        double credit;
        double debitTotal = 0;
        double creditTotal = 0;
        String beginAccNo = beginAccChartKeyString.equals(AllPlace.FIRST) ? AllPlace.FIRST : fisDef.getACNo(t, beginAccChartKeyString);
        String endAccNo = endAccChartKeyString.equals(AllPlace.LAST) ? AllPlace.LAST : fisDef.getACNo(t, endAccChartKeyString);
        for (int i = 0; i < fisDef.getACListSize(t); i++) {
            if (fisDef.getACIsEntry(t, i)) {

                String accNo = fisDef.getACNo(t, i);
                if (!beginAccNo.equals(AllPlace.FIRST) && accNo.compareTo(beginAccNo) < 0) {
                    continue;
                }

                if (!endAccNo.equals(AllPlace.LAST) && accNo.compareTo(endAccNo) > 0) {
                    continue;
                }

                String accChartKeyString = fisDef.getACKeyString(t, i);
                beginning = fisDef.getACBeginning(t, accChartKeyString);

                flexTable.setHTML(row, 0, accNo + "&nbsp;&nbsp;&nbsp;&nbsp;" + fisDef.getACName(t, accChartKeyString));
                flexCellFormatter.setColSpan(row, 0, 6);
                flexTable.setHTML(row, 1, NumberFormat.getFormat("#,##0.00").format(beginning));

                row += 1;

                debit = 0;
                credit = 0;
                for (int j = 0; j < fisDef.getJListSize(t); j++) {
                    String journalKeyString = fisDef.getJKeyString(t, j);

                    if ((beginDay > 0 && beginMonth > 0 && beginYear > 0)
                            && (fisDef.getJCompareDate(t, journalKeyString, beginDay, beginMonth, beginYear) < 0)) {
                        continue;
                    }

                    if ((endDay > 0 && endMonth > 0 && endYear > 0) && (fisDef.getJCompareDate(t, journalKeyString, endDay, endMonth, endYear) > 0)) {
                        continue;
                    }

                    for (int k = 0; k < fisDef.getJItemListSize(t, journalKeyString); k++) {
                        if (fisDef.getJItemACKeyString(t, journalKeyString, k).equals(accChartKeyString)) {

                            flexTable.setHTML(row, 0, fisDef.getJDay(t, journalKeyString) + "/" + fisDef.getJMonth(t, journalKeyString) + "/"
                                            + fisDef.getJYear(t, journalKeyString));
                            flexTable.setHTML(row, 1, fisDef.getJTShortName(t, fisDef.getJJTKeyString(t, journalKeyString)));
                            flexTable.setHTML(row, 2, fisDef.getJNo(t, journalKeyString));
                            flexTable.setHTML(row, 3, fisDef.getJDesc(t, journalKeyString));

                            if (fisDef.getJItemAmt(t, journalKeyString, k) > 0) {
                                flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(fisDef.getJItemAmt(t, journalKeyString, k)));
                                flexTable.setHTML(row, 5, "&nbsp;");

                                beginning += fisDef.getJItemAmt(t, journalKeyString, k);
                                debit += fisDef.getJItemAmt(t, journalKeyString, k);
                            } else {
                                flexTable.setHTML(row, 4, "&nbsp;");
                                flexTable.setHTML(row, 5,
                                        NumberFormat.getFormat("#,##0.00").format(Math.abs(fisDef.getJItemAmt(t, journalKeyString, k))));

                                beginning += fisDef.getJItemAmt(t, journalKeyString, k);
                                credit += fisDef.getJItemAmt(t, journalKeyString, k);
                            }

                            flexTable.setHTML(row, 6, NumberFormat.getFormat("#,##0.00").format(beginning));
                            row += 1;
                        }
                    }
                }

                flexTable.setHTML(row, 0, "&nbsp;");
                flexTable.setHTML(row, 1, "&nbsp;");
                flexTable.setHTML(row, 2, "&nbsp;");
                flexTable.setHTML(row, 3, constants.total());
                flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(debit));
                flexTable.setHTML(row, 5, NumberFormat.getFormat("#,##0.00").format(Math.abs(credit)));
                flexTable.setHTML(row, 6, "&nbsp;");
                row += 1;

                debitTotal += debit;
                creditTotal += credit;

            }
        }

        flexTable.setHTML(row, 0, "&nbsp;");
        flexTable.setHTML(row, 1, "&nbsp;");
        flexTable.setHTML(row, 2, "&nbsp;");
        flexTable.setHTML(row, 3, constants.wholeTotal());
        flexTable.setHTML(row, 4, NumberFormat.getFormat("#,##0.00").format(debitTotal));
        flexTable.setHTML(row, 5, NumberFormat.getFormat("#,##0.00").format(Math.abs(creditTotal)));
        flexTable.setHTML(row, 6, "&nbsp;");
    }

    @Override
    public void setTrialData(T t, String comName) {
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
        double debit;
        double credit;
        double debitTotal = 0;
        double creditTotal = 0;
        for (int i = 0; i < fisDef.getACListSize(t); i++) {
            if (fisDef.getACIsEntry(t, i)) {
                String accChartKeyString = fisDef.getACKeyString(t, i);

                debit = 0;
                credit = 0;
                for (int j = 0; j < fisDef.getJListSize(t); j++) {
                    String journalKeyString = fisDef.getJKeyString(t, j);
                    for (int k = 0; k < fisDef.getJItemListSize(t, journalKeyString); k++) {
                        if (fisDef.getJItemACKeyString(t, journalKeyString, k).equals(accChartKeyString)) {
                            if (fisDef.getJItemAmt(t, journalKeyString, k) > 0) {
                                debit += fisDef.getJItemAmt(t, journalKeyString, k);
                            } else {
                                credit += fisDef.getJItemAmt(t, journalKeyString, k);
                            }
                        }
                    }
                }

                flexTable.setHTML(row, 0, fisDef.getACNo(t, i));
                flexTable.setHTML(row, 1, fisDef.getACName(t, i));
                flexTable.setHTML(row, 2, NumberFormat.getFormat("#,##0.00").format(debit));
                flexTable.setHTML(row, 3, NumberFormat.getFormat("#,##0.00").format(Math.abs(credit)));
                row += 1;

                debitTotal += debit;
                creditTotal += credit;
            }
        }

        flexTable.setHTML(row, 0, "&nbsp;");
        flexTable.setHTML(row, 1, constants.wholeTotal());
        flexTable.setHTML(row, 2, NumberFormat.getFormat("#,##0.00").format(debitTotal));
        flexTable.setHTML(row, 3, NumberFormat.getFormat("#,##0.00").format(Math.abs(creditTotal)));

    }
    
    @Override
    public void setFinData(final T t, final String comName, final String finHeaderKeyString) {
        flexTable.setStyleName("flexTable finance");
        
        // Set header
        flexTable.setHeaderHTML(0, 0, 4, comName);
        flexTable.setHeaderHTML(1, 0, 4, fisDef.getFinHeaderName(t, finHeaderKeyString));
        int month = fisDef.getFEndMonth(t);
        int year = fisDef.getFEndYear(t);
        flexTable.setHeaderHTML(2, 0, 4, constants.end() + genFormalDate(Utils.getLastDay(month, year), month, year));
        
        int row = 0;
        double var1 = 0;
        double var2 = 0;
        double var3 = 0;
        double var4 = 0;
        for(int i=0; i<fisDef.getFinItemListSize(t, finHeaderKeyString); i++){
            
            String finItemKeyString = fisDef.getFinItemKeyString(t, finHeaderKeyString, i);
            
            Comm comm = fisDef.getFinItemComm(t, finHeaderKeyString, finItemKeyString);
            PrintCon printCon = fisDef.getFinItemPrintCon(t, finHeaderKeyString, finItemKeyString);
            PrintStyle printStyle = fisDef.getFinItemPrintStyle(t, finHeaderKeyString, finItemKeyString);
            CalCon calCon = fisDef.getFinItemCalCon(t, finHeaderKeyString, finItemKeyString);
            
            if(comm.equals(Comm.TXT)){
                row = calPrint(printCon, row, fisDef.getFinItemArg(t, finHeaderKeyString,
                        finItemKeyString), 0, false, printStyle);
            }else if(comm.equals(Comm.ACCNO)){
                String accChartKeyString = fisDef.getFinItemArg(t, finHeaderKeyString,
                        finItemKeyString);
                double total = 0;
                for (int j = 0; j < fisDef.getJListSize(t); j++) {
                    String journalKeyString = fisDef.getJKeyString(t, j);
                    for (int k = 0; k < fisDef.getJItemListSize(t, journalKeyString); k++) {
                        if (fisDef.getJItemACKeyString(t, journalKeyString, k).equals(accChartKeyString)) {
                            total += fisDef.getJItemAmt(t, journalKeyString, k);
                        }
                    }
                }
                
                row = calPrint(printCon, row, fisDef.getACName(t, accChartKeyString), total, true, printStyle);
                
                var1 = calOperateVar(calCon, fisDef.getFinItemVar1(t, finHeaderKeyString,
                        finItemKeyString), var1, total);
                var2 = calOperateVar(calCon, fisDef.getFinItemVar2(t, finHeaderKeyString,
                        finItemKeyString), var2, total);
                var3 = calOperateVar(calCon, fisDef.getFinItemVar3(t, finHeaderKeyString,
                        finItemKeyString), var3, total);
                var4 = calOperateVar(calCon, fisDef.getFinItemVar4(t, finHeaderKeyString,
                        finItemKeyString), var4, total);
            }else if(comm.equals(Comm.PVAR1)){
                row = calPrint(printCon, row, fisDef.getFinItemArg(t, finHeaderKeyString,
                        finItemKeyString), var1, true, printStyle);
                
                var1 = calOperateVar(calCon, fisDef.getFinItemVar1(t, finHeaderKeyString,
                        finItemKeyString), var1, var1);
                var2 = calOperateVar(calCon, fisDef.getFinItemVar2(t, finHeaderKeyString,
                        finItemKeyString), var2, var1);
                var3 = calOperateVar(calCon, fisDef.getFinItemVar3(t, finHeaderKeyString,
                        finItemKeyString), var3, var1);
                var4 = calOperateVar(calCon, fisDef.getFinItemVar4(t, finHeaderKeyString,
                        finItemKeyString), var4, var1);
                
            }else if(comm.equals(Comm.PVAR2)){
                row = calPrint(printCon, row, fisDef.getFinItemArg(t, finHeaderKeyString,
                        finItemKeyString), var2, true, printStyle);
                
                var1 = calOperateVar(calCon, fisDef.getFinItemVar1(t, finHeaderKeyString,
                        finItemKeyString), var1, var2);
                var2 = calOperateVar(calCon, fisDef.getFinItemVar2(t, finHeaderKeyString,
                        finItemKeyString), var2, var2);
                var3 = calOperateVar(calCon, fisDef.getFinItemVar3(t, finHeaderKeyString,
                        finItemKeyString), var3, var2);
                var4 = calOperateVar(calCon, fisDef.getFinItemVar4(t, finHeaderKeyString,
                        finItemKeyString), var4, var2);
            }else if(comm.equals(Comm.PVAR3)){
                row = calPrint(printCon, row, fisDef.getFinItemArg(t, finHeaderKeyString,
                        finItemKeyString), var3, true, printStyle);
                
                var1 = calOperateVar(calCon, fisDef.getFinItemVar1(t, finHeaderKeyString,
                        finItemKeyString), var1, var3);
                var2 = calOperateVar(calCon, fisDef.getFinItemVar2(t, finHeaderKeyString,
                        finItemKeyString), var2, var3);
                var3 = calOperateVar(calCon, fisDef.getFinItemVar3(t, finHeaderKeyString,
                        finItemKeyString), var3, var3);
                var4 = calOperateVar(calCon, fisDef.getFinItemVar4(t, finHeaderKeyString,
                        finItemKeyString), var4, var3);
            }else if(comm.equals(Comm.PVAR4)){
                row = calPrint(printCon, row, fisDef.getFinItemArg(t, finHeaderKeyString,
                        finItemKeyString), var4, true, printStyle);
                
                var1 = calOperateVar(calCon, fisDef.getFinItemVar1(t, finHeaderKeyString,
                        finItemKeyString), var1, var4);
                var2 = calOperateVar(calCon, fisDef.getFinItemVar2(t, finHeaderKeyString,
                        finItemKeyString), var2, var4);
                var3 = calOperateVar(calCon, fisDef.getFinItemVar3(t, finHeaderKeyString,
                        finItemKeyString), var3, var4);
                var4 = calOperateVar(calCon, fisDef.getFinItemVar4(t, finHeaderKeyString,
                        finItemKeyString), var4, var4);
            }else{
                throw new AssertionError(comm);
            }
        }
        
    }
    
    private int calPrint(PrintCon printCon, int row, String text, double value, boolean isValue, PrintStyle printStyle){
        if(printCon.equals(PrintCon.PRINT)){
            print(row, text, value, isValue, printStyle);
            row += 1;
        }else if(printCon.equals(PrintCon.NOIFZERO)){
            if(value != 0){
                print(row, text, value, isValue, printStyle);
                row += 1;
            }
        }else if(printCon.equals(PrintCon.NOPRINT)){
            // Don't print
        }else{
            throw new AssertionError();
        }
        return row;
    }
    
    private void print(int row, String text, double value, boolean isValue, PrintStyle printStyle){
        flexTable.setHTML(row, 0, text);
        if(isValue){
            flexTable.setHTML(row, 1, NumberFormat.getFormat("#,##0.00;(#,##0.00)").format(value));
        }else{
            flexTable.setHTML(row, 1, "");
        }
        flexTable.setHTML(row, 2, "");
        flexTable.setHTML(row, 3, "");
        
        if(printStyle.equals(PrintStyle.CENTER)){
            flexCellFormatter.addStyleName(row, 0, center);
        }else if(printStyle.equals(PrintStyle.ULINE)){
            flexCellFormatter.addStyleName(row, 1, uline);
        }else if(printStyle.equals(PrintStyle.TWOULINES)){
            flexCellFormatter.addStyleName(row, 1, twoulines);
        }else if(printStyle.equals(PrintStyle.BLANK)){
            
        }else{
            throw new AssertionError(printStyle);
        }
    }
    
    private double calOperateVar(CalCon calCon, Operand operand, double var, double operator){
        if(calCon.equals(CalCon.CAL)){
            return operateVar(operand, var, operator);
        }else if(calCon.equals(CalCon.CALIFPOS)){
            if(operator > 0){
                return operateVar(operand, var, operator);
            }else{
                return var;
            }
        }else if(calCon.equals(CalCon.CALIFNE)){
            if(operator < 0){
                return operateVar(operand, var, operator);
            }else{
                return var;
            }
        }else{
            throw new AssertionError(calCon);
        }
    }
    
    private double operateVar(Operand operand, double var, double operator){
        if(operand.equals(Operand.PLUS)){
            return var + operator;
        }else if(operand.equals(Operand.MINUS)){
            return var - operator;
        }else if(operand.equals(Operand.CLEAR)){
            return 0;
        }else if(operand.equals(Operand.BLANK)){
            return var;
        }else{
            throw new AssertionError();
        }
    }

    private String genAccName(String accName, int level) {
        String s = accName;
        for (int i = 1; i < level; i++) {
            s = "&nbsp;&nbsp;&nbsp;&nbsp;" + s;
        }
        return s;
    }
    
    private String genTodayDate(){
        String dateS = DateTimeFormat.getFormat("dd/MM/yyyy").format(new Date());
        //TODO Convert to Thai year using company settings
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
    
    private int[] getBeginDateEndDate(T t, int beginDay, int beginMonth, int beginYear, int endDay, int endMonth, int endYear) {
        int[] dates = new int[6];
        dates[0] = beginDay != 0 ? beginDay : 1;
        dates[1] = beginMonth != 0 ? beginMonth : fisDef.getFBeginMonth(t);
        dates[2] = beginYear != 0 ? beginYear : fisDef.getFBeginYear(t);

        dates[3] = endDay != 0 ? endDay : Utils.getLastDay(fisDef.getFEndMonth(t), fisDef.getFEndYear(t));
        dates[4] = endMonth != 0 ? endMonth : fisDef.getFEndMonth(t);
        dates[5] = endYear != 0 ? endYear : fisDef.getFEndYear(t);

        return dates;
    }

    private String[] getBeginAccNoEndAccNo(T t, String beginAccNoKeyString, String endAccNoKeyString) {
        String[] accNos = new String[2];

        if (beginAccNoKeyString.equals(AllPlace.FIRST)) {
            for (int i = 0; i < fisDef.getACListSize(t); i++) {
                if (fisDef.getACIsEntry(t, i)) {
                    accNos[0] = fisDef.getACNo(t, i);
                    break;
                }
            }
        } else {
            accNos[0] = fisDef.getACNo(t, beginAccNoKeyString);
        }

        if (endAccNoKeyString.equals(AllPlace.LAST)) {
            for (int i = fisDef.getACListSize(t) - 1; i >= 0; i--) {
                if (fisDef.getACIsEntry(t, i)) {
                    accNos[1] = fisDef.getACNo(t, i);
                    break;
                }
            }
        } else {
            accNos[1] = fisDef.getACNo(t, endAccNoKeyString);
        }
        return accNos;
    }
}
