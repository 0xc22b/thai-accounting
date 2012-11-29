<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Forgot password?</title>    
    <link type="text/css" rel="stylesheet" media="all" href="/css/stylesheet-alpha.css" />
</head>
<body>
	<noscript>
        <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
            Your web browser must have JavaScript enabled
            in order for this application to display correctly.
        </div>
    </noscript>

    <label id="resultLb" style="display: none;"></label>
    <div id="formPanel">
        <% if (request.getParameter("fid") == null) { %>
		    <h2>Please enter your username or email.</h2>
		    <label id="usernameOrEmailErrLb" class="errLb">&nbsp;</label>
		    <input type="text" placeholder="username or email" id="usernameOrEmailTB" />
	    <% } else { %>
	        <h2>Please enter your new password.</h2>
		    <label id="newPasswordErrLb" class="errLb">&nbsp;</label>
		    <input type="password" placeholder="new password" id="newPasswordTB" />
		    <label id="repeatPasswordErrLb" class="errLb">&nbsp;</label>
		    <input type="password" placeholder="password again" id="repeatPasswordTB" />
	    <% } %>
	    <button id="okBtn">OK</button>
	    <img src="/img/loading.gif" id="loadingImg" style="display: none;" />
    </div>

    <div id="footer">
        Thai accounting 2012. All rights Reserved
    </div>
    
    <!-- JavaScript at the bottom for fast page loading -->
    
    <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="js/libs/jquery-1.7.1.min.js"><\/script>')</script>
    
    <!-- scripts concatenated and minified via build script -->
    <script src="js/userverifier-alpha.js"></script>
    <script src="js/forgot-alpha.js"></script>
    <script>
        $(document).ready(function () {
            JS_FORGOT.init();    
        });
    </script>
    <!-- end scripts -->
    
</body>
</html>
