<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Account settings</title>    
    <link type="text/css" rel="stylesheet" media="all" href="/css/stylesheet-alpha.css" />
</head>
<body>
	<noscript>
        <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
            Your web browser must have JavaScript enabled
            in order for this application to display correctly.
        </div>
    </noscript>
    
    <a href="/">Back</a>
    
    <h1>Account settings</h1>
    <div id="changeUsernamePanel" class="panel">
        <div class="lb">Username:</div>
        <div id="changeUsernameLb" class="value">${username}</div>
        <button id="changeUsernameBtn">change</button>
        <div id="changeUsernameEditor" style="display: none;" class="editor">
            <label id="changeUsernameErrLb" class="errLb">&nbsp;</label>
            <input type="text" placeholder="New username" id="changeUsernameTB" />
            <label id="changeUsernamePasswordErrLb" class="errLb">&nbsp;</label>
            <input type="password" placeholder="password" id="changeUsernamePasswordTB" />
            <button id="changeUsernameOkBtn">OK</button>
            <img src="/img/loading.gif" id="changeUsernameLoadingImg" style="display: none;" />
        </div>
    </div>
    <div id="changeEmailPanel" class="panel">
        <div class="lb">Email:</div>
        <span id="changeEmailLb" class="value">${email}</span>
	    <button id="changeEmailBtn">change</button>
	    <% if (((Boolean)request.getAttribute("didEmailConfirm")).booleanValue() == true) { %>
	       <label>Email verified.</label>
	    <% } else { %>
	       <button id="resendEmailConfirmBtn">Resend email verification</button>
	    <% } %>
        <div id="changeEmailEditor" style="display: none;" class="editor">
            <label id="changeEmailErrLb" class="errLb">&nbsp;</label>
            <input type="text" placeholder="New email" id="changeEmailTB" />
            <label id="changeEmailPasswordErrLb" class="errLb">&nbsp;</label>
            <input type="password" placeholder="password" id="changeEmailPasswordTB" />
            <button id="changeEmailOkBtn">OK</button>
            <img src="/img/loading.gif" id="changeEmailLoadingImg" style="display: none;" />
        </div>
    </div>    
	<div id="changePasswordPanel" class="panel">
	    <div class="lb">Password:</div>
	    <button id="changePasswordBtn">Change password</button>
        <div id="changePasswordEditor" style="display: none;" class="editor">
            <label id="changeNewPasswordErrLb" class="errLb">&nbsp;</label>
            <input type="password" placeholder="New password" id="changeNewPasswordTB" />
            <label id="changeRepeatPasswordErrLb" class="errLb">&nbsp;</label>
            <input type="password" placeholder="Repeat New password" id="changeRepeatPasswordTB" />
            <label id="changePasswordErrLb" class="errLb">&nbsp;</label>
            <input type="password" placeholder="Current password" id="changePasswordTB" />
            <button id="changePasswordOkBtn">OK</button>
            <img src="/img/loading.gif" id="changePasswordLoadingImg" style="display: none;" />
        </div>
	</div>
	<div id="changeLangPanel" class="panel">
	    <div class="lb">Language:</div>
	    <div id="changeLangLb" class="value">${lang}</div>
	    <button id="changeLangBtn">change</button>
        <div id="changeLangEditor" style="display: none;" class="editor">
            <label id="changeLangErrLb" class="errLb">&nbsp;</label>
            <select id="changeLangS">
                <% String lang = (String) request.getAttribute("lang"); %>
                <option value="en" <% if (lang.equals("en")) { %>selected<% } %>>en</option>
                <option value="th" <% if (lang.equals("th")) { %>selected<% } %>>th</option>
            </select>
            <button id="changeLangOkBtn">OK</button>
            <img src="/img/loading.gif" id="changeLangLoadingImg" style="display: none;" />
        </div>
	</div>
	<div id="deleteAccountPanel" class="panel">
	    <div class="lb">Delete account:</div>
	    <button id="deleteAccountBtn">Delete account</button>
        <div id="deleteAccountEditor" style="display: none;" class="editor">
        
            <!-- 
                Delete Account

                This page allows you to completely delete your account. 
                This cannot be reversed so please be sure that you really want
                to do this.
             -->

            <label id="deleteAccountPasswordErrLb" class="errLb">&nbsp;</label>
            <input type="password" placeholder="password" id="deleteAccountPasswordTB" />
            <button id="deleteAccountOkBtn">OK</button>
            <img src="/img/loading.gif" id="deleteAccountLoadingImg" style="display: none;" />
        </div>
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
    <script src="js/user-alpha.js"></script>
    <script>
        $(document).ready(function () {
            JS_USER.init();    
        });
    </script>
    <!-- end scripts -->
    
</body>
</html>
