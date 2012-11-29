package gwt.server;

import gwt.server.user.UserManager;
import gwt.server.user.UserVerifier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForgotServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getServletConfig().getServletContext().getRequestDispatcher(
                "/forgot.jsp").forward(req, resp);
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        // enter username or email to receive an email to reset password.
        String usernameOrEmail = req.getParameter(UserManager.PARAMETER_USERNAME);
        if (usernameOrEmail != null) {
            UserVerifier.Log log = new UserVerifier.Log();
            UserManager.forgotPassword(usernameOrEmail, log);
            HomeServlet.response(resp, log);
            return;
        }
        
        // enter new password to reset password.
        String sessionKeyString = req.getParameter(UserManager.PARAMETER_SSID);
        String sessionID = req.getParameter(UserManager.PARAMETER_FID);
        String newPassword = req.getParameter(UserManager.PARAMETER_NEW_PASSWORD);
        String repeatPassword = req.getParameter(UserManager.PARAMETER_REPEAT_PASSWORD);
        if (sessionKeyString != null && sessionID != null && newPassword != null
                && repeatPassword != null) {
            UserVerifier.Log log = new UserVerifier.Log();
            UserManager.resetPassword(sessionKeyString, sessionID, newPassword,
                    repeatPassword, log);
            HomeServlet.response(resp, log);
            return;
        }
        
        //TODO: throw error!!
        resp.sendError(404);
    }
}


