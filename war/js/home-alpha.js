/*global window, document, jQuery, USER_VERIFIER */

var JS_HOME = (function ($, UserVerifier) {
  'use strict';

  var that, errAjax, setLoading,
    logInUsernameTB, logInPasswordTB, logInErrLb, logInBtn, logInLoadingImg,
    signUpUsernameTB, signUpEmailTB, signUpPasswordTB, signUpRepeatPasswordTB,
    signUpUsernameErrLb, signUpEmailErrLb, signUpPasswordErrLb,
    signUpRepeatPasswordErrLb, signUpBtn, signUpLoadingImg;

  errAjax = 'Could not connect to the server. Please try again.';

  setLoading = function (isLoading, okBtn, loadingImg) {
    if (isLoading) {
      okBtn.hide();
      loadingImg.show();
    } else {
      okBtn.show();
      loadingImg.hide();
    }
  };

  return {
    onSuccessLogIn: function () {
      document.location.reload();
    },

    onLogInBtnClick: function () {
      var usernameOrEmail, password, log, isValid, dataString;

      usernameOrEmail = logInUsernameTB.val().trim();
      password = logInPasswordTB.val().trim();

      log = [];
      if (usernameOrEmail.indexOf('@') === -1) {
        isValid = UserVerifier.isUsernameValid(usernameOrEmail, log);
      } else {
        isValid = UserVerifier.isEmailValid(usernameOrEmail, log);
      }
      if (!isValid ||
          !UserVerifier.isPasswordValid(password, log,
              UserVerifier.LogInfoType.password)) {
        logInErrLb.html(UserVerifier.errLogIn);
        return false;
      }

      logInErrLb.html('&nbsp;');

      setLoading(true, logInBtn, logInLoadingImg);

      dataString = 'username=' + usernameOrEmail + '&password=' + password;

      $.ajax({
        type: 'POST',
        url: '/login',
        data: dataString,
        success: function (res) {
          var serverLog, logInfo;

          serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
                                            UserVerifier.LogInfoType.logIn);
          if (logInfo && logInfo.isValid) {
            that.onSuccessLogIn();
          } else {
            logInErrLb.html(UserVerifier.errLogIn);
            setLoading(false, logInBtn, logInLoadingImg);
          }
        },
        error: function () {
          //Popup connection error.
          window.alert(errAjax);
          setLoading(false, logInBtn, logInLoadingImg);
        }
      });
      return false;
    },

    onSignUpBtnClick: function () {
      var username, email, password, repeatPassword, log, isValid1, isValid2,
        isValid3, isValid4, logInfo, dataString;

      username = signUpUsernameTB.val().trim();
      email = signUpEmailTB.val().trim().toLowerCase();
      password = signUpPasswordTB.val().trim();
      repeatPassword = signUpRepeatPasswordTB.val().trim();

      log = [];
      isValid1 = UserVerifier.isUsernameValid(username, log);
      isValid2 = UserVerifier.isEmailValid(email, log);
      isValid3 = UserVerifier.isPasswordValid(password, log,
          UserVerifier.LogInfoType.password);
      isValid4 = UserVerifier.isRepeatPasswordValid(password, repeatPassword, log);
      if (!isValid1 || !isValid2 || !isValid3 || !isValid4) {
        UserVerifier.setErrLb(signUpUsernameErrLb, log,
                              UserVerifier.LogInfoType.username, false);
        UserVerifier.setErrLb(signUpEmailErrLb, log,
                              UserVerifier.LogInfoType.email, false);
        UserVerifier.setErrLb(signUpPasswordErrLb, log,
                              UserVerifier.LogInfoType.password, false);
        UserVerifier.setErrLb(signUpRepeatPasswordErrLb, log,
                              UserVerifier.LogInfoType.repeatPassword, false);
        return false;
      }

      signUpUsernameErrLb.html('&nbsp;');
      signUpEmailErrLb.html('&nbsp;');
      signUpPasswordErrLb.html('&nbsp;');
      signUpRepeatPasswordErrLb.html('&nbsp;');

      setLoading(true, signUpBtn, signUpLoadingImg);

      dataString = 'username=' + username + '&email=' + email
        + '&password=' + password + "&repeatPassword=" + repeatPassword;

      $.ajax({
        type: 'POST',
        url: '/signup',
        data: dataString,
        success: function (res) {
          var serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
                                            UserVerifier.LogInfoType.signUp);
          if (logInfo && logInfo.isValid) {
            that.onSuccessLogIn();
          } else {
            UserVerifier.setErrLb(signUpUsernameErrLb, serverLog,
                                  UserVerifier.LogInfoType.username, false);
            UserVerifier.setErrLb(signUpEmailErrLb, serverLog,
                                  UserVerifier.LogInfoType.email, false);
            UserVerifier.setErrLb(signUpPasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.password, false);
            UserVerifier.setErrLb(signUpRepeatPasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.repeatPassword, false);
            setLoading(false, signUpBtn, signUpLoadingImg);
          }
        },
        error: function () {
          //Popup connection error.
          window.alert(errAjax);
          setLoading(false, signUpBtn, signUpLoadingImg);
        }
      });
      return false;
    },

    onSignUpUsernameTBBlur: function () {
      var log = [], logInfo;
      UserVerifier.isUsernameValid(signUpUsernameTB.val(), log, function (log) {
        logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.username);
        // Only if the current value is still the same.
        if (logInfo.value === signUpUsernameTB.val()) {
          if (logInfo.isValid) {
            signUpUsernameErrLb.html('&nbsp;');
          } else {
            signUpUsernameErrLb.html(logInfo.msg);
          }
        }
      });
    },

    onSignUpEmailTBBlur: function () {
      var log = [], logInfo;

      UserVerifier.isEmailValid(signUpEmailTB.val(), log, function (log) {
        logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
        // Only if the current value is still the same.
        if (logInfo.value === signUpEmailTB.val()) {
          if (logInfo.isValid) {
            signUpEmailErrLb.html('&nbsp;');
          } else {
            signUpEmailErrLb.html(logInfo.msg);
          }
        }
      });
    },

    onSignUpPasswordTBBlur: function () {
      var log = [], logInfo;
      UserVerifier.isPasswordValid(signUpPasswordTB.val(), log,
          UserVerifier.LogInfoType.password);
      logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.password);
      // Only if the current value is still the same.
      if (logInfo.value === signUpPasswordTB.val()) {
        if (logInfo.isValid) {
          signUpPasswordErrLb.html('&nbsp;');
        } else {
          signUpPasswordErrLb.html(logInfo.msg);
        }
      }
    },

    onSignUpRepeatPasswordTBBlur: function () {
      var log = [], logInfo;
      UserVerifier.isRepeatPasswordValid(signUpPasswordTB.val(),
                                         signUpRepeatPasswordTB.val(), log);
      logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.repeatPassword);
      // Only if the current value is still the same.
      if (logInfo.value === signUpRepeatPasswordTB.val()) {
        if (logInfo.isValid) {
          signUpRepeatPasswordErrLb.html('&nbsp;');
        } else {
          signUpRepeatPasswordErrLb.html(logInfo.msg);
        }
      }
    },

    init: function () {

      that = this;

      logInUsernameTB = $('#logInUsernameTB');
      logInPasswordTB = $('#logInPasswordTB');
      logInErrLb = $('#logInErrLb');
      logInBtn = $('#logInBtn');
      logInLoadingImg = $('#logInLoadingImg');

      signUpUsernameTB = $('#signUpUsernameTB');
      signUpEmailTB = $('#signUpEmailTB');
      signUpPasswordTB = $('#signUpPasswordTB');
      signUpRepeatPasswordTB = $('#signUpRepeatPasswordTB');
      signUpUsernameErrLb = $('#signUpUsernameErrLb');
      signUpEmailErrLb = $('#signUpEmailErrLb');
      signUpPasswordErrLb = $('#signUpPasswordErrLb');
      signUpRepeatPasswordErrLb = $('#signUpRepeatPasswordErrLb');
      signUpBtn = $('#signUpBtn');
      signUpLoadingImg = $('#signUpLoadingImg');

      logInBtn.click(that.onLogInBtnClick);
      signUpBtn.click(that.onSignUpBtnClick);

      signUpUsernameTB.blur(that.onSignUpUsernameTBBlur);
      signUpEmailTB.blur(that.onSignUpEmailTBBlur);
      signUpPasswordTB.blur(that.onSignUpPasswordTBBlur);
      signUpRepeatPasswordTB.blur(that.onSignUpRepeatPasswordTBBlur);
      signUpRepeatPasswordTB.blur(that.onSignUpRepeatPasswordTBBlur);
    }
  };
}(jQuery, USER_VERIFIER));
