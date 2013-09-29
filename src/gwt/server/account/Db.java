package gwt.server.account;

import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;
import gwt.shared.model.SJournalType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.utils.SystemProperty;

public class Db {

    // Table names
    public static final String COM = "com";
    public static final String FISCAL_YEAR = "fiscal_year";
    public static final String ACC_GRP = "acc_grp";
    public static final String DOC_TYPE = "doc_type";
    public static final String JOURNAL_TYPE = "journal_type";
    public static final String ACC_CHART = "acc_chart";
    public static final String JOURNAL_HEADER = "journal_header";
    public static final String JOURNAL_ITEM = "journal_item";
    public static final String ACC_AMT = "acc_amt";

    // Table fields
    public static final String ID = "id";
    public static final String USER_KEY_STRING = "user_key_string";
    public static final String NAME = "name";
    public static final String CREATE_DATE = "create_date";
    public static final String BEGIN_MONTH = "begin_month";
    public static final String BEGIN_YEAR = "begin_year";
    public static final String END_MONTH = "end_month";
    public static final String END_YEAR = "end_year";
    public static final String SHORT_NAME = "short_name";
    public static final String JOURNAL_TYPE_ID = "journal_type_id";
    public static final String CODE = "code";
    public static final String JOURNAL_DESC = "journal_desc";
    public static final String ACC_GRP_ID = "acc_grp_id";
    public static final String PARENT_ACC_CHART_ID = "parent_acc_chart_id";
    public static final String NO = "no";
    public static final String TYPE = "type";
    public static final String LEVEL = "level";
    public static final String BEGINNING = "beginning";
    public static final String DOC_TYPE_ID = "doc_type_id";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String DESC = "desc";
    public static final String ACC_CHART_ID = "acc_chart_id";
    public static final String AMT = "amt";

    // Error messages
    public static final String NO_ROW_EFFECTED_ERR = "Failed, no row affected!";
    public static final String NO_GENERATED_KEYS_ERR = "Failed, no generated key obtained.";
    public static final String FISCAL_YEAR_TOO_SMALL_ERR = "Edit aborted! Some journals are not in the new range of fiscal year!";
    public static final String ACC_CHART_BEING_USED_ERR = "This account chart is being use in some journals.";

    public static Connection getDBConn() throws ClassNotFoundException, SQLException {

        String url = null;

        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
          // Load the class that provides the new "jdbc:google:mysql://" prefix.
          Class.forName("com.mysql.jdbc.GoogleDriver");
          url = "jdbc:google:mysql://your-project-id:your-instance-name/thai_accounting?user=root";
        } else {
          // Local MySQL instance to use during development.
          Class.forName("com.mysql.jdbc.Driver");
          url = "jdbc:mysql://localhost:3306/thai_accounting?user=root";
        }

