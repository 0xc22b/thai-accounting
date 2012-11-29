package gwt.server;

import gwt.server.org.json.JSONException;
import gwt.server.user.UserManager;
import gwt.server.user.UserVerifier;
import gwt.server.user.model.User;
import gwt.server.user.model.UserData;
import gwt.shared.NotLoggedInException;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private static final String SSID_COOKIE = "SSID";
    private static final String SID_COOKIE = "SID";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        //Prevent XSRF - not allow in iframe
        resp.addHeader("X-Frame-Options", "DENY");
        
        Cookie sSIDCookie = getSSIDCookie(req);
        Cookie sIDCookie = getSIDCookie(req);
        if (sSIDCookie != null && sIDCookie != null){
            try {
                User user = UserManager.checkLoggedInAndGetUser(sSIDCookie.getValue(),
                        sIDCookie.getValue());
                
                UserData userData = UserManager.getUserData(user.getKey());
                req.setAttribute(UserData.LANG, userData.getLang());
                
                getServletConfig().getServletContext().getRequestDispatcher("/acc.jsp").forward(req, resp);
                return;
            } catch (NotLoggedInException e) {

            }
        }
        
        getServletConfig().getServletContext().getRequestDispatcher("/home.jsp").forward(req,resp);
    }
        
    protected static Cookie getSSIDCookie(HttpServletRequest req) {
        return getCookie(req, SSID_COOKIE);
    }
    
    protected static Cookie getSIDCookie(HttpServletRequest req) {
        return getCookie(req, SID_COOKIE);
    }
    
    protected static Cookie createSSIDCookie(String cookieValue) {
        return createCookie(SSID_COOKIE, cookieValue, 20, "/");
    }
    
    protected static Cookie createSIDCookie(String cookieValue) {
        return createCookie(SID_COOKIE, cookieValue, 20, "/");
    }
    
    protected static void response(HttpServletResponse resp, UserVerifier.Log log)
            throws IOException{
        // Prevent XSRF - not allow in iframe
        resp.addHeader("X-Frame-Options", "DENY");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            out.print(log.getJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }
    
    private static Cookie getCookie(HttpServletRequest req, String cookieName){
        Cookie[] cookies = req.getCookies();
        if (cookies != null){
            for(int i = 0; i < cookies.length; i++){
                Cookie cookie = cookies[i]; 
                if(cookie.getName().equals(cookieName)){
                    return cookie;
                }
            }
        }
        return null;
    }
    
    private static Cookie createCookie(String cookieName, String cookieValue,
            int days, String path){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath(path);
        cookie.setMaxAge(days * 24 * 60 * 60);
        return cookie;
    }
}


