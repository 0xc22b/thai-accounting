package gwt.server;

import gwt.client.model.RpcService;
import gwt.server.account.CreateSetup;
import gwt.server.account.Db;
import gwt.server.user.UserManager;
import gwt.server.user.model.User;
import gwt.server.user.model.UserData;
import gwt.shared.NotLoggedInException;
import gwt.shared.SConstants;
import gwt.shared.Utils;
import gwt.shared.model.SAccAmt;
import gwt.shared.model.SAccChart;
import gwt.shared.model.SAccChart.AccType;
import gwt.shared.model.SAccGrp;
import gwt.shared.model.SCom;
import gwt.shared.model.SComList;
import gwt.shared.model.SDocType;
import gwt.shared.model.SFiscalYear;
import gwt.shared.model.SJournalHeader;
import gwt.shared.model.SJournalItem;
import gwt.shared.model.SJournalType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {

    @Override
    public SComList getComList(String sSID, String sID) throws NotLoggedInException {
        User user = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            user = UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                SComList sComList = new SComList();

                String sql = "SELECT * FROM com LEFT JOIN fiscal_year ON com.id = fiscal_year.com_id WHERE com.user_key_string = ? ORDER BY com.create_date ASC, fiscal_year.begin_month ASC, fiscal_year.begin_year ASC";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, user.getKeyString());
                ResultSet rs = statement.executeQuery();

                SCom sCom = null;
                long id;
                while (rs.next()) {
                    id = rs.getLong(Db.dot(Db.COM, Db.ID));
                    if (sCom == null || !sCom.getKeyString().equals(id + "")) {
                        sCom = new SCom(id + "", rs.getString(Db.dot(Db.COM, Db.NAME)),
                                new Date(rs.getTimestamp(Db.CREATE_DATE).getTime()));
                        sComList.addSCom(sCom);
                    }

                    id = rs.getLong(Db.dot(Db.FISCAL_YEAR, Db.ID));
                    if (id != 0) {
                        SFiscalYear sFis = new SFiscalYear(
                                id + "",
                                rs.getInt(Db.dot(Db.FISCAL_YEAR, Db.BEGIN_MONTH)),
                                rs.getInt(Db.dot(Db.FISCAL_YEAR, Db.BEGIN_YEAR)),
                                rs.getInt(Db.dot(Db.FISCAL_YEAR, Db.END_MONTH)),
                                rs.getInt(Db.dot(Db.FISCAL_YEAR, Db.END_YEAR)));
                        sCom.addSFis(sFis);
                    }
                }
                return sComList;
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

    @Override
    public String addCom(String sSID, String sID, final SCom sCom) throws NotLoggedInException {

        User user = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            user = UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                String sql = "INSERT INTO com (id, user_key_string, `name`) VALUES( ? , ? , ? )";
                PreparedStatement statement = conn.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS);
                statement.setNull(1, Types.INTEGER);
                statement.setString(2, user.getKeyString());
                statement.setString(3, sCom.getName());
                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1) + "";
                } else {
                    throw new SQLException(Db.NO_GENERATED_KEYS_ERR);
                }
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

    @Override
    public String editCom(String sSID, String sID, final SCom sCom) throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                String sql = "UPDATE com SET `name` = ? WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, sCom.getName());
                statement.setLong(2, Long.parseLong(sCom.getKeyString()));
                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                return sCom.getKeyString();
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

    @Override
    public String deleteCom(String sSID, String sID, String keyString) throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                // Can be deleted only if no restrict references
                return Db.deleteFromDB(conn, Db.COM, keyString);
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

    @Override
    public String addFis(String sSID, String sID, final String comKeyString,
            final int setupType, final SFiscalYear sFis) throws NotLoggedInException {
        User user = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            user = UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                long comId = Long.parseLong(comKeyString);

                String sql = "INSERT INTO fiscal_year (id, com_id, begin_month, begin_year, end_month, end_year) VALUES( ? , ? , ? , ? , ? , ? )";
                PreparedStatement statement = conn.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS);
                statement.setNull(1, Types.INTEGER);
                statement.setLong(2, comId);
                statement.setInt(3, sFis.getBeginMonth());
                statement.setInt(4, sFis.getBeginYear());
                statement.setInt(5, sFis.getEndMonth());
                statement.setInt(6, sFis.getEndYear());
                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {

                    long fisId = generatedKeys.getLong(1);

                    if(setupType == SConstants.ADD_FIS_WITH_DEFAULT_SETUP){
                        UserData userData = UserManager.getUserData(user.getKey());
                        if (userData.getLang().equals("th")) {
                            CreateSetup.createInThai(conn, fisId);
                        } else {
                            CreateSetup.createInEnglish(conn, fisId);
                        }
                    } else if (setupType == SConstants.ADD_FIS_WITH_PREVIOUS_SETUP) {
                        CreateSetup.createFromPrev(conn, comId, fisId);
                    }

                    return fisId + "";
                } else {
                    throw new SQLException(Db.NO_GENERATED_KEYS_ERR);
                }
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

    @Override
    public String editFis(String sSID, String sID, final SFiscalYear sFis)
            throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                Long fisId = Long.parseLong(sFis.getKeyString());

                String sql = "SELECT id FROM journal_header WHERE fiscal_year_id = ? && (year < ? || (year = ? && month < ?) || year > ? || (year = ? && month > ?)) LIMIT 1";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setLong(1, fisId);
                statement.setInt(2, sFis.getBeginYear());
                statement.setInt(3, sFis.getBeginYear());
                statement.setInt(4, sFis.getBeginMonth());
                statement.setInt(5, sFis.getEndYear());
                statement.setInt(6, sFis.getEndYear());
                statement.setInt(7, sFis.getEndMonth());
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    throw new IllegalArgumentException(Db.FISCAL_YEAR_TOO_SMALL_ERR);
                }

                sql = "UPDATE fiscal_year SET begin_month = ?, begin_year = ?, end_month = ?, end_year = ? WHERE id = ?";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, sFis.getBeginMonth());
                statement.setInt(2, sFis.getBeginYear());
                statement.setInt(3, sFis.getEndMonth());
                statement.setInt(4, sFis.getEndYear());
                statement.setLong(5, fisId);
                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                return sFis.getKeyString();
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

    @Override
    public String deleteFis(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                // Can be deleted only if no restrict references
                return Db.deleteFromDB(conn, Db.FISCAL_YEAR, keyString);
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

    @Override
    public SFiscalYear getSetup(String sSID, String sID, final String fisKeyString)
            throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                Long fisId = Long.parseLong(fisKeyString);
                return Db.getSetup(conn, fisId);
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

    @Override
    public String addJournalType(String sSID, String sID, final String fisKeyString,
            final SJournalType sJournalType) throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                PreparedStatement statement = Db.getAddJTPreparedStatement(conn,
                        Long.parseLong(fisKeyString));
                return Db.addJT(statement, sJournalType.getName(), sJournalType.getShortName()) + "";
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

    @Override
    public String editJournalType(String sSID, String sID, final SJournalType sJournalType)
            throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                String sql = "UPDATE journal_type SET `name` = ?, short_name = ? WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, sJournalType.getName());
                statement.setString(2, sJournalType.getShortName());
                statement.setLong(3, Long.parseLong(sJournalType.getKeyString()));
                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                return sJournalType.getKeyString();
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

    @Override
    public String deleteJournalType(String sSID, String sID, String keyString)
            throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                // Checking that it can be deleted was delegated to MySQL Foreign key
                return Db.deleteFromDB(conn, Db.JOURNAL_TYPE, keyString);
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

    @Override
    public String addDocType(String sSID, String sID, final String fisKeyString,
            final SDocType sDocType) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {

                PreparedStatement statement = Db.getAddDTPreparedStatement(conn,
                        Long.parseLong(fisKeyString));
                return Db.addDT(
                        statement,
                        Long.parseLong(sDocType.getJournalTypeKeyString()),
                        sDocType.getCode(),
                        sDocType.getName(),
                        sDocType.getJournalDesc()) + "";
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

    @Override
    public String editDocType(String sSID, String sID, final SDocType sDocType)
            throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                String sql = "UPDATE doc_type SET journal_type_id = ?, code = ?, `name` = ?, journal_desc = ? WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setLong(1, Long.parseLong(sDocType.getJournalTypeKeyString()));
                statement.setString(2, sDocType.getCode());
                statement.setString(3, sDocType.getName());
                statement.setString(4, sDocType.getJournalDesc());
                statement.setLong(5, Long.parseLong(sDocType.getKeyString()));
                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }
                return sDocType.getKeyString();
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

    @Override
    public String deleteDocType(String sSID, String sID, final String keyString)
            throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                // Checking that it can be deleted was delegated to MySQL Foreign key
                return Db.deleteFromDB(conn, Db.DOC_TYPE, keyString);
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

    @Override
    public String addAccGrp(String sSID, String sID, final String fisKeyString,
            final SAccGrp sAccGrp) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                PreparedStatement statement = Db.getAddAGPreparedStatement(conn,
                        Long.parseLong(fisKeyString));
                return Db.addAG(statement, sAccGrp.getName()) + "";
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

    @Override
    public String editAccGrp(String sSID, String sID, final SAccGrp sAccGrp)
            throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                String sql = "UPDATE acc_grp SET `name` = ? WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, sAccGrp.getName());
                statement.setLong(2, Long.parseLong(sAccGrp.getKeyString()));
                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                return sAccGrp.getKeyString();
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

    @Override
    public String deleteAccGrp(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                // Checking that it can be deleted was delegated to MySQL Foreign key
                return Db.deleteFromDB(conn, Db.ACC_GRP, keyString);
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

    @Override
    public String addAccChart(String sSID, String sID, final String fisKeyString,
            final SAccChart sAccChart) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                long parentAccChartId = sAccChart.getParentAccChartKeyString() == null ? 0
                        : Long.parseLong(sAccChart.getParentAccChartKeyString());

                PreparedStatement statement = Db.getAddACPreparedStatement(conn,
                        Long.parseLong(fisKeyString));
                return Db.addAC(
                        statement,
                        sAccChart.getNo(),
                        sAccChart.getName(),
                        Long.parseLong(sAccChart.getAccGroupKeyString()),
                        sAccChart.getLevel(),
                        sAccChart.getType(),
                        parentAccChartId) + "";
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

    @Override
    public String editAccChart(String sSID, String sID, String fisKeyString,
            final SAccChart sAccChart) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                long accChartId = Long.parseLong(sAccChart.getKeyString());

                // In case of change from control to entry, already check in client
                // Change from entry to control, check if there is any journals
                if (sAccChart.getType() == AccType.CONTROL) {
                    String journalSql = "SELECT journal_item.id FROM journal_item JOIN journal_header ON journal_item.journal_header_id = journal_header.id WHERE journal_item.acc_chart_id = ? && journal_header.fiscal_year_id = ? LIMIT 1";
                    PreparedStatement journalStatement = conn.prepareStatement(journalSql);
                    journalStatement.setLong(1, Long.parseLong(fisKeyString));
                    journalStatement.setLong(2, accChartId);
                    ResultSet rs = journalStatement.executeQuery();
                    if (rs.next()) {
                        throw new SQLException(Db.ACC_CHART_BEING_USED_ERR);
                    }
                }

                String sql = "UPDATE acc_chart SET acc_grp_id = ?, parent_acc_chart_id = ?, `no` = ?, `name` = ?, `type` = ?, `level` = ? WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setLong(1, Long.parseLong(sAccChart.getAccGroupKeyString()));
                if (sAccChart.getParentAccChartKeyString() == null) {
                    statement.setNull(2, Types.INTEGER);
                } else {
                    statement.setLong(2, Long.parseLong(sAccChart.getParentAccChartKeyString()));
                }
                statement.setString(3, sAccChart.getNo());
                statement.setString(4, sAccChart.getName());
                statement.setInt(5, sAccChart.getType().ordinal());
                statement.setInt(6, sAccChart.getLevel());
                statement.setLong(7, accChartId);
                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                return accChartId + "";
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

    @Override
    public String deleteAccChart(String sSID, String sID, String keyString) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                // Checking that it can be deleted was delegated to MySQL Foreign key
                return Db.deleteFromDB(conn, Db.ACC_CHART, keyString);
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

    @Override
    public String setBeginning(String sSID, String sID, String accChartKeyString, double beginning)
            throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                String sql = "UPDATE acc_chart SET beginning = ? WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setDouble(1, beginning);
                statement.setLong(2, Long.parseLong(accChartKeyString));
                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                return accChartKeyString;
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

    @Override
    public ArrayList<SJournalHeader> getJournalListWithJT(String sSID, String sID, String fisKeyString,
            String journalTypeKeyString, int month, int year) throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                String sql = "SELECT * FROM doc_type LEFT JOIN journal_header ON doc_type.id = journal_header.doc_type_id LEFT JOIN journal_item ON journal_header.id = journal_item.journal_header_id WHERE doc_type.fiscal_year_id = ? && doc_type.journal_type_id = ? && journal_header.month = ? && journal_header.year = ? ORDER BY journal_header.day ASC, journal_header.no ASC";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setLong(1, Long.parseLong(fisKeyString));
                statement.setLong(2, Long.parseLong(journalTypeKeyString));
                statement.setInt(3, month);
                statement.setInt(4, year);
                ResultSet rs = statement.executeQuery();

                return populateJournalList(rs);
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

    private ArrayList<SJournalHeader> populateJournalList(ResultSet rs) throws SQLException {
        ArrayList<SJournalHeader> sJournalList = new ArrayList<SJournalHeader>();

        SJournalHeader sJournal = null;
        long id;
        while (rs.next()) {
            id = rs.getLong(Db.dot(Db.JOURNAL_HEADER, Db.ID));
            if (sJournal == null || !sJournal.getKeyString().equals(id + "")) {
                sJournal = new SJournalHeader(
                        id + "",
                        rs.getLong(Db.dot(Db.JOURNAL_HEADER, Db.DOC_TYPE_ID)) + "",
                        rs.getString(Db.dot(Db.JOURNAL_HEADER, Db.NO)),
                        rs.getInt(Db.dot(Db.JOURNAL_HEADER, Db.DAY)),
                        rs.getInt(Db.dot(Db.JOURNAL_HEADER, Db.MONTH)),
                        rs.getInt(Db.dot(Db.JOURNAL_HEADER, Db.YEAR)),
                        rs.getString(Db.dot(Db.JOURNAL_HEADER, Db.DESC)));

                sJournalList.add(sJournal);
            }

            id = rs.getLong(Db.dot(Db.JOURNAL_ITEM, Db.ID));
            if (id != 0) {
                SJournalItem sJournalItem = new SJournalItem(
                        id + "",
                        rs.getLong(Db.dot(Db.JOURNAL_ITEM, Db.ACC_CHART_ID)) + "",
                        rs.getDouble(Db.dot(Db.JOURNAL_ITEM, Db.AMT)),
                        new Date(rs.getTimestamp(Db.dot(Db.JOURNAL_ITEM, Db.CREATE_DATE)).getTime()));

                sJournal.addItem(sJournalItem);
            }
        }
        return sJournalList;
    }

    @Override
    public String getJournalBodyHtml(String sSID, String sID, String fisKeyString,
            String journalTypeKeyString, int[] dates, String totalConstant,
            String wholeTotalConstant) throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {

                // TODO: Performance?
                String sql = "SELECT * FROM doc_type LEFT JOIN journal_header ON doc_type.id = journal_header.doc_type_id LEFT JOIN journal_item ON journal_header.id = journal_item.journal_header_id LEFT JOIN acc_chart ON journal_item.acc_chart_id = acc_chart.id WHERE doc_type.fiscal_year_id = ? && doc_type.journal_type_id = ? && journal_header.year >= ? && journal_header.year <= ? && (journal_header.year > ? || journal_header.month >= ?) && (journal_header.year < ? || journal_header.month <= ?) && (journal_header.year != ? || journal_header.month != ? || journal_header.day >= ?) && (journal_header.year != ? || journal_header.month != ? || journal_header.day <= ?) ORDER BY journal_header.day ASC, journal_header.no ASC";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setLong(1, Long.parseLong(fisKeyString));
                statement.setLong(2, Long.parseLong(journalTypeKeyString));
                statement.setInt(3, dates[2]);
                statement.setInt(4, dates[5]);
                statement.setInt(5, dates[2]);
                statement.setInt(6, dates[1]);
                statement.setInt(7, dates[5]);
                statement.setInt(8, dates[4]);
                statement.setInt(9, dates[2]);
                statement.setInt(10, dates[1]);
                statement.setInt(11, dates[0]);
                statement.setInt(12, dates[5]);
                statement.setInt(13, dates[4]);
                statement.setInt(14, dates[3]);
                ResultSet rs = statement.executeQuery();

                return genJournalBodyHtml(rs, totalConstant, wholeTotalConstant);
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

    private String genJournalBodyHtml(ResultSet rs, String totalConstant, String wholeTotalConstant)
            throws SQLException {

        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");

        long jId = 0;

        double totalDebit = 0.0;
        double totalCredit = 0.0;
        double wholeTotalDebit = 0.0;
        double wholeTotalCredit = 0.0;

        while (rs.next()) {

            long id = rs.getLong(Db.dot(Db.JOURNAL_HEADER, Db.ID));
            if ( jId == 0 || jId != id) {

                if (jId != 0) {
                    genJournalTotalHtml(sb, df, totalConstant, STYLE_NAME_AULINE, totalDebit,
                            totalCredit);

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
        genJournalTotalHtml(sb, df, totalConstant, STYLE_NAME_AULINE, totalDebit, totalCredit);

        wholeTotalDebit += totalDebit;
        wholeTotalCredit += totalCredit;

        // Whole total
        genJournalTotalHtml(sb, df, wholeTotalConstant, STYLE_NAME_DULINE, wholeTotalDebit,
                wholeTotalCredit);

        return sb.toString();
    }

    private void genJournalBeginningHtml(StringBuilder sb, int day, int month, int year,
            String no) {

        sb.append("<tr>");
        sb.append("<td colspan=5>");
        sb.append(day + "/" + month + "/" + year + "&nbsp;&nbsp;&nbsp;&nbsp;" + no);
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
        sb.append(amt >= 0 ? df.format(Math.abs(amt)) : "&nbsp;");
        sb.append("</td>");

        sb.append("<td>");
        sb.append(amt >= 0 ? "&nbsp;" : df.format(Math.abs(amt)));
        sb.append("</td>");

        sb.append("</tr>");
    }

    private void genJournalTotalHtml(StringBuilder sb, DecimalFormat df, String totalConstant,
            String printStyle, double totalDebit, double totalCredit) {
        sb.append("<tr>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>" + totalConstant + "</td>");
        sb.append("<td class='" + printStyle +"'>");
        sb.append(df.format(totalDebit));
        sb.append("</td>");
        sb.append("<td class='" + printStyle + "'>");
        sb.append(df.format(Math.abs(totalCredit)));
        sb.append("</td>");
        sb.append("</tr>");

        sb.append("<tr>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("</tr>");
    }

    @Override
    public String getLedgerBodyHtml(String sSID,
            String sID, String fisKeyString, String beginACNo, String endACNo, int[] dates,
            boolean doShowAll, String totalConstant, String wholeTotalConstant)
            throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {

                // TODO: Performance?
                String sql = "SELECT * FROM acc_chart LEFT JOIN journal_item ON acc_chart.id = journal_item.acc_chart_id LEFT JOIN journal_header USE INDEX (year_index) ON journal_item.journal_header_id = journal_header.id LEFT JOIN doc_type ON journal_header.doc_type_id = doc_type.id LEFT JOIN journal_type ON doc_type.journal_type_id = journal_type.id WHERE acc_chart.fiscal_year_id = ? && acc_chart.type = ? && acc_chart.no BETWEEN ? AND ? && journal_header.year >= ? && journal_header.year <= ? && (journal_header.year > ? || journal_header.month >= ?) && (journal_header.year < ? || journal_header.month <= ?) && (journal_header.year != ? || journal_header.month != ? || journal_header.day >= ?) && (journal_header.year != ? || journal_header.month != ? || journal_header.day <= ?) ORDER BY acc_chart.no ASC, journal_header.year ASC, journal_header.month ASC, journal_header.day ASC, journal_header.no ASC";

                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setLong(1, Long.parseLong(fisKeyString));
                statement.setInt(2, AccType.ENTRY.ordinal());
                statement.setString(3, beginACNo);
                statement.setString(4, endACNo);
                statement.setInt(5, dates[2]);
                statement.setInt(6, dates[5]);
                statement.setInt(7, dates[2]);
                statement.setInt(8, dates[1]);
                statement.setInt(9, dates[5]);
                statement.setInt(10, dates[4]);
                statement.setInt(11, dates[2]);
                statement.setInt(12, dates[1]);
                statement.setInt(13, dates[0]);
                statement.setInt(14, dates[5]);
                statement.setInt(15, dates[4]);
                statement.setInt(16, dates[3]);
                ResultSet rs = statement.executeQuery();

                return genLedgerBodyHtml(rs, doShowAll, totalConstant, wholeTotalConstant);
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

    public static final String STYLE_NAME_RIGHT = "right";
    public static final String STYLE_NAME_DULINE = "duline";
    public static final String STYLE_NAME_AULINE = "auline";

    private String genLedgerBodyHtml(ResultSet rs, boolean doShowAll, String totalConstant,
            String wholeTotalConstant) throws SQLException {

        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");

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
                    genLedgerTotalHtml(sb, df, totalConstant, STYLE_NAME_AULINE, totalDebit,
                            totalCredit);

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
            genLedgerTotalHtml(sb, df, totalConstant, STYLE_NAME_AULINE, totalDebit, totalCredit);

            wholeTotalDebit += totalDebit;
            wholeTotalCredit += totalCredit;
        }

        // Whole total
        genLedgerTotalHtml(sb, df, wholeTotalConstant, STYLE_NAME_DULINE, wholeTotalDebit,
                wholeTotalCredit);

        return sb.toString();
    }

    private void genLedgerBeginningHtml(StringBuilder sb, DecimalFormat df, String acNo,
            String acName, double beginning) {

        sb.append("<tr>");
        sb.append("<td colspan=6>");
        sb.append(acNo + "&nbsp;&nbsp;&nbsp;&nbsp;" + acName);
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
        sb.append(amt >= 0 ? df.format(Math.abs(amt)) : "&nbsp;");
        sb.append("</td>");

        sb.append("<td>");
        sb.append(amt >= 0 ? "&nbsp;" : df.format(Math.abs(amt)));
        sb.append("</td>");

        sb.append("<td>");
        sb.append(df.format(remaining));
        sb.append("</td>");
        sb.append("</tr>");
    }

    private void genLedgerTotalHtml(StringBuilder sb, DecimalFormat df, String totalConstant,
            String printStyle, double totalDebit, double totalCredit) {

        sb.append("<tr>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>" + totalConstant + "</td>");
        sb.append("<td class='" + printStyle +"'>");
        sb.append(df.format(totalDebit));
        sb.append("</td>");
        sb.append("<td class='" + printStyle + "'>");
        sb.append(df.format(Math.abs(totalCredit)));
        sb.append("</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("</tr>");

        sb.append("<tr>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("<td>&nbsp;</td>");
        sb.append("</tr>");
    }

    @Override
    public String addJournal(String sSID, String sID, String fisKeyString, SJournalHeader sJournal)
            throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {

                // Transaction
                conn.setAutoCommit(false);

                long journalId = Db.addJournal(conn, Long.parseLong(fisKeyString), sJournal);

                // end transaction
                conn.commit();

                return journalId + "";
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

    @Override
    public String editJournal(String sSID, String sID, String fisKeyString, SJournalHeader sJournal)
            throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                // Transaction
                conn.setAutoCommit(false);

                long headerId = Long.parseLong(sJournal.getKeyString());

                // Edit header
                String updateSql = "UPDATE journal_header SET doc_type_id = ?, `no` = ?, `day` = ?, `month` = ?, `year` = ?, `desc` = ? WHERE id = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateSql);
                updateStatement.setLong(1, Long.parseLong(sJournal.getDocTypeKeyString()));
                updateStatement.setString(2, sJournal.getNo());
                updateStatement.setInt(3, sJournal.getDay());
                updateStatement.setInt(4, sJournal.getMonth());
                updateStatement.setInt(5, sJournal.getYear());
                updateStatement.setString(6, sJournal.getDesc());
                updateStatement.setLong(7, headerId);
                int affectedRows = updateStatement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                long fisId = Long.parseLong(fisKeyString);

                // deduct amt from old items
                deductAccAmt(conn, fisId, sJournal.getKeyString());

                // delete old items
                String deleteSql = "DELETE FROM journal_item WHERE journal_header_id = ?";
                PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
                deleteStatement.setLong(1, headerId);
                affectedRows = deleteStatement.executeUpdate();
                if (affectedRows <= 0) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                // add new items and plus amt from new items to acc_amt
                Db.addJournalItems(conn, fisId, headerId, sJournal.getItemList());

                // end transaction
                conn.commit();

                return sJournal.getKeyString();
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

    @Override
    public String deleteJournal(String sSID, String sID, String fisKeyString, String keyString)
            throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                // Transaction
                conn.setAutoCommit(false);

                deductAccAmt(conn, Long.parseLong(fisKeyString), keyString);

                // delete items and header
                //     (Checking that it can be deleted was delegated to MySQL Foreign key)
                Db.deleteFromDB(conn, Db.JOURNAL_HEADER, keyString);

                // end transaction
                conn.commit();

                return keyString;
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

    private void deductAccAmt(Connection conn, long fisId, String journalHeaderKeyString)
            throws SQLException {

        // Get items
        String getItemSql = "SELECT * FROM journal_item WHERE journal_item.journal_header_id = ?";
        PreparedStatement getItemStatement = conn.prepareStatement(getItemSql);
        getItemStatement.setLong(1, Long.parseLong(journalHeaderKeyString));

        // deduct amt from acc_amt
        String deductSql = "UPDATE acc_amt SET amt = amt - ? WHERE fiscal_year_id = ? && acc_chart_id = ?";
        PreparedStatement deductStatement = conn.prepareStatement(deductSql);

        ResultSet getItemRs = getItemStatement.executeQuery();
        while (getItemRs.next()) {
            deductStatement.setDouble(1, getItemRs.getDouble(Db.AMT));
            deductStatement.setLong(2, fisId);
            deductStatement.setLong(3, getItemRs.getLong(Db.ACC_CHART_ID));
            int affectedRows = deductStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
            }
        }
    }

    @Override
    public HashMap<String, SAccAmt> getAccAmtMap(String sSID, String sID, String fisKeyString)
            throws NotLoggedInException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {
                HashMap<String, SAccAmt> sAccAmtMap = new HashMap<String, SAccAmt>();

                String sql = "SELECT * FROM acc_amt WHERE fiscal_year_id = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setLong(1, Long.parseLong(fisKeyString));
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    String accChartKeyString = rs.getLong(Db.ACC_CHART_ID) + "";
                    SAccAmt sAccAmt = new SAccAmt(
                            accChartKeyString,
                            rs.getDouble(Db.AMT),
                            new Date(rs.getTimestamp(Db.CREATE_DATE).getTime()));
                    sAccAmtMap.put(accChartKeyString, sAccAmt);
                }

                return sAccAmtMap;
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

    @Override
    public String recalculateAccAmt(String sSID, String sID, String fisKeyString)
            throws NotLoggedInException {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try{
            UserManager.checkLoggedInAndGetUser(sSID, sID);
        } finally{
            pm.close();
        }

        try {
            Connection conn = Db.getDBConn();
            try {

                long fisId = Long.parseLong(fisKeyString);

                // clear acc amts
                String clearSql = "DELETE FROM acc_amt WHERE fiscal_year_id = ?";
                PreparedStatement clearStatement = conn.prepareStatement(clearSql);
                clearStatement.setLong(1, fisId);
                clearStatement.executeUpdate();

                // Get begin end month year
                String fiscalSql = "SELECT * FROM fiscal_year where id = ?";
                PreparedStatement fiscalStatement = conn.prepareStatement(fiscalSql);
                fiscalStatement.setLong(1, fisId);
                ResultSet rs = fiscalStatement.executeQuery();
                if (!rs.first()) {
                    throw new SQLException(Db.NO_ROW_EFFECTED_ERR);
                }

                int[] beginDate = {0, rs.getInt(Db.BEGIN_MONTH), rs.getInt(Db.BEGIN_YEAR)};
                int[] endDate = {0, rs.getInt(Db.END_MONTH), rs.getInt(Db.END_YEAR)};

                ArrayList<int[]> months = Utils.findAllMonths(beginDate, endDate);

                String journalSql = "SELECT * FROM journal_item LEFT JOIN journal_header ON journal_item.journal_header_id = journal_header.id WHERE journal_header.fiscal_year_id = ? && journal_header.month = ? && journal_header.year = ?";
                PreparedStatement journalStatement = conn.prepareStatement(journalSql);
                journalStatement.setLong(1, Long.parseLong(fisKeyString));

                HashMap<Long, Double> accAmtMap = new HashMap<Long, Double>();

                String plusSql = "INSERT INTO acc_amt (fiscal_year_id, acc_chart_id, amt) VALUES ( ?, ?, ?) ON DUPLICATE KEY UPDATE amt = amt + ?";;
                PreparedStatement plusStatement = conn.prepareStatement(plusSql);
                plusStatement.setLong(1, fisId);

                // loop for each month
                for (int[] month : months) {

                    // Make sure to clear it first
                    accAmtMap.clear();

                    // get journal
                    journalStatement.setInt(2, month[1]);
                    journalStatement.setInt(3, month[2]);
                    rs = journalStatement.executeQuery();

                    // sum up
                    while (rs.next()) {
                        long acId = rs.getLong(Db.dot(Db.JOURNAL_ITEM, Db.ACC_CHART_ID));
                        double amt = rs.getDouble(Db.dot(Db.JOURNAL_ITEM, Db.AMT));
                        if (accAmtMap.containsKey(acId)) {
                            accAmtMap.put(acId, accAmtMap.get(acId) + amt);
                        } else {
                            accAmtMap.put(acId, amt);
                        }
                    }

                    // update(not replace) to acc amts as batch update
                    for (Map.Entry<Long, Double> entry : accAmtMap.entrySet()) {
                        plusStatement.setLong(2, entry.getKey());
                        plusStatement.setDouble(3, entry.getValue());
                        plusStatement.setDouble(4, entry.getValue());
                    }
                }

                return "succeeded";
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
}
