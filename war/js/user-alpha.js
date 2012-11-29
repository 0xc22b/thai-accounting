/*global window, document, jQuery, USER_VERIFIER */
var JS_USER = (function ($, UserVerifier) {
  'use strict';

  var that, errAjax, errNotLoggedIn, methodName, parameterName, setLoading,
    readCookie, genDataString, onChangeBtnClick,

    changeUsernameLb, changeUsernameBtn, changeUsernameEditor,
    changeUsernameErrLb, changeUsernameTB, changeUsernamePasswordErrLb,
    changeUsernamePasswordTB, changeUsernameOkBtn, changeUsernameLoadingImg,

    changeEmailLb, changeEmailBtn, changeEmailEditor,
    changeEmailErrLb, changeEmailTB, changeEmailPasswordErrLb,
    changeEmailPasswordTB, changeEmailOkBtn, changeEmailLoadingImg,

    resendEmailConfirmBtn,

    changePasswordBtn, changePasswordEditor,
    changeNewPasswordErrLb, changeNewPasswordTB, changeRepeatPasswordErrLb,
    changeRepeatPasswordTB, changePasswordErrLb, changePasswordTB,
    changePasswordOkBtn, changePasswordLoadingImg,

    changeLangLb, changeLangBtn, changeLangEditor, changeLangErrLb, changeLangS,
    changeLangOkBtn, changeLangLoadingImg,

    deleteAccountBtn, deleteAccountEditor,
    deleteAccountPasswordErrLb, deleteAccountPasswordTB,
    deleteAccountOkBtn, deleteAccountLoadingImg;

  errAjax = 'Could not connect to the server. Please try again.';
  errNotLoggedIn = 'Please sign in. Going to Sign in page...';

  methodName = {
    changeUsername: 'changeUsername',
    changeEmail: 'changeEmail',
    changePassword: 'changePassword',
    changeLang: 'changeLang',
    deleteAccount: 'deleteAccount',
    resendEmailConfirm: 'resendEmailConfirm'
  };

  parameterName = {
    username: 'username',
    email: 'email',
    password: 'password',
    repeatPassword: 'repeatPassword',
    newPassword: 'newPassword',
    lang: 'lang'
  };

  setLoading = function (isLoading, okBtn, loadingImg) {
    if (isLoading) {
      okBtn.hide();
      loadingImg.show();
    } else {
      okBtn.show();
      loadingImg.hide();
    }
  };

  readCookie = function (name) {
    var nameEQ, ca, i, c;
    nameEQ = name + "=";
    ca = document.cookie.split(';');
    for (i = 0; i < ca.length; i = i + 1) {
      c = ca[i];
      while (c.charAt(0) === ' ') {
        c = c.substring(1, c.length);
      }
      if (c.indexOf(nameEQ) === 0) {
        return c.substring(nameEQ.length, c.length);
      }
    }
    return null;
  };

  genDataString = function (method, contentList) {
    var dataString, i, content;
    dataString = 'ssid=' + readCookie('SSID');
    dataString += '&sid=' + readCookie('SID');
    dataString += '&method=' + method;
    for (i = 0; i < contentList.length; i = i + 1) {
      content = contentList[i];
      dataString += '&' + content.name + '=' + content.value;
    }
    return dataString;
  };

  onChangeBtnClick = function (changeBtn, changeBtnText, editor, labelList,
      textBoxList) {
    var i, label, textBox;
    if (editor.is(':visible')) {
      editor.hide();
      changeBtn.text(changeBtnText);
    } else {
      // Reset the inputs
      for (i = 0; i < labelList.length; i = i + 1) {
        label = labelList[i];
        label.html('&nbsp;');
      }
      for (i = 0; i < textBoxList.length; i = i + 1) {
        textBox = textBoxList[i];
        textBox.val('');
      }
      // Change the button's text to hide
      changeBtn.text('hide');
      editor.show();
    }
  };

  return {
    goHome: function () {
      window.location.assign('/');
    },

    onChangeUsernameBtnClick: function () {
      onChangeBtnClick(
        changeUsernameBtn,
        'change',
        changeUsernameEditor,
        [changeUsernameErrLb, changeUsernamePasswordErrLb],
        [changeUsernameTB, changeUsernamePasswordTB]
      );
    },

    onChangeUsernameOkBtnClick: function () {
      var username, password, log, contentList;

      username = changeUsernameTB.val().trim();
      password = changeUsernamePasswordTB.val().trim();

      log = [];
      if (!UserVerifier.isUsernameValid(username, log) ||
          !UserVerifier.isPasswordValid(password, log,
              UserVerifier.LogInfoType.password)) {
        UserVerifier.setErrLb(changeUsernameErrLb, log,
                              UserVerifier.LogInfoType.username, false);
        UserVerifier.setErrLb(changeUsernamePasswordErrLb, log,
                              UserVerifier.LogInfoType.password, false);
        return false;
      }

      changeUsernameErrLb.html('&nbsp');
      changeUsernamePasswordErrLb.html('&nbsp');

      setLoading(true, changeUsernameOkBtn, changeUsernameLoadingImg);

      contentList = [
        {
          name: parameterName.username,
          value: username
        }, {
          name: parameterName.password,
          value: password
        }
      ];

      $.ajax({
        type: 'POST',
        url: '/user',
        data: genDataString(methodName.changeUsername, contentList),
        success: function (res) {
          var serverLog, logInfo;

          serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
              UserVerifier.LogInfoType.changeUsername);
          if (logInfo && logInfo.isValid) {
            //TODO: update completed.
            changeUsernameLb.html(username);
            setLoading(false, changeUsernameOkBtn, changeUsernameLoadingImg);
            that.onChangeUsernameBtnClick();
          } else {
            UserVerifier.setErrLb(changeUsernameErrLb, serverLog,
                                  UserVerifier.LogInfoType.username, false);
            UserVerifier.setErrLb(changeUsernamePasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.password, false);
            setLoading(false, changeUsernameOkBtn, changeUsernameLoadingImg);

            logInfo = UserVerifier.getLogInfo(serverLog,
                UserVerifier.LogInfoType.didLogIn, false);
            if (logInfo) {
              window.alert(errNotLoggedIn);
              that.goHome();
            }
          }
        },
        error: function () {
          //Popup connection error.
          setLoading(false, changeUsernameOkBtn, changeUsernameLoadingImg);
          window.alert(errAjax);
        }
      });

      return false;
    },

    onChangeUsernameTBBlur: function () {
      var log = [], logInfo;
      UserVerifier.isUsernameValid(changeUsernameTB.val(), log, function (log) {
        logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.username);
        // Only if the current value is still the same.
        if (logInfo.value === changeUsernameTB.val()) {
          if (logInfo.isValid) {
            changeUsernameErrLb.html('&nbsp;');
          } else {
            changeUsernameErrLb.html(logInfo.msg);
          }
        }
      });
    },

    onChangeUsernamePasswordTBBlur: function (e) {
      var log = [], logInfo;
      UserVerifier.isPasswordValid(changeUsernamePasswordTB.val(), log,
          UserVerifier.LogInfoType.password);
      logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.password);
      // Only if the current value is still the same.
      if (logInfo.value === changeUsernamePasswordTB.val()) {
        if (logInfo.isValid) {
          changeUsernamePasswordErrLb.html('&nbsp;');
        } else {
          changeUsernamePasswordErrLb.html(logInfo.msg);
        }
      }
    },

    onChangeEmailBtnClick: function () {
      onChangeBtnClick(
        changeEmailBtn,
        'change',
        changeEmailEditor,
        [changeEmailErrLb, changeEmailPasswordErrLb],
        [changeEmailTB, changeEmailPasswordTB]
      );
    },

    onChangeEmailOkBtnClick: function (e) {
      var email, password, log, contentList;

      email = changeEmailTB.val().trim();
      password = changeEmailPasswordTB.val().trim();

      log = [];
      if (!UserVerifier.isEmailValid(email, log) ||
          !UserVerifier.isPasswordValid(password, log,
              UserVerifier.LogInfoType.password)) {
        UserVerifier.setErrLb(changeEmailErrLb, log,
                              UserVerifier.LogInfoType.email, false);
        UserVerifier.setErrLb(changeEmailPasswordErrLb, log,
                              UserVerifier.LogInfoType.password, false);
        return false;
      }

      changeEmailErrLb.html('&nbsp');
      changeEmailPasswordErrLb.html('&nbsp');

      setLoading(true, changeEmailOkBtn, changeEmailLoadingImg);

      contentList = [
        {
          name: parameterName.email,
          value: email
        }, {
          name: parameterName.password,
          value: password
        }
      ];

      $.ajax({
        type: 'POST',
        url: '/user',
        data: genDataString(methodName.changeEmail, contentList),
        success: function (res) {
          var serverLog, logInfo;

          serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
              UserVerifier.LogInfoType.changeEmail);
          if (logInfo && logInfo.isValid) {
            //TODO: update completed.
            changeEmailLb.html(email);
            setLoading(false, changeEmailOkBtn, changeEmailLoadingImg);
            that.onChangeEmailBtnClick();
          } else {
            UserVerifier.setErrLb(changeEmailErrLb, serverLog,
                                  UserVerifier.LogInfoType.email, false);
            UserVerifier.setErrLb(changeEmailPasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.password, false);
            setLoading(false, changeEmailOkBtn, changeEmailLoadingImg);

            logInfo = UserVerifier.getLogInfo(serverLog,
                UserVerifier.LogInfoType.didLogIn, false);
            if (logInfo) {
              window.alert(errNotLoggedIn);
              that.goHome();
            }
          }
        },
        error: function () {
          //Popup connection error.
          setLoading(false, changeEmailOkBtn, changeEmailLoadingImg);
          window.alert(errAjax);
        }
      });

      return false;
    },

    onChangeEmailTBBlur: function (e) {
      var log = [], logInfo;
      UserVerifier.isEmailValid(changeEmailTB.val(), log, function (log) {
        logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
        // Only if the current value is still the same.
        if (logInfo.value === changeEmailTB.val()) {
          if (logInfo.isValid) {
            changeEmailErrLb.html('&nbsp;');
          } else {
            changeEmailErrLb.html(logInfo.msg);
          }
        }
      });
    },

    onChangeEmailPasswordTBBlur: function (e) {
      var log = [], logInfo;
      UserVerifier.isPasswordValid(changeEmailPasswordTB.val(), log,
          UserVerifier.LogInfoType.password);
      logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.password);
      // Only if the current value is still the same.
      if (logInfo.value === changeEmailPasswordTB.val()) {
        if (logInfo.isValid) {
          changeEmailPasswordErrLb.html('&nbsp;');
        } else {
          changeEmailPasswordErrLb.html(logInfo.msg);
        }
      }
    },

    onResendEmailConfirmBtnClick: function (e) {

      // setLoading
      resendEmailConfirmBtn.html('sending...');
      resendEmailConfirmBtn.attr('disabled', 'disabled');

      $.ajax({
        type: 'POST',
        url: '/user',
        data: genDataString(methodName.resendEmailConfirm, []),
        success: function (res) {
          var serverLog, logInfo;
          serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
                                            UserVerifier.LogInfoType.sendEmailConfirm);
          if (logInfo && logInfo.isValid) {
            resendEmailConfirmBtn.html('sent');
          } else {
            resendEmailConfirmBtn.html('retry');
            resendEmailConfirmBtn.removeAttr('disabled');
          }
        },
        error: function () {
          //Popup connection error.
          resendEmailConfirmBtn.removeAttr('disabled');
          window.alert(errAjax);
        }
      });

      return false;
    },

    onChangePasswordBtnClick: function () {
      onChangeBtnClick(
        changePasswordBtn,
        'Change password',
        changePasswordEditor,
        [changeNewPasswordErrLb, changeRepeatPasswordErrLb, changePasswordErrLb],
        [changeNewPasswordTB, changeRepeatPasswordTB, changePasswordTB]
      );
    },

    onChangePasswordOkBtnClick: function (e) {
      var newPassword, repeatPassword, password, log, contentList;

      newPassword = changeNewPasswordTB.val().trim();
      repeatPassword = changeRepeatPasswordTB.val().trim();
      password = changePasswordTB.val().trim();

      log = [];
      if (!UserVerifier.isPasswordValid(newPassword, log, UserVerifier.LogInfoType.newPassword)
            || !UserVerifier.isRepeatPasswordValid(newPassword, repeatPassword, log)
            || !UserVerifier.isPasswordValid(password, log,
                UserVerifier.LogInfoType.password)) {
        UserVerifier.setErrLb(changeNewPasswordErrLb, log,
                              UserVerifier.LogInfoType.newPassword, false);
        UserVerifier.setErrLb(changeRepeatPasswordErrLb, log,
                              UserVerifier.LogInfoType.repeatPassword, false);
        UserVerifier.setErrLb(changePasswordErrLb, log,
                              UserVerifier.LogInfoType.password, false);
        return false;
      }

      changeNewPasswordErrLb.html('&nbsp');
      changeRepeatPasswordErrLb.html('&nbsp');
      changePasswordErrLb.html('&nbsp');

      setLoading(true, changePasswordOkBtn, changePasswordLoadingImg);

      contentList = [
        {
          name: parameterName.newPassword,
          value: newPassword
        }, {
          name: parameterName.repeatPassword,
          value: repeatPassword
        }, {
          name: parameterName.password,
          value: password
        }
      ];

      $.ajax({
        type: 'POST',
        url: '/user',
        data: genDataString(methodName.changePassword, contentList),
        success: function (res) {
          var serverLog, logInfo;

          serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
                                            UserVerifier.LogInfoType.changePassword);
          if (logInfo && logInfo.isValid) {
            //TODO: update completed.
            setLoading(false, changePasswordOkBtn, changePasswordLoadingImg);
            that.onChangePasswordBtnClick();
            window.alert('Your password has been changed.');
          } else {
            UserVerifier.setErrLb(changeNewPasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.newPassword, false);
            UserVerifier.setErrLb(changeRepeatPasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.repeatPassword, false);
            UserVerifier.setErrLb(changePasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.password, false);
            setLoading(false, changePasswordOkBtn, changePasswordLoadingImg);

            logInfo = UserVerifier.getLogInfo(serverLog,
                                              UserVerifier.LogInfoType.didLogIn, false);
            if (logInfo) {
              window.alert(errNotLoggedIn);
              that.goHome();
            }
          }
        },
        error: function () {
          //Popup connection error.
          setLoading(false, changePasswordOkBtn, changePasswordLoadingImg);
          window.alert(errAjax);
        }
      });

      return false;
    },

    onChangeNewPasswordTBBlur: function (e) {
      var log = [], logInfo;
      UserVerifier.isPasswordValid(changeNewPasswordTB.val(), log,
          UserVerifier.LogInfoType.newPassword);
      logInfo = UserVerifier.getLogInfo(log,
          UserVerifier.LogInfoType.newPassword);
      // Only if the current value is still the same.
      if (logInfo.value === changeNewPasswordTB.val()) {
        if (logInfo.isValid) {
          changeNewPasswordErrLb.html('&nbsp;');
        } else {
          changeNewPasswordErrLb.html(logInfo.msg);
        }
      }
    },

    onChangeRepeatPasswordTBBlur: function (e) {
      var log = [], logInfo;
      UserVerifier.isRepeatPasswordValid(changeNewPasswordTB.val(),
                                         changeRepeatPasswordTB.val(), log);
      logInfo = UserVerifier.getLogInfo(log,
                                        UserVerifier.LogInfoType.repeatPassword);
      // Only if the current value is still the same.
      if (logInfo.value === changeRepeatPasswordTB.val()) {
        if (logInfo.isValid) {
          changeRepeatPasswordErrLb.html('&nbsp;');
        } else {
          changeRepeatPasswordErrLb.html(logInfo.msg);
        }
      }
    },

    onChangePasswordTBBlur: function (e) {
      var log = [], logInfo;
      UserVerifier.isPasswordValid(changePasswordTB.val(), log,
          UserVerifier.LogInfoType.password);
      logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.password);
      // Only if the current value is still the same.
      if (logInfo.value === changePasswordTB.val()) {
        if (logInfo.isValid) {
          changePasswordErrLb.html('&nbsp;');
        } else {
          changePasswordErrLb.html(logInfo.msg);
        }
      }
    },

    onChangeLangBtnClick: function () {
      onChangeBtnClick(
        changeLangBtn,
        'change',
        changeLangEditor,
        [changeLangErrLb],
        []
      );
    },

    onChangeLangOkBtnClick: function () {
      var lang, log, contentList;

      lang = changeLangS.val();

      changeLangErrLb.html('&nbsp');

      setLoading(true, changeLangOkBtn, changeLangLoadingImg);

      contentList = [
        {
          name: parameterName.lang,
          value: lang
        }
      ];

      $.ajax({
        type: 'POST',
        url: '/user',
        data: genDataString(methodName.changeLang, contentList),
        success: function (res) {
          var serverLog, logInfo;

          serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
              UserVerifier.LogInfoType.changeLang);
          if (logInfo && logInfo.isValid) {
            //TODO: update completed.
            changeLangLb.html(lang);
            setLoading(false, changeLangOkBtn, changeLangLoadingImg);
            that.onChangeLangBtnClick();
          } else {
            UserVerifier.setErrLb(changeLangErrLb, serverLog,
                UserVerifier.LogInfoType.changeLang, false);
            setLoading(false, changeLangOkBtn, changeLangLoadingImg);

            logInfo = UserVerifier.getLogInfo(serverLog,
                UserVerifier.LogInfoType.didLogIn, false);
            if (logInfo) {
              window.alert(errNotLoggedIn);
              that.goHome();
            }
          }
        },
        error: function () {
          //Popup connection error.
          setLoading(false, changeLangOkBtn, changeLangLoadingImg);
          window.alert(errAjax);
        }
      });

      return false;
    },

    onDeleteAccountBtnClick: function () {
      onChangeBtnClick(
        deleteAccountBtn,
        'Delete account',
        deleteAccountEditor,
        [deleteAccountPasswordErrLb],
        [deleteAccountPasswordTB]
      );
    },

    onDeleteAccountOkBtnClick: function () {
      var password, log, contentList;

      password = deleteAccountPasswordTB.val().trim();

      log = [];
      if (!UserVerifier.isPasswordValid(password, log,
          UserVerifier.LogInfoType.password)) {
        UserVerifier.setErrLb(deleteAccountPasswordErrLb, log,
                              UserVerifier.LogInfoType.password, false);
        return false;
      }

      deleteAccountPasswordErrLb.html('&nbsp');

      setLoading(true, deleteAccountOkBtn, deleteAccountLoadingImg);

      contentList = [
        {
          name: parameterName.password,
          value: password
        }
      ];

      $.ajax({
        type: 'POST',
        url: '/user',
        data: genDataString(methodName.deleteAccount, contentList),
        success: function (res) {
          var serverLog, logInfo;

          serverLog = $.parseJSON(res);
          logInfo = UserVerifier.getLogInfo(serverLog,
                                            UserVerifier.LogInfoType.deleteAccount);
          if (logInfo && logInfo.isValid) {
            setLoading(false, deleteAccountOkBtn, deleteAccountLoadingImg);
            window.alert('Your account has been deleted!');
            that.goHome();
          } else {
            UserVerifier.setErrLb(deleteAccountPasswordErrLb, serverLog,
                                  UserVerifier.LogInfoType.password, false);
            setLoading(false, deleteAccountOkBtn, deleteAccountLoadingImg);

            logInfo = UserVerifier.getLogInfo(serverLog,
                                              UserVerifier.LogInfoType.didLogIn, false);
            if (logInfo) {
              window.alert(errNotLoggedIn);
              that.goHome();
            }
          }
        },
        error: function () {
          //Popup connection error.
          setLoading(false, deleteAccountOkBtn, deleteAccountLoadingImg);
          window.alert(errAjax);
        }
      });

      return false;
    },

    onDeleteAccountPasswordTBBlur: function () {
      var log = [], logInfo;
      UserVerifier.isPasswordValid(deleteAccountPasswordTB.val(), log,
          UserVerifier.LogInfoType.password);
      logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.password);
      // Only if the current value is still the same.
      if (logInfo.value === deleteAccountPasswordTB.val()) {
        if (logInfo.isValid) {
          deleteAccountPasswordErrLb.html('&nbsp;');
        } else {
          deleteAccountPasswordErrLb.html(logInfo.msg);
        }
      }
    },

    init: function () {

      that = this;

      changeUsernameLb = $('#changeUsernameLb');
      changeUsernameBtn = $('#changeUsernameBtn');
      changeUsernameLoadingImg = $('#changeUsernameLoadingImg');
      changeUsernameEditor = $('#changeUsernameEditor');
      changeUsernameErrLb = $('#changeUsernameErrLb');
      changeUsernameTB = $('#changeUsernameTB');
      changeUsernamePasswordErrLb = $('#changeUsernamePasswordErrLb');
      changeUsernamePasswordTB = $('#changeUsernamePasswordTB');
      changeUsernameOkBtn = $('#changeUsernameOkBtn');

      changeUsernameBtn.click(that.onChangeUsernameBtnClick);
      changeUsernameTB.blur(that.onChangeUsernameTBBlur);
      changeUsernamePasswordTB.blur(that.onChangeUsernamePasswordTBBlur);
      changeUsernameOkBtn.click(that.onChangeUsernameOkBtnClick);

      changeEmailLb = $('#changeEmailLb');
      changeEmailBtn = $('#changeEmailBtn');
      changeEmailLoadingImg = $('#changeEmailLoadingImg');
      changeEmailEditor = $('#changeEmailEditor');
      changeEmailErrLb = $('#changeEmailErrLb');
      changeEmailTB = $('#changeEmailTB');
      changeEmailPasswordErrLb = $('#changeEmailPasswordErrLb');
      changeEmailPasswordTB = $('#changeEmailPasswordTB');
      changeEmailOkBtn = $('#changeEmailOkBtn');

      changeEmailBtn.click(that.onChangeEmailBtnClick);
      changeEmailTB.blur(that.onChangeEmailTBBlur);
      changeEmailPasswordTB.blur(that.onChangeEmailPasswordTBBlur);
      changeEmailOkBtn.click(that.onChangeEmailOkBtnClick);

      resendEmailConfirmBtn = $('#resendEmailConfirmBtn');
      if (resendEmailConfirmBtn) {
        resendEmailConfirmBtn.click(that.onResendEmailConfirmBtnClick);
      }

      changePasswordBtn = $('#changePasswordBtn');
      changePasswordLoadingImg = $('#changePasswordLoadingImg');
      changePasswordEditor = $('#changePasswordEditor');
      changeNewPasswordErrLb = $('#changeNewPasswordErrLb');
      changeNewPasswordTB = $('#changeNewPasswordTB');
      changeRepeatPasswordErrLb = $('#changeRepeatPasswordErrLb');
      changeRepeatPasswordTB = $('#changeRepeatPasswordTB');
      changePasswordErrLb = $('#changePasswordErrLb');
      changePasswordTB = $('#changePasswordTB');
      changePasswordOkBtn = $('#changePasswordOkBtn');

      changePasswordBtn.click(that.onChangePasswordBtnClick);
      changeNewPasswordTB.blur(that.onChangeNewPasswordTBBlur);
      changeRepeatPasswordTB.blur(that.onChangeRepeatPasswordTBBlur);
      changePasswordTB.blur(that.onChangePasswordTBBlur);
      changePasswordOkBtn.click(that.onChangePasswordOkBtnClick);

      changeLangLb = $('#changeLangLb');
      changeLangBtn = $('#changeLangBtn');
      changeLangLoadingImg = $('#changeLangLoadingImg');
      changeLangEditor = $('#changeLangEditor');
      changeLangErrLb = $('#changeLangErrLb');
      changeLangS = $('#changeLangS');
      changeLangOkBtn = $('#changeLangOkBtn');

      changeLangBtn.click(that.onChangeLangBtnClick);
      changeLangOkBtn.click(that.onChangeLangOkBtnClick);

      deleteAccountBtn = $('#deleteAccountBtn');
      deleteAccountLoadingImg = $('#deleteAccountLoadingImg');
      deleteAccountEditor = $('#deleteAccountEditor');
      deleteAccountPasswordErrLb = $('#deleteAccountPasswordErrLb');
      deleteAccountPasswordTB = $('#deleteAccountPasswordTB');
      deleteAccountOkBtn = $('#deleteAccountOkBtn');

      deleteAccountBtn.click(that.onDeleteAccountBtnClick);
      deleteAccountPasswordTB.blur(that.onDeleteAccountPasswordTBBlur);
      deleteAccountOkBtn.click(that.onDeleteAccountOkBtnClick);

    }
  };
}(jQuery, USER_VERIFIER));
