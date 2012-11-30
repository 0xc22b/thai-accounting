package gwt.server;

import gwt.server.user.UserManager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmailConfirmServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        boolean didEmailConfirm = false;
        String sessionKeyString = req.getParameter(UserManager.PARAMETER_SSID);
        String sessionID = req.getParameter(UserManager.PARAMETER_VID);
        if (sessionKeyString != null && sessionID != null) {
            didEmailConfirm = UserManager.confirmEmail(sessionKeyString, sessionID);
        }
        
        req.setAttribute("didEmailConfirm", didEmailConfirm);
        getServletConfig().getServletContext().getRequestDispatcher(
                "/emailconfirm.jsp").forward(req, resp);
    }
}


