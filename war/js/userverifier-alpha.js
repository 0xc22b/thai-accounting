/*global window, document, jQuery */

//
// In sync with server class UserVerifier.java!
//
var USER_VERIFIER = (function ($) {
  'use strict';

  var allowedCharacters, upperCharacters, errEmpty, errNameLength,
    errNameCharacters, errEmailSpace, errEmailFormat, errPasswordLength,
    errPasswordSpace, errRepeatPassword, errAjax;

  allowedCharacters = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_';
  upperCharacters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

  errEmpty = 'This field is required.';
  errNameLength = 'Please use between 3 and 30 characters.';
  errNameCharacters = 'Please use only letters (a-z, A-Z), numbers, and _.';
  errEmailSpace = 'Email can not contain space.';
  errEmailFormat = 'Email must contain @ and domain name i.e. @example.com';
  errPasswordLength = 'Short passwords are easy to guess. Try one with at least 7 characters.';
  errPasswordSpace = 'Password can not contain space.';
  errRepeatPassword = 'These passwords don\'t match. Please try again.';

  errAjax = 'Could not connect to the server. Please try again.';

  return {
    errLogIn: 'Your username or password are not correct.',

    LogInfoType: {
      username: 'USERNAME',
      email: 'EMAIL',
      password: 'PASSWORD',
      repeatPassword: 'REPEAT_PASSWORD',
      newPassword: 'NEW_PASSWORD',

      signUp: 'SIGN_UP',
      logIn: 'LOG_IN',
      changeUsername: 'CHANGE_USERNAME',
      changeEmail: 'CHANGE_EMAIL',
      changePassword: 'CHANGE_PASSWORD',
      deleteAccount: 'DELETE_ACCOUNT',
      forgotPassword: 'FORGOT_PASSWORD',
      resetPassword: 'RESET_PASSWORD',
      sendEmailConfirm: 'SEND_EMAIL_CONFIRM',
      changeLang: 'CHANGE_LANG',

      didLogIn: 'DID_LOG_IN'
    },

    addLogInfo: function (log, type, value, isValid, msg) {
      log[log.length] = {type: type, value: value, isValid: isValid, msg: msg};
    },

    getLogInfo: function (log, type, opt_isValid) {
      var i, logInfo, logInfoResult;
      for (i = 0; i < log.length; i = i + 1) {
        logInfo = log[i];
        if (logInfo.type === type) {
          if (opt_isValid !== undefined) {
            // If opt_isValid provided, just return the first one that matches.
            if (logInfo.isValid === opt_isValid) {
              return logInfo;
            }
          } else {
            if (logInfoResult) {
              throw {
                name: 'GetLogInfo Error',
                message: 'If opt_isValid is not provided, the object of this type need to be unique.'
              };
            } else {
              logInfoResult = logInfo;
            }
          }
        }
      }
      return logInfoResult;
    },

    setErrLb: function (errLb, log, logInfoType, opt_isValid) {
      var logInfo = this.getLogInfo(log, logInfoType, opt_isValid);
      errLb.html((logInfo && logInfo.msg) || '&nbsp');
    },

    isUsernameValid: function (username, log, opt_callback) {

      var i, s, dataString;

      if (username === undefined || username === null) {
        throw {
          name: 'UsernameValidation Error',
          message: 'Username can not be undefined or null but it is ' + username
        };
      }

      if (username.length === 0) {
        this.addLogInfo(log, this.LogInfoType.username,
            username, false, errEmpty);
        if (opt_callback) { opt_callback(log); }
        return false;
      }

      if (username.length < 3 || username.length > 30) {
        this.addLogInfo(log, this.LogInfoType.username,
            username, false, errNameLength);
        if (opt_callback) { opt_callback(log); }
        return false;
      }

      //Loop every character, allow only letters, number, and _.
      for (i = 0; i < username.length; i = i + 1) {
        s = username.charAt(i);
        if (allowedCharacters.indexOf(s) === -1) {
          this.addLogInfo(log, this.LogInfoType.username,
              username, false, errNameCharacters);
          if (opt_callback) { opt_callback(log); }
          return false;
        }
      }

      if (opt_callback) {
        // Get the result from callback
        dataString = 'username=' + username;

        $.ajax({
          type: 'GET',
          url: '/signup',
          data: dataString,
          success: function (res) {
            var jsonObj = $.parseJSON(res);
            opt_callback(jsonObj);
          },
          error: function () {
            //Popup connection error.
            window.alert(errAjax);
          }
        });
      }

      this.addLogInfo(log, this.LogInfoType.username, username,
          true, null);
      return true;
    },

    isEmailValid: function (email, log, opt_callback) {
      var i, s, t, dataString;

      if (email === undefined || email === null) {
        throw {
          name: 'EmailValidation Error',
          message: 'Email can not be undefined or null but it is ' + email
        };
      }

      if (email.length === 0) {
        this.addLogInfo(log, this.LogInfoType.email, email,
                                false, errEmpty);
        if (opt_callback) { opt_callback(log); }
        return false;
      }

      //No space
      if (email.indexOf(' ') !== -1) {
        this.addLogInfo(log, this.LogInfoType.email, email,
                                false, errEmailSpace);
        if (opt_callback) { opt_callback(log); }
        return false;
      }

      if (email.length < 5) {
        this.addLogInfo(log, this.LogInfoType.email, email,
                                false, errEmailFormat);
        if (opt_callback) { opt_callback(log); }
        return false;
      }

      //Loop every character, no uppercase allowed.
      for (i = 0; i < email.length; i = i + 1) {
        s = email.charAt(i);
        if (upperCharacters.indexOf(s) !== -1) {
          throw {
            name: 'EmailValidation Error',
            message: 'Email can not be upper case but it is ' + email
          };
        }
      }

      if (email.indexOf('@') === -1) {
        this.addLogInfo(log, this.LogInfoType.email, email,
                                false, errEmailFormat);
        if (opt_callback) { opt_callback(log); }
        return false;
      }

      s = email.substring(email.indexOf('@') + 1);
      if (s.indexOf('.') === -1) {
        this.addLogInfo(log, this.LogInfoType.email, email,
                                false, errEmailFormat);
        if (opt_callback) { opt_callback(log); }
        return false;
      }

      // example.com, example.co.th
      if (s.indexOf('.') === 0 || s.indexOf('.') === (s.length - 1)) {
        this.addLogInfo(log, this.LogInfoType.email, email,
                                false, errEmailFormat);
        if (opt_callback) { opt_callback(log); }
        return false;
      }

      if (opt_callback) {
        dataString = 'email=' + email;

        $.ajax({
          type: 'GET',
          url: '/signup',
          data: dataString,
          success: function (res) {
            var jsonObj = $.parseJSON(res);
            opt_callback(jsonObj);
          },
          error: function () {
            //Popup connection error.
            window.alert(errAjax);
          }
        });
      }

      this.addLogInfo(log, this.LogInfoType.email, email,
                              true, null);
      return true;
    },

    isPasswordValid: function (password, log, type) {
      if (password === undefined || password === null) {
        throw {
          name: 'PasswordValidation Error',
          message: 'Password can not be undefined or null but it is ' + password
        };
      }

      if (password.length === 0) {
        this.addLogInfo(log, type, password, false, errEmpty);
        return false;
      }

      //No space
      if (password.indexOf(' ') !== -1) {
        this.addLogInfo(log, password, false, errPasswordSpace);
        return false;
      }

      if (password.length < 7) {
        this.addLogInfo(log, type, password, false, errPasswordLength);
        return false;
      }

      this.addLogInfo(log, type, password, true, null);
      return true;
    },

    isRepeatPasswordValid: function (password, repeatPassword, log) {
      if (password === undefined || password === null
          || repeatPassword === undefined || repeatPassword === null) {
        throw {
          name: 'RepeatPasswordValidation Error',
          message: 'Password and repeatPassword can not be undefined or null'
            + 'but it is ' + password + ", " + repeatPassword
        };
      }

      if (password !== repeatPassword) {
        this.addLogInfo(log, this.LogInfoType.repeatPassword,
                                repeatPassword, false, errRepeatPassword);
        return false;
      }

      this.addLogInfo(log, this.LogInfoType.repeatPassword,
                              repeatPassword, true, null);
      return true;
    }
  };
}(jQuery));
//
// End of in sync with the server class UserVerified!
//
