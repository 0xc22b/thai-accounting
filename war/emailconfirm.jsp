<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Email verification</title>    
    <link type="text/css" rel="stylesheet" media="all" href="/css/stylesheet-alpha.css" />
</head>
<body>
	<noscript>
        <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
            Your web browser must have JavaScript enabled
            in order for this application to display correctly.
        </div>
    </noscript>

    <h2>Email verifiaction</h2>
    <% if (((Boolean)request.getAttribute("didEmailConfirm")).booleanValue() == true) { %>
        <p>Your email has been verified.</p><a href="/">Continue</a>
    <% } else { %>
        <p>We could not verify your email. Please try again.</p>
    <% } %>

    <div id="footer">
        Thai accounting 2012. All rights Reserved
    </div>
</body>
</html>
