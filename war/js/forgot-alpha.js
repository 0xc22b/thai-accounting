/*global window, document, jQuery, USER_VERIFIER */
var JS_FORGOT = (function ($, UserVerifier) {
  'use strict';

  var that, errAjax, setLoading,
    formPanel, resultLb,
    usernameOrEmailErrLb, usernameOrEmailTB,
    newPasswordErrLb, newPasswordTB, repeatPasswordErrLb, repeatPasswordTB,
    okBtn, loadingImg;

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
    getUrlQuery: function () {
      var oGetVars = {}, aItKey, nKeyId, aCouples;

      if (window.location.search.length > 1) {
        aCouples = window.location.search.substr(1).split("&");
        for (nKeyId = 0; nKeyId < aCouples.length; nKeyId = nKeyId + 1) {
          aItKey = aCouples[nKeyId].split("=");
          oGetVars[window.unescape(aItKey[0])] = aItKey.length > 1
            ? window.unescape(aItKey[1]) : "";
        }
      }

      return oGetVars;
    },

    onForgotPasswordOkBtnClick: function () {
      var usernameOrEmail, log, logInfo, dataString;

      usernameOrEmail = $.trim(usernameOrEmailTB.val());

      log = [];
      if (usernameOrEmail.indexOf('@') === -1) {
        if (!UserVerifier.isUsernameValid(usernameOrEmail, log)) {
          logInfo = UserVerifier.getLogInfo(log,
              UserVerifier.LogInfoType.username, false);
          usernameOrEmailErrLb.html(logInfo.msg);
          return false;
        }
      } else {
        if (!UserVerifier.isEmailValid(usernameOrEmail, log)) {
          logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email,
              false);
          usernameOrEmailTB.html(logInfo.msg);
          return false;
        }
      }

      usernameOrEmailErrLb.html('&nbsp;');

      setLoading(true, okBtn, loadingImg);

      dataString = 'username=' + usernameOrEmail;

      $.ajax({
        type: 'POST',
        url: '/forgot',
        data: dataString,
        success: function (res) {
          var serverLog, logInfo;

          serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
              UserVerifier.LogInfoType.forgotPassword);
          if (logInfo && logInfo.isValid) {
            resultLb.html(logInfo.msg);
            formPanel.hide();
            resultLb.show();
          } else {
            UserVerifier.setErrLb(usernameOrEmailErrLb, serverLog,
                                  UserVerifier.LogInfoType.username, false);
            UserVerifier.setErrLb(usernameOrEmailErrLb, serverLog,
                                  UserVerifier.LogInfoType.email, false);

            usernameOrEmailErrLb.html(logInfo.msg);

            setLoading(false, okBtn, loadingImg);
          }
        },
        error: function () {
          //Popup connection error.
          window.alert(errAjax);
          setLoading(false, okBtn, loadingImg);
        }
      });
      return false;
    },

    onUsernameOrEmailTBBlur: function () {
      var log = [], logInfo;

      if (usernameOrEmailTB.val().indexOf('@') === -1) {
        UserVerifier.isUsernameValid(usernameOrEmailTB.val(), log);
        logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.username);
      } else {
        UserVerifier.isEmailValid(usernameOrEmailTB.val(), log);
        logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
      }

      // Only if the current value is still the same.
      if (logInfo.value === usernameOrEmailTB.val()) {
        if (logInfo.isValid) {
          usernameOrEmailErrLb.html('&nbsp;');
        } else {
          usernameOrEmailErrLb.html(logInfo.msg);
        }
      }
    },

    onResetPasswordOkBtnClick: function () {
      var newPassword, repeatPassword, log, logInfo, dataString;

      newPassword = $.trim(newPasswordTB.val());
      repeatPassword = $.trim(repeatPasswordTB.val());

      log = [];
      if (!UserVerifier.isPasswordValid(newPassword, log,
          UserVerifier.LogInfoType.newPassword) ||
          !UserVerifier.isRepeatPasswordValid(newPassword, repeatPassword, log)) {

        UserVerifier.setErrLb(newPasswordErrLb, log,
                              UserVerifier.LogInfoType.newPassword, false);
        UserVerifier.setErrLb(repeatPasswordErrLb, log,
                              UserVerifier.LogInfoType.repeatPassword, false);
        return false;
      }

      newPasswordErrLb.html('&nbsp;');
      repeatPasswordErrLb.html('&nbsp;');

      setLoading(true, okBtn, loadingImg);

      dataString = 'ssid=' + that.getUrlQuery().ssid + '&fid=' + that.getUrlQuery().fid
        + '&newPassword=' + newPassword + "&repeatPassword=" + repeatPassword;

      $.ajax({
        type: 'POST',
        url: '/forgot',
        data: dataString,
        success: function (res) {
          var serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
                                            UserVerifier.LogInfoType.resetPassword);
          if (logInfo && logInfo.isValid) {
            resultLb.html(logInfo.msg);

            formPanel.hide();
            resultLb.show();
          } else {
            UserVerifier.setErrLb(newPasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.password, false);
            UserVerifier.setErrLb(repeatPasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.repeatPassword, false);

            newPasswordErrLb.html(logInfo.msg);

            setLoading(false, okBtn, loadingImg);
          }
        },
        error: function () {
          //Popup connection error.
          window.alert(errAjax);
          setLoading(false, okBtn, loadingImg);
        }
      });
      return false;
    },

    onNewPasswordTBBlur: function () {
      var log = [], logInfo;
      UserVerifier.isPasswordValid(newPasswordTB.val(), log,
          UserVerifier.LogInfoType.newPassword);
      logInfo = UserVerifier.getLogInfo(log,
          UserVerifier.LogInfoType.newPassword);
      // Only if the current value is still the same.
      if (logInfo.value === newPasswordTB.val()) {
        if (logInfo.isValid) {
          newPasswordErrLb.html('&nbsp;');
        } else {
          newPasswordErrLb.html(logInfo.msg);
        }
      }
    },

    onRepeatPasswordTBBlur: function () {
      var log = [], logInfo;
      UserVerifier.isRepeatPasswordValid(newPasswordTB.val(),
                                         repeatPasswordTB.val(), log);
      logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.repeatPassword);
      // Only if the current value is still the same.
      if (logInfo.value === repeatPasswordTB.val()) {
        if (logInfo.isValid) {
          repeatPasswordErrLb.html('&nbsp;');
        } else {
          repeatPasswordErrLb.html(logInfo.msg);
        }
      }
    },

    init: function () {

      that = this;

      formPanel = $('#formPanel');
      resultLb = $('#resultLb');

      okBtn = $('#okBtn');
      loadingImg = $('#loadingImg');

      if (that.getUrlQuery().fid) {
        newPasswordErrLb = $('#newPasswordErrLb');
        newPasswordTB = $('#newPasswordTB');
        repeatPasswordErrLb = $('#repeatPasswordErrLb');
        repeatPasswordTB = $('#repeatPasswordTB');

        okBtn.click(that.onResetPasswordOkBtnClick);

        newPasswordTB.blur(that.onNewPasswordTBBlur);
        repeatPasswordTB.blur(that.onRepeatPasswordTBBlur);
      } else {
        usernameOrEmailErrLb = $('#usernameOrEmailErrLb');
        usernameOrEmailTB = $('#usernameOrEmailTB');

        okBtn.click(that.onForgotPasswordOkBtnClick);

        usernameOrEmailTB.blur(that.onUsernameOrEmailTBBlur);
      }
    }
  };
}(jQuery, USER_VERIFIER));
