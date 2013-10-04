package gwt.server;

import gwt.server.account.Db;
import gwt.shared.SConstants;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {

        String lang = req.getParameter(SConstants.LANG);
        if (lang != null) {
            try {
                Connection conn = Db.getDBConn();
                try {
                    Db.updateLang(conn, lang);
                } finally {
                    conn.close();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            }
        }

        resp.sendRedirect("/");
    }
}
