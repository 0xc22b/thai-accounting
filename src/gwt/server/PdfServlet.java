package gwt.server;

import gwt.client.place.AllPlace;
import gwt.server.account.Db;
import gwt.shared.SConstants;
import gwt.shared.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

@SuppressWarnings("serial")
public class PdfServlet extends HttpServlet {

    public static final String STYLE_NAME_RIGHT = "right";
    public static final String STYLE_NAME_DULINE = "duline";
    public static final String STYLE_NAME_AULINE = "auline";
    public static final String STYLE_NAME_PAGE_AVOID_BREAK_AFTER = "page-avoid-break-after";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {

        String action = req.getParameter(SConstants.ACTION);
        if (action.equals(SConstants.JOURNAL_ACTION)) {
            getJournalPdf(req, resp);
        } else if (action.equals(SConstants.LEDGER_ACTION)) {
            getLedgerPdf(req, resp);
        } else {
            test(resp);
        }
    }

    private void getJournalPdf(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String fisKeyString = req.getParameter(SConstants.FIS_KEY_STRING);
        String journalTypeKeyString = req.getParameter(SConstants.JOURNAL_TYPE_KEY_STRING);
        int[] dates = extractDate(req);;
        String comName = req.getParameter(SConstants.COM_NAME);
        String journalTypeName = req.getParameter(SConstants.JOURNAL_TYPE_NAME);
        String lang = req.getParameter(SConstants.LANG);

        try {
            Connection conn = Db.getDBConn();
            try {

                StringBuilder sb = new StringBuilder();

                ResultSet rs = Db.getJournal(conn, fisKeyString, journalTypeKeyString, dates);

                genBeforeBodyHtml(sb);
                genJournalBodyHtml(sb, rs, dates, comName, journalTypeName, lang);
                genAfterBodyHtml(sb);

                ITextRenderer renderer = new ITextRenderer();
                ITextFontResolver fontResolver = renderer.getFontResolver();
                fontResolver.addFont("browau.ttf", BaseFont.IDENTITY_H, true);

                // parse the markup into an xml Document
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(new ByteArrayInputStream(
                                sb.toString().getBytes("UTF-8")));
                renderer.setDocument(doc, null);
                renderer.layout();

                resp.setContentType("application/pdf");
                OutputStream os = resp.getOutputStream();

                renderer.createPDF(os);
                os.close();
            } finally {
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (DocumentException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void getLedgerPdf(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String fisKeyString = req.getParameter(SConstants.FIS_KEY_STRING);
        String beginACNo = req.getParameter(SConstants.BEGIN_ACC_NO);
        String endACNo = req.getParameter(SConstants.END_ACC_NO);
        int[] dates = extractDate(req);
        boolean doShowAll = req.getParameter(SConstants.DO_SHOW_ALL).equals(AllPlace.SHOW_ALL);
        String comName = req.getParameter(SConstants.COM_NAME);
        String lang = req.getParameter(SConstants.LANG);

        try {
            Connection conn = Db.getDBConn();
            try {

                StringBuilder sb = new StringBuilder();

                ResultSet rs = Db.getLedger(conn, fisKeyString, beginACNo, endACNo, dates);

                genBeforeBodyHtml(sb);
                genLedgerBodyHtml(sb, rs, beginACNo, endACNo, dates, doShowAll, comName, lang);
                genAfterBodyHtml(sb);

                ITextRenderer renderer = new ITextRenderer();
                ITextFontResolver fontResolver = renderer.getFontResolver();
                fontResolver.addFont("browau.ttf", BaseFont.IDENTITY_H, true);

                // parse the markup into an xml Document
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(new ByteArrayInputStream(
                        sb.toString().getBytes("UTF-8")));
                renderer.setDocument(doc, null);
                renderer.layout();

                resp.setContentType("application/pdf");
                OutputStream os = resp.getOutputStream();

                renderer.createPDF(os);
                os.close();
            } finally {
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (DocumentException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void test(HttpServletResponse resp) throws ServletException, IOException {

        try {
            ITextRenderer renderer = new ITextRenderer();
            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont("browau.ttf", BaseFont.IDENTITY_H, true);

            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version='1.0' encoding='UTF-8'?>");
            sb.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
            sb.append("<head>");
            sb.append("<link rel='stylesheet' type='text/css' href='");
            sb.append(getServletContext().getRealPath("/css/printstyle-alpha.css"));
            sb.append("' media='print' />");
            sb.append("</head>");
            sb.append("<body>");
            sb.append("<table><tr><td class='aduline'>");
            sb.append("ภาษาไทยง่ายนิดเดียว ยากเยอะ");
            sb.append("</td></tr></table>");
            sb.append("</body>");
            sb.append("</html>");

            // parse the markup into an xml Document
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
            renderer.setDocument(doc, null);
            renderer.layout();

            resp.setContentType("application/pdf");
            OutputStream os = resp.getOutputStream();

            renderer.createPDF(os);
            os.close();

            //resp.addHeader("Content-Type", "application/force-download");
            //resp.addHeader("Content-Disposition", "attachment; filename=\"ledger.pdf\"");
            //resp.getOutputStream().write(output.toByteArray());

            /*try {
            BaseFont baseFont = BaseFont.createFont("browau.ttf", BaseFont.IDENTITY_H, true);
            Font font = new Font(baseFont, 12);
            Document pdfDocument = new Document();
            PdfWriter.getInstance(pdfDocument, output);
            pdfDocument.open();
            Paragraph p1 = new Paragraph(new Chunk("ทดสอบ", font));
            pdfDocument.add(p1);
            pdfDocument.close();
            } catch (DocumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
        } catch (DocumentException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String genJournalBodyHtml(StringBuilder sb, ResultSet rs, int[] dates,
            String comName, String journalTypeName, String lang) throws SQLException {

        DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");

        sb.append("<table class='flexTable journal'>");
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th colspan='3'>" + comName + "</th>");
        sb.append("<th colspan='2'>" + genTodayDate(lang) + "</th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th colspan='5'>" + journalTypeName + "</th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th colspan='5'>");
        sb.append(SConstants.BEGIN(lang) + " " + genFormalDate(dates[0], dates[1], dates[2], lang) + " "
                + SConstants.END(lang) + " " + genFormalDate(dates[3], dates[4], dates[5], lang));
        sb.append("</th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th>" + SConstants.ACC_NO(lang) + "</th>");
        sb.append("<th>" + SConstants.ACC_NAME(lang) + "</th>");
        sb.append("<th>" + SConstants.DESC(lang) + "</th>");
        sb.append("<th>" + SConstants.DEBIT(lang) + "</th>");
        sb.append("<th>" + SConstants.CREDIT(lang) + "</th>");
        sb.append("</tr>");
        sb.append("</thead><tbody>");

        long jId = 0;

        double totalDebit = 0.0;
        double totalCredit = 0.0;
        double wholeTotalDebit = 0.0;
        double wholeTotalCredit = 0.0;

        while (rs.next()) {

            long id = rs.getLong(Db.dot(Db.JOURNAL_HEADER, Db.ID));
            if ( jId == 0 || jId != id) {

                if (jId != 0) {
                    genJournalTotalHtml(sb, df, SConstants.TOTAL(lang),
                            STYLE_NAME_AULINE, totalDebit, totalCredit);

                    wholeTotalDebit += totalDebit;
                    wholeTotalCredit += totalCredit;

                    totalDebit = 0.0;
                    totalCredit = 0.0;
                }

                jId = id;

                genJournalBeginningHtml(
                        sb,
                        rs.getInt(Db.dot(Db.JOURNAL_HEADER, Db.DAY)),
                        rs.getInt(Db.dot(Db.JOURNAL_HEADER, Db.MONTH)),
                        rs.getInt(Db.dot(Db.JOURNAL_HEADER, Db.YEAR)),
                        rs.getString(Db.dot(Db.JOURNAL_HEADER, Db.NO)));
            }

            double amt = rs.getDouble(Db.dot(Db.JOURNAL_ITEM, Db.AMT));
            if (amt >= 0) {
                totalDebit += amt;
            } else {
                totalCredit += amt;
            }

            genJournalItemHtml(
                    sb,
                    df,
                    rs.getString(Db.dot(Db.ACC_CHART, Db.NO)),
                    rs.getString(Db.dot(Db.ACC_CHART, Db.NAME)),
                    rs.getString(Db.dot(Db.JOURNAL_HEADER, Db.DESC)),
                    amt);
        }

        // Total for the last journal
        genJournalTotalHtml(sb, df, SConstants.TOTAL(lang), STYLE_NAME_AULINE,
                totalDebit, totalCredit);

        wholeTotalDebit += totalDebit;
        wholeTotalCredit += totalCredit;

        // Whole total
        genJournalTotalHtml(sb, df, SConstants.WHOLE_TOTAL(lang), STYLE_NAME_DULINE,
                wholeTotalDebit, wholeTotalCredit);

        sb.append("</tbody></table>");

        return sb.toString();
    }

    private void genJournalBeginningHtml(StringBuilder sb, int day, int month, int year,
            String no) {

        sb.append("<tr class='" + STYLE_NAME_PAGE_AVOID_BREAK_AFTER + "'>");
        sb.append("<td colspan='5'>");
        sb.append(day + "/" + month + "/" + year + "&#160;&#160;&#160;&#160;" + no);
        sb.append("</td>");
        sb.append("</tr>");
    }

    private void genJournalItemHtml(StringBuilder sb, DecimalFormat df, String accNo,
            String accName, String desc, double amt) {

        sb.append("<tr>");

        sb.append("<td>");
        sb.append(accNo);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(accName);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(desc);
        sb.append("</td>");

        sb.append("<td>");
        sb.append(amt >= 0 ? df.format(Math.abs(amt)) : "&#160;");
        sb.append("</td>");

        sb.append("<td>");
        sb.append(amt >= 0 ? "&#160;" : df.format(Math.abs(amt)));
        sb.append("</td>");

        sb.append("</tr>");
    }

    private void genJournalTotalHtml(StringBuilder sb, DecimalFormat df, String totalConstant,
            String printStyle, double totalDebit, double totalCredit) {
        sb.append("<tr>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>" + totalConstant + "</td>");
        sb.append("<td class='" + printStyle +"'>");
        sb.append(df.format(totalDebit));
        sb.append("</td>");
        sb.append("<td class='" + printStyle + "'>");
        sb.append(df.format(Math.abs(totalCredit)));
        sb.append("</td>");
        sb.append("</tr>");

        sb.append("<tr>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("</tr>");
    }

    private String genLedgerBodyHtml(StringBuilder sb, ResultSet rs, String beginACNo,
            String endACNo, int[] dates, boolean doShowAll, String comName, String lang)
            throws SQLException {

        DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");

        sb.append("<table class='flexTable ledger'>");
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th colspan='4'>" + comName + "</th>");
        sb.append("<th colspan='3'>" + genTodayDate(lang) + "</th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th colspan='7'>" + SConstants.LEDGER(lang) + "</th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th colspan='7'>");
        sb.append(SConstants.ACC_NO(lang) + " " + beginACNo + " - " + endACNo + " ("
                + genFormalDate(dates[0], dates[1], dates[2], lang) + " - "
                + genFormalDate(dates[3], dates[4], dates[5], lang) + ")");
        sb.append("</th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<th>" + SConstants.DATE(lang) + "</th>");
        sb.append("<th>" + SConstants.JOURNAL_TYPE(lang) + "</th>");
        sb.append("<th>" + SConstants.JOURNAL_NO(lang) + "</th>");
        sb.append("<th>" + SConstants.DESC(lang) + "</th>");
        sb.append("<th>" + SConstants.DEBIT(lang) + "</th>");
        sb.append("<th>" + SConstants.CREDIT(lang) + "</th>");
        sb.append("<th>" + SConstants.REMAINING(lang) + "</th>");
        sb.append("</tr>");
        sb.append("</thead><tbody>");

        long acId = 0;
        boolean doPrintTotal = false;

        double remaining = 0.0;
        double totalDebit = 0.0;
        double totalCredit = 0.0;
        double wholeTotalDebit = 0.0;
        double wholeTotalCredit = 0.0;

        while (rs.next()) {

            long id = rs.getLong(Db.dot(Db.ACC_CHART, Db.ID));
            long jItemId = rs.getLong(Db.dot(Db.JOURNAL_ITEM, Db.ID));
            if ( acId == 0 || acId != id) {
                acId = id;
                remaining = rs.getDouble(Db.dot(Db.ACC_CHART, Db.BEGINNING));

                if (doPrintTotal) {
                    genLedgerTotalHtml(sb, df, SConstants.TOTAL(lang), STYLE_NAME_AULINE,
                            totalDebit, totalCredit);

                    wholeTotalDebit += totalDebit;
                    wholeTotalCredit += totalCredit;

                    doPrintTotal = false;
                    totalDebit = 0.0;
                    totalCredit = 0.0;
                }

                if (doShowAll || !Utils.isZero(remaining, 2) || jItemId != 0) {
                    genLedgerBeginningHtml(
                            sb,
                            df,
                            rs.getString(Db.dot(Db.ACC_CHART, Db.NO)),
                            rs.getString(Db.dot(Db.ACC_CHART, Db.NAME)),
                            remaining);
                    doPrintTotal = true;
                }
            }

            if (jItemId != 0) {

                double amt = rs.getDouble(Db.dot(Db.JOURNAL_ITEM, Db.AMT));
                remaining += amt;

                if (amt >= 0) {
                    totalDebit += amt;
                } else {
                    totalCredit += amt;
                }

                genLedgerItemHtml(
                        sb,
                        df,
                        rs.getInt(Db.dot(Db.JOURNAL_HEADER, Db.DAY)),
                        rs.getInt(Db.dot(Db.JOURNAL_HEADER, Db.MONTH)),
                        rs.getInt(Db.dot(Db.JOURNAL_HEADER, Db.YEAR)),
                        rs.getString(Db.dot(Db.JOURNAL_TYPE, Db.SHORT_NAME)),
                        rs.getString(Db.dot(Db.JOURNAL_HEADER, Db.NO)),
                        rs.getString(Db.dot(Db.JOURNAL_HEADER, Db.DESC)),
                        amt,
                        remaining);
            }
        }

        // Total for the last printed account chart item
        if (doPrintTotal) {
            genLedgerTotalHtml(sb, df, SConstants.TOTAL(lang), STYLE_NAME_AULINE,
                    totalDebit, totalCredit);

            wholeTotalDebit += totalDebit;
            wholeTotalCredit += totalCredit;
        }

        // Whole total
        genLedgerTotalHtml(sb, df, SConstants.WHOLE_TOTAL(lang), STYLE_NAME_DULINE,
                wholeTotalDebit, wholeTotalCredit);

        sb.append("</tbody></table>");

        return sb.toString();
    }

    private void genLedgerBeginningHtml(StringBuilder sb, DecimalFormat df, String acNo,
            String acName, double beginning) {

        sb.append("<tr>");
        sb.append("<td colspan='6'>");
        sb.append(acNo + "&#160;&#160;&#160;&#160;" + acName);
        sb.append("</td>");
        sb.append("<td class='" + STYLE_NAME_RIGHT + "'>");
        sb.append(df.format(beginning));
        sb.append("</td>");
        sb.append("</tr>");
    }

    private void genLedgerItemHtml(StringBuilder sb, DecimalFormat df, int day, int month,
            int year, String jTShortName, String no, String desc, double amt, double remaining) {

        sb.append("<tr>");
        sb.append("<td>");
        sb.append(day + "/" + month + "/" + year);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(jTShortName);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(no);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(desc);
        sb.append("</td>");

        sb.append("<td>");
        sb.append(amt >= 0 ? df.format(Math.abs(amt)) : "&#160;");
        sb.append("</td>");

        sb.append("<td>");
        sb.append(amt >= 0 ? "&#160;" : df.format(Math.abs(amt)));
        sb.append("</td>");

        sb.append("<td>");
        sb.append(df.format(remaining));
        sb.append("</td>");
        sb.append("</tr>");
    }

    private void genLedgerTotalHtml(StringBuilder sb, DecimalFormat df, String totalConstant,
            String printStyle, double totalDebit, double totalCredit) {

        sb.append("<tr>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>" + totalConstant + "</td>");
        sb.append("<td class='" + printStyle +"'>");
        sb.append(df.format(totalDebit));
        sb.append("</td>");
        sb.append("<td class='" + printStyle + "'>");
        sb.append(df.format(Math.abs(totalCredit)));
        sb.append("</td>");
        sb.append("<td>&#160;</td>");
        sb.append("</tr>");

        sb.append("<tr>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("<td>&#160;</td>");
        sb.append("</tr>");
    }

    private void genBeforeBodyHtml(StringBuilder sb) {
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
        sb.append("<head>");
        sb.append("<link rel='stylesheet' type='text/css' href='");
        sb.append(getServletContext().getRealPath("/css/printstyle-alpha.css"));
        sb.append("' media='print' />");
        sb.append("</head>");
        sb.append("<body>");
    }

    private void genAfterBodyHtml(StringBuilder sb) {
        sb.append("</body>");
        sb.append("</html>");
    }

    private String genTodayDate(String lang){

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String dateS = df.format(new Date());
        String yearS = dateS.split("/")[2];

        int year =  Integer.parseInt(yearS);
        if (lang.equals("th")) {
            year += 543;
        }
        return dateS.substring(0, 6) + year;
    }

    private String genFormalDate(int day, int month, int year, String lang){
        String s = "";
        s += day + " ";

        switch(month){
        case 1:
            s += SConstants.JAN(lang) + " ";
            break;
        case 2:
            s += SConstants.FEB(lang) + " ";
            break;
        case 3:
            s += SConstants.MAR(lang) + " ";
            break;
        case 4:
            s += SConstants.APR(lang) + " ";
            break;
        case 5:
            s += SConstants.MAY(lang) + " ";
            break;
        case 6:
            s += SConstants.JUN(lang) + " ";
            break;
        case 7:
            s += SConstants.JUL(lang) + " ";
            break;
        case 8:
            s += SConstants.AUG(lang) + " ";
            break;
        case 9:
            s += SConstants.SEP(lang) + " ";
            break;
        case 10:
            s += SConstants.OCT(lang) + " ";
            break;
        case 11:
            s += SConstants.NOV(lang) + " ";
            break;
        case 12:
            s += SConstants.DEC(lang) + " ";
            break;
        default:
            throw new AssertionError(month);
        }

        s += year;

        return s;
    }

    private int[] extractDate(HttpServletRequest req){
        int[] date = new int[6];
        date[0] = Integer.parseInt(req.getParameter(SConstants.BEGIN_DAY));
        date[1] = Integer.parseInt(req.getParameter(SConstants.BEGIN_MONTH));
        date[2] = Integer.parseInt(req.getParameter(SConstants.BEGIN_YEAR));
        date[3] = Integer.parseInt(req.getParameter(SConstants.END_DAY));
        date[4] = Integer.parseInt(req.getParameter(SConstants.END_MONTH));
        date[5] = Integer.parseInt(req.getParameter(SConstants.END_YEAR));
        return date;
    }
}
