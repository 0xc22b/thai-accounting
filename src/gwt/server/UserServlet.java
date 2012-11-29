package gwt.server;

import gwt.server.user.UserManager;
import gwt.server.user.UserVerifier;
import gwt.server.user.model.User;
import gwt.server.user.model.UserData;
import gwt.shared.NotLoggedInException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //Prevent XSRF - not allow in iframe
        resp.addHeader("X-Frame-Options", "DENY");
        
        Cookie sSIDCookie = HomeServlet.getSSIDCookie(req);
        Cookie sIDCookie = HomeServlet.getSIDCookie(req);
        if (sSIDCookie != null && sIDCookie != null){
            try {
                User user = UserManager.checkLoggedInAndGetUser(
                        sSIDCookie.getValue(), sIDCookie.getValue());
                req.setAttribute(User.USER_NAME, user.getUsername());
                req.setAttribute(User.EMAIL, user.getEmail());
                
                UserData userData = UserManager.getUserData(user.getKey());
                req.setAttribute(UserData.DID_EMAIL_CONFIRM, userData.didEmailComfirm());
                req.setAttribute(UserData.LANG, userData.getLang());

                getServletConfig().getServletContext().getRequestDispatcher("/user.jsp").forward(req, resp);
                return;
            } catch (NotLoggedInException e1) {
                // Not logged in, redirect to home page (below).
            }
        }
        
        // No cookie, redirect to home page.
        resp.sendRedirect("/");
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // Check logging in from sessionID in request parameter.
        String sessionKeyString = req.getParameter(UserManager.PARAMETER_SSID);
        String sessionID = req.getParameter(UserManager.PARAMETER_SID);
        User user = null;
        try {
            user = UserManager.checkLoggedInAndGetUser(sessionKeyString, sessionID);
        } catch (NotLoggedInException e) {
            UserVerifier.Log log = new UserVerifier.Log();
            log.addLogInfo(UserVerifier.LogInfoType.DID_LOG_IN, null, false,
                    null);
            HomeServlet.response(resp, log);
            return;
        }
        
        String methodName = req.getParameter("method");
        
        @SuppressWarnings("rawtypes")
        Class[] argClasses = {HttpServletRequest.class, HttpServletResponse.class,
            User.class, String.class};
        Object[] argObjects = {req, resp, user, sessionKeyString};
        try {
            Method method = getClass().getMethod(methodName, argClasses);
            method.invoke(this, argObjects);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public void changeUsername(HttpServletRequest req,
            HttpServletResponse resp, User user, String sessionKeyString) throws IOException {
        
        String username = req.getParameter(UserManager.PARAMETER_USERNAME);
        String password = req.getParameter(UserManager.PARAMETER_PASSWORD);
        
        UserVerifier.Log log = new UserVerifier.Log();
        
        UserManager.changeUsername(user, username, password, log);
        
        HomeServlet.response(resp, log);
    }
    
    public void changeEmail(HttpServletRequest req,
            HttpServletResponse resp, User user, String sessionKeyString) throws IOException {
        String email = req.getParameter(UserManager.PARAMETER_EMAIL);
        String password = req.getParameter(UserManager.PARAMETER_PASSWORD);
        
        UserVerifier.Log log = new UserVerifier.Log();
        
        UserManager.changeEmail(user, email, password, log);
        
        HomeServlet.response(resp, log);
    }
    
    public void changePassword(HttpServletRequest req,
            HttpServletResponse resp, User user, String sessionKeyString) throws IOException {
        String newPassword = req.getParameter(UserManager.PARAMETER_NEW_PASSWORD);
        String repeatPassword = req.getParameter(UserManager.PARAMETER_REPEAT_PASSWORD);
        String password = req.getParameter(UserManager.PARAMETER_PASSWORD);
        
        UserVerifier.Log log = new UserVerifier.Log();
        
        UserManager.changePassword(user, sessionKeyString, newPassword, repeatPassword,
                password, log);
        
        HomeServlet.response(resp, log);
    }
    
    public void changeLang(HttpServletRequest req, HttpServletResponse resp,
            User user, String sessionKeyString) throws IOException {
        
        String lang = req.getParameter(UserManager.PARAMETER_LANG);
        
        UserVerifier.Log log = new UserVerifier.Log();
        
        UserManager.changeLang(user, lang, log);
        
        HomeServlet.response(resp, log);
    }
    
    public void deleteAccount(HttpServletRequest req,
            HttpServletResponse resp, User user, String sessionKeyString) throws IOException {

        String password = req.getParameter(UserManager.PARAMETER_PASSWORD);
        
        UserVerifier.Log log = new UserVerifier.Log();
        
        UserManager.deleteAccount(user, password, log);
        
        if (log.isValid()) {
            //TODO: delete user's business data
            
            
            // Delete user's cookies
            Cookie sSIDCookie = HomeServlet.getSSIDCookie(req);
            sSIDCookie.setMaxAge(0);
            resp.addCookie(sSIDCookie);
            
            Cookie sIDCookie = HomeServlet.getSIDCookie(req);
            sIDCookie.setMaxAge(0);
            resp.addCookie(sIDCookie);
        }
        
        HomeServlet.response(resp, log);
    }
    
    public void resendEmailConfirm(HttpServletRequest req,
            HttpServletResponse resp, User user, String sessionKeyString)
            throws IOException {
        
        UserVerifier.Log log = new UserVerifier.Log();
        
        UserManager.sendEmailConfirm(user, log);
        
        HomeServlet.response(resp, log);
    }
}


