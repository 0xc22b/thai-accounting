package gwt.server;

import gwt.server.user.UserManager;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogOutServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        Cookie sSIDCookie = HomeServlet.getSSIDCookie(req);
        Cookie sIDCookie = HomeServlet.getSIDCookie(req);
        if (sSIDCookie != null && sIDCookie != null){
            UserManager.deleteLoginSession(sSIDCookie.getValue(), sIDCookie.getValue());

            sSIDCookie.setMaxAge(0);
            resp.addCookie(sSIDCookie);
            
            sIDCookie.setMaxAge(0);
            resp.addCookie(sIDCookie);
        }
        
        resp.sendRedirect("/");
    }
}


