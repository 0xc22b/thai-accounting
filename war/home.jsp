<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Thai-Accounting</title>
    <link type="text/css" rel="stylesheet" media="all" href="/css/stylesheet-alpha.css" />
</head>
<body>
	<noscript>
        <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
            Your web browser must have JavaScript enabled
            in order for this application to display correctly.
        </div>
    </noscript>
    
    <a href="https://github.com/witnapatat/thai-accounting">
        <img style="position: absolute; top: 0; right: 0; border: 0;"
                src="https://s3.amazonaws.com/github/ribbons/forkme_right_gray_6d6d6d.png" 
                alt="Fork me on GitHub" />
    </a>
    
    <div id="logInP">
        <h2>Log in</h2>
        <label id='logInErrLb' class='errLb'>&nbsp;</label>
        <input type='text' name='username' placeholder='username or email' id='logInUsernameTB' /> 
        <input type='password' name='password'  placeholder='password' id='logInPasswordTB' />
        <button id='logInBtn'>OK</button>
        <img src='/img/loading.gif' id='logInLoadingImg' style='display: none;' />
        <a href="/forgot">Forgot password?</a>
    </div>
    
    <h1>Welcome to Thai-Accounting (beta)</h1>
    
    <div id='descP'>
        <p>2 Steps to generate accounting reports</p>
        <p>1. Setup</p>
        <ul>
            <li>Journal type</li>
            <li>Document type</li>
            <li>Account group</li>
            <li>Account chart</li>
            <li>Beginning</li>
            <li>Financial statement</li>
        </ul>
        <p>2. Add journals</p>
        <p>The accounting reports are</p>
        <ul>
            <li>Account chart</li>
            <li>Journal</li>
            <li>Ledger</li>
            <li>Trial</li>
            <li>Balance sheet</li>
            <li>Profit and loss account</li>
        </ul>
    </div>

    <div id="signUpP">
        <h2>Create an account</h2>    
        <label id='signUpUsernameErrLb' class='errLb'>&nbsp;</label>
        <input type='text' name='username' placeholder='username' id='signUpUsernameTB' />
        <label id='signUpPasswordErrLb' class='errLb'>&nbsp;</label> 
        <input type='password' name='password'  placeholder='password' id='signUpPasswordTB' />
        <label id='signUpRepeatPasswordErrLb' class='errLb'>&nbsp;</label> 
        <input type='password' name='password'  placeholder='Repeat password' id='signUpRepeatPasswordTB' />
        <label id='signUpEmailErrLb' class='errLb'>&nbsp;</label>
        <input type='text' name='email'  placeholder='email' id='signUpEmailTB' />
        <button id='signUpBtn'>OK</button>
        <img src='/img/loading.gif' id='signUpLoadingImg' style='display: none;' />
    </div>
    
    <div id='footer'>
        Thai accounting 2012. All rights Reserved
    </div>
    
    <!-- JavaScript at the bottom for fast page loading -->
    
    <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="js/libs/jquery-1.7.1.min.js"><\/script>')</script>
    
    <!-- scripts concatenated and minified via build script -->
    <script src="js/userverifier-alpha.js"></script>
    <script src="js/home-alpha.js"></script>
    <script>
        $(document).ready(function () {
            JS_HOME.init();    
        });
    </script>
    <!-- end scripts -->
    
</body>
</html>
