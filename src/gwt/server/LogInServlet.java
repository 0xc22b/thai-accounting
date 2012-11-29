package gwt.server;

import gwt.server.user.UserManager;
import gwt.server.user.UserVerifier;
import gwt.server.user.model.Session;
import gwt.server.user.model.User;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogInServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        String username = req.getParameter(UserManager.PARAMETER_USERNAME);
        String password = req.getParameter(UserManager.PARAMETER_PASSWORD);
        UserVerifier.Log log = new UserVerifier.Log();
        
        User user = UserManager.checkUsernameOrEmailAndPassword(username, password, log);
        if (user != null) {
            Session session = UserManager.addLoginSession(user.getKey());

            resp.addCookie(HomeServlet.createSSIDCookie(session.getKeyString()));
            resp.addCookie(HomeServlet.createSIDCookie(session.getSessionID()));
        }
        
        HomeServlet.response(resp, log);
        return;
    }
}


