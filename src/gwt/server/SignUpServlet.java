package gwt.server;

import gwt.server.user.UserManager;
import gwt.server.user.UserVerifier;
import gwt.server.user.model.Session;
import gwt.server.user.model.User;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignUpServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        UserVerifier.Log log = new UserVerifier.Log();
        
        String username = req.getParameter(UserManager.PARAMETER_USERNAME);
        if (username != null) {
            
            username = username.trim();
            
            UserVerifier.isUsernameValid(username, true, log);
        }
        
        String email = req.getParameter(UserManager.PARAMETER_EMAIL);
        if (email != null) {
            
            email = email.trim();
            
            // Email always be lowercase.
            email = email.toLowerCase();
            
            UserVerifier.isEmailValid(email, true, log);
        }
        
        HomeServlet.response(resp, log);
        return;
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String username = req.getParameter(UserManager.PARAMETER_USERNAME);
        String email = req.getParameter(UserManager.PARAMETER_EMAIL);
        String password = req.getParameter(UserManager.PARAMETER_PASSWORD);
        String repeatPassword = req.getParameter(UserManager.PARAMETER_REPEAT_PASSWORD);
        UserVerifier.Log log = new UserVerifier.Log();
        
        User user = UserManager.signUp(username, email, password,
                repeatPassword, log);
        if (user != null) {
            Session session = UserManager.addLoginSession(user.getKey());
            
            resp.addCookie(HomeServlet.createSSIDCookie(session.getKeyString()));
            resp.addCookie(HomeServlet.createSIDCookie(session.getSessionID()));
        }
        
        HomeServlet.response(resp, log);
        return;
    }
}


