<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%
   String lang = "en_US";
   try {
       Class.forName("com.mysql.jdbc.Driver");
       String url = "jdbc:mysql://localhost:3306/thai_accounting?user=root&password=root";

       Connection conn =  DriverManager.getConnection(url);
       try {
           String sql = "SELECT * FROM user WHERE id = 1";
           PreparedStatement statement = conn.prepareStatement(sql);
           ResultSet rs = statement.executeQuery();
           if (rs.first()) {
               lang = rs.getString("lang");
           }
       } finally {
           conn.close();
       }
   } catch (ClassNotFoundException e) {
       e.printStackTrace();
   } catch (SQLException e) {
       e.printStackTrace();
   }
%>

<!doctype html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">

    <meta name="gwt:property" content="locale=<%= lang %>">

    <title>Thai SME accounting</title>        
</head>
<body>

    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    <iframe id="__printingFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>
    
    <script src="thaiaccounting/thaiaccounting.nocache.js"></script>
    
</body>
</html>