        return DriverManager.getConnection(url);
    }

    public static SFiscalYear getSetup(Connection conn, long fisId) throws SQLException {
        SFiscalYear sFis = new SFiscalYear();

        String sql;
        PreparedStatement statement;
        ResultSet rs;

        sql = "SELECT * FROM journal_type WHERE fiscal_year_id = ? ORDER BY create_date ASC";
        statement = conn.prepareStatement(sql);
        statement.setLong(1, fisId);
        rs = statement.executeQuery();
        while (rs.next()) {
            SJournalType sJournalType = new SJournalType(
                    rs.getLong(ID) + "",
                    rs.getString(NAME),
                    rs.getString(SHORT_NAME),
                    new Date(rs.getTimestamp(CREATE_DATE).getTime()));
            sFis.addSJournalType(sJournalType);
        }

        sql = "SELECT * FROM doc_type WHERE fiscal_year_id = ? ORDER BY create_date ASC";
        statement = conn.prepareStatement(sql);
        statement.setLong(1, fisId);
        rs = statement.executeQuery();
        while (rs.next()) {
            SDocType sDocType = new SDocType(
                    rs.getLong(ID) + "",
                    rs.getLong(JOURNAL_TYPE_ID) + "",
                    rs.getString(CODE),
                    rs.getString(NAME),
                    rs.getString(JOURNAL_DESC),
                    new Date(rs.getTimestamp(CREATE_DATE).getTime()));
            sFis.addSDocType(sDocType);
        }

        sql = "SELECT * FROM acc_grp WHERE fiscal_year_id = ? ORDER BY create_date ASC";
        statement = conn.prepareStatement(sql);
        statement.setLong(1, fisId);
        rs = statement.executeQuery();
        while (rs.next()) {
            SAccGrp sAccGrp = new SAccGrp(
                    rs.getLong(ID) + "",
                    rs.getString(NAME),
                    new Date(rs.getTimestamp(CREATE_DATE).getTime()));
            sFis.addSAccGrp(sAccGrp);
        }

        sql = "SELECT * FROM acc_chart WHERE fiscal_year_id = ?";
        statement = conn.prepareStatement(sql);
        statement.setLong(1, fisId);
        rs = statement.executeQuery();
        while (rs.next()) {
            SAccChart sAccChart = new SAccChart(
                    rs.getLong(ID) + "",
                    rs.getLong(ACC_GRP_ID) + "",
                    rs.getLong(PARENT_ACC_CHART_ID) == 0 ? null : rs.getLong(PARENT_ACC_CHART_ID) + "",
                    rs.getString(NO),
                    rs.getString(NAME),
                    AccType.values()[rs.getInt(TYPE)],
                    rs.getInt(LEVEL),
                    rs.getDouble(BEGINNING));
            sFis.addSAccChart(sAccChart);
        }

        return sFis;
    }

    public static PreparedStatement getAddJTPreparedStatement(Connection conn, long fiscalYearId)
            throws SQLException {
        String sql = "INSERT INTO journal_type (id, fiscal_year_id, `name`, short_name) VALUES( ? , ? , ? , ? )";
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setNull(1, Types.INTEGER);
        statement.setLong(2, fiscalYearId);
        return statement;
    }

    public static long addJT(PreparedStatement statement, String name, String shortName)
            throws SQLException {
        statement.setString(3, name);
        statement.setString(4, shortName);
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException(NO_ROW_EFFECTED_ERR);
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new SQLException(NO_GENERATED_KEYS_ERR);
        }
    }

    public static PreparedStatement getAddDTPreparedStatement(Connection conn, long fiscalYearId)
            throws SQLException {
        String sql = "INSERT INTO doc_type (id, fiscal_year_id, journal_type_id, code, `name`, journal_desc) VALUES( ? , ? , ? , ? , ? , ? )";
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setNull(1, Types.INTEGER);
        statement.setLong(2, fiscalYearId);
        return statement;
    }

    public static long addDT(PreparedStatement statement, long jTId, String code, String name,
            String journalDesc) throws SQLException {
        statement.setLong(3, jTId);
        statement.setString(4, code);
        statement.setString(5, name);
        statement.setString(6, journalDesc);
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException(NO_ROW_EFFECTED_ERR);
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new SQLException(NO_GENERATED_KEYS_ERR);
        }
    }

    public static PreparedStatement getAddAGPreparedStatement(Connection conn, long fiscalYearId)
            throws SQLException {
        String sql = "INSERT INTO acc_grp (id, fiscal_year_id, `name`) VALUES( ? , ? , ? )";
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setNull(1, Types.INTEGER);
        statement.setLong(2, fiscalYearId);
        return statement;
    }

    public static long addAG(PreparedStatement statement, String name) throws SQLException {
        statement.setString(3, name);
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException(NO_ROW_EFFECTED_ERR);
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new SQLException(NO_GENERATED_KEYS_ERR);
        }
    }

    public static PreparedStatement getAddACPreparedStatement(Connection conn, long fiscalYearId)
            throws SQLException {
        String sql = "INSERT INTO acc_chart (id, fiscal_year_id, acc_grp_id, parent_acc_chart_id, `no`, `name`, `type`, `level`) VALUES( ? , ? , ? , ? , ? , ? , ? , ? )";
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setNull(1, Types.INTEGER);
        statement.setLong(2, fiscalYearId);
        return statement;
    }

    public static long addAC(PreparedStatement statement, String no, String name, long aGId,
            int level, AccType type, long parentACId) throws SQLException {
        statement.setLong(3, aGId);
        if (parentACId == 0) {
            statement.setNull(4, Types.INTEGER);
        } else {
            statement.setLong(4, parentACId);
        }
        statement.setString(5, no);
        statement.setString(6, name);
        statement.setInt(7, type.ordinal());
        statement.setInt(8, level);
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException(NO_ROW_EFFECTED_ERR);
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new SQLException(NO_GENERATED_KEYS_ERR);
        }
    }

    public static long addJournal(Connection conn, long fisId, SJournalHeader sJournal)
            throws SQLException {

        // Careful, conn for this method need to be a transaction to guarantee the accuracy of acc_amt

        // Add header
        String addHeaderSql = "INSERT INTO journal_header (id, fiscal_year_id, doc_type_id, `no`, `day`, `month`, `year`, `desc`) VALUES ( ? , ? , ? , ? , ?, ? , ? , ? )";
        PreparedStatement addHeaderStatement = conn.prepareStatement(addHeaderSql,
                Statement.RETURN_GENERATED_KEYS);
        addHeaderStatement.setNull(1, Types.INTEGER);
        addHeaderStatement.setLong(2, fisId);
        addHeaderStatement.setLong(3, Long.parseLong(sJournal.getDocTypeKeyString()));
        addHeaderStatement.setString(4, sJournal.getNo());
        addHeaderStatement.setInt(5, sJournal.getDay());
        addHeaderStatement.setInt(6, sJournal.getMonth());
        addHeaderStatement.setInt(7, sJournal.getYear());
        addHeaderStatement.setString(8, sJournal.getDesc());
        int affectedRows = addHeaderStatement.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
        }

        Long headerId;
        ResultSet generatedKeys = addHeaderStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            headerId = generatedKeys.getLong(1);
        } else {
            throw new SQLException(Db.NO_GENERATED_KEYS_ERR);
        }

        addJournalItems(conn, fisId, headerId, sJournal.getItemList());

        return headerId;
    }

    public static void addJournalItems(Connection conn, long fisId, long headerId,
            List<SJournalItem> itemList) throws SQLException {

        // Careful, conn for this method need to be a transaction to guarantee the accuracy of acc_amt

        // Add items
        String addItemSql = "INSERT INTO journal_item (id, journal_header_id, acc_chart_id, amt) VALUES ( ? , ? , ? , ? )";
        PreparedStatement addItemStatement = conn.prepareStatement(addItemSql);

        // Plus to acc_amt
        String plusSql = "INSERT INTO acc_amt (fiscal_year_id, acc_chart_id, amt) VALUES ( ?, ?, ? ) ON DUPLICATE KEY UPDATE amt = amt + ?";
        PreparedStatement plusStatement = conn.prepareStatement(plusSql);

        int affectedRows;

        for (SJournalItem sJournalItem : itemList) {
            addItemStatement.setNull(1, Types.INTEGER);
            addItemStatement.setLong(2, headerId);
            addItemStatement.setLong(3, Long.parseLong(sJournalItem.getAccChartKeyString()));
            addItemStatement.setDouble(4, sJournalItem.getAmt());

            affectedRows = addItemStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
            }

            plusStatement.setLong(1, fisId);
            plusStatement.setLong(2, Long.parseLong(sJournalItem.getAccChartKeyString()));
            plusStatement.setDouble(3, sJournalItem.getAmt());
            plusStatement.setDouble(4, sJournalItem.getAmt());

            affectedRows = plusStatement.executeUpdate();
            // With ON DUPLICATE KEY UPDATE, the affected-rows value per row is 1
            //     if the row is inserted as a new row and 2 if an existing row is updated.
            if (affectedRows != 1 && affectedRows != 2) {
                throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
            }
        }
    }

    public static String deleteFromDB(Connection conn, String tableName, String keyString)
            throws NumberFormatException, SQLException {

        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setLong(1, Long.parseLong(keyString));
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException(NO_ROW_EFFECTED_ERR);
        }
        return keyString;
    }

    public static String dot(String t1, String t2) {
        return t1 + "." + t2;
    }
}
