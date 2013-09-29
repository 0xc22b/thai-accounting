package gwt.server;

import gwt.server.account.Db;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class StressTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {

        String respText;

        String fisKeyString = req.getParameter("fis");
        String monthString = req.getParameter("month");
        String yearString = req.getParameter("year");

        long fisId = Long.parseLong(fisKeyString);
        int month = Integer.parseInt(monthString);
        int year = Integer.parseInt(yearString);

        addJournals(fisId, month, year);

        respText = "Journals added to the month!";

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(respText);
        out.flush();
        out.close();
    }

    private void addJournals(long fisId, int month, int year) {

        try {
            Connection conn = Db.getDBConn();
            try {

                // Get doc types and acc charts
                SFiscalYear sFis = Db.getSetup(conn, fisId);

                List<SDocType> sDTList = new ArrayList<SDocType>();
                sDTList = sFis.getSDocTypeList();

                List<SAccChart> sACList = new ArrayList<SAccChart>();
                for (SAccChart sAccChart : sFis.getSAccChartList()) {
                    if (sAccChart.getType() == AccType.ENTRY) {
                        sACList.add(sAccChart);
                    }
                }

                Date d = new Date();

                // Transaction
                conn.setAutoCommit(false);

                SJournalItem sJItem;
                for (int i = 1; i <= 26; i++) { // Total of days in 1 month
                    for (int j = 1; j <= 40; j++) { // Total of journals per day

                        SJournalHeader sJournal = new SJournalHeader(
                                null,
                                getRandomDTKeyString(sDTList),
                                genNo(year, month, i, j),
                                (i % 30),
                                month,
                                year,
                                "Desc for " + year + "-" + month + "-" + i + "-" + j);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 28230.0, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -28230.0, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 6720.0, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -6720.0, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 88110.0, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -88110.0, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 5210.37, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -5210.37, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 10.99, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -10.99, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 99000.30, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -99000.30, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 3456.78, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -3456.78, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 21098.76, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -21098.76, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 6785.00, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -6785.00, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), 987.65, d);
                        sJournal.addItem(sJItem);

                        sJItem = new SJournalItem(null, getRandomACKeyString(sACList), -987.65, d);
                        sJournal.addItem(sJItem);

                        Db.addJournal(conn, fisId, sJournal);

                        // end transaction
                        conn.commit();
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e.getCause());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    private String getRandomACKeyString(List<SAccChart> sACList) {
        int i = (int)(Math.random() * (sACList.size() - 1));
        return sACList.get(i).getKeyString();
    }

    private String getRandomDTKeyString(List<SDocType> sDTList) {
        int i = (int)(Math.random() * (sDTList.size() - 1));
        return sDTList.get(i).getKeyString();
    }

    private String genNo(int year, int month, int i, int j) {
        String monthStr = month <= 9 ? "0" + month : month + "";
        String iStr = i <= 9 ? "0" + i : i + "";
        String jStr = j <= 9 ? "0" + j : j + "";
        return year + monthStr + iStr + jStr;
    }
}
