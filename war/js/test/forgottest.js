/*global window, document, jQuery, JS_FORGOT, QUnit, USER_OBJ */

(function ($, jsForgot, QUnit, userObj) {
  'use strict';

  var formPanel, resultLb,
    usernameOrEmailErrLb, usernameOrEmailTB,
    newPasswordErrLb, newPasswordTB, repeatPasswordErrLb, repeatPasswordTB,
    okBtn, loadingImg;

  QUnit.module(
    'forgot.js: forgot password',
    {
      setup: function () {
        jsForgot.getUrlQuery = function () {
          return {};
        };

        jsForgot.init();

        formPanel = $('#formPanel');
        resultLb = $('#resultLb');

        usernameOrEmailErrLb = $('#usernameOrEmailErrLb');
        usernameOrEmailTB = $('#usernameOrEmailTB');
      }
    }
  );

  QUnit.asyncTest('onForgotPasswordOkBtnClick1', 1, function () {

    jsForgot.onForgotPasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(usernameOrEmailErrLb.html(),
          'This field is required.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onForgotPasswordOkBtnClick2', 1, function () {

    usernameOrEmailTB.val('nothisusername');

    jsForgot.onForgotPasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(usernameOrEmailErrLb.html(),
          'This username or email is not available.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onForgotPasswordOkBtnClick3', 4, function () {

    usernameOrEmailTB.val(userObj.username);

    jsForgot.onForgotPasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(usernameOrEmailErrLb.html(),
          '&nbsp;');
      QUnit.deepEqual(resultLb.html(),
          'Please check your email to accept confirmation link for changing password.');
      QUnit.deepEqual(resultLb.is(':visible'), true);
      QUnit.deepEqual(formPanel.is(':visible'), false);
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onForgotPasswordOkBtnClick4', 4, function () {

    usernameOrEmailTB.val(userObj.email);

    jsForgot.onForgotPasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(usernameOrEmailErrLb.html(),
          '&nbsp;');
      QUnit.deepEqual(resultLb.html(),
          'Please check your email to accept confirmation link for changing password.');
      QUnit.deepEqual(resultLb.is(':visible'), true);
      QUnit.deepEqual(formPanel.is(':visible'), false);
      QUnit.start();
    }, 2000);
  });

  QUnit.test('onUsernameOrEmailTBBlur', function () {

    usernameOrEmailTB.val('te@email.');

    jsForgot.onUsernameOrEmailTBBlur();

    QUnit.deepEqual(usernameOrEmailErrLb.html(),
          'Email must contain @ and domain name i.e. @example.com');
  });

  QUnit.module(
    'forgot.js: reset password',
    {
      setup: function () {
        jsForgot.getUrlQuery = function () {
          return {ssid: 'asdnnwilskldd', fid: 'diIkannsw_dk'};
        };

        jsForgot.init();

        formPanel = $('#formPanel');
        resultLb = $('#resultLb');

        newPasswordErrLb = $('#newPasswordErrLb');
        newPasswordTB = $('#newPasswordTB');
        repeatPasswordErrLb = $('#repeatPasswordErrLb');
        repeatPasswordTB = $('#repeatPasswordTB');
      }
    }
  );

  QUnit.asyncTest('onResetPasswordOkBtnClick1', 3, function () {

    newPasswordTB.val('asdfjkl;123');
    repeatPasswordTB.val('asdfjkl;123');

    jsForgot.onResetPasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(newPasswordErrLb.html(),
          'Could not find your account. Please request to reset password again.');
      QUnit.deepEqual(resultLb.is(':visible'), false);
      QUnit.deepEqual(formPanel.is(':visible'), true);
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onResetPasswordOkBtnClick2', 3, function () {

    newPasswordTB.val('asdfjkl;123');
    repeatPasswordTB.val('asdfjkl;');

    jsForgot.onResetPasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(repeatPasswordErrLb.html(),
          'These passwords don\'t match. Please try again.');
      QUnit.deepEqual(resultLb.is(':visible'), false);
      QUnit.deepEqual(formPanel.is(':visible'), true);
      QUnit.start();
    }, 2000);
  });

  QUnit.test('onNewPasswordTBBlur', function () {

    newPasswordTB.val('ads');

    jsForgot.onNewPasswordTBBlur();

    QUnit.deepEqual(newPasswordErrLb.html(),
          'Short passwords are easy to guess. Try one with at least 7 characters.');
  });

  QUnit.test('onRepeatPasswordTBBlur', function () {

    newPasswordTB.val('adsfjkl;23');
    repeatPasswordTB.val('adsd');

    jsForgot.onRepeatPasswordTBBlur();

    QUnit.deepEqual(repeatPasswordErrLb.html(),
          'These passwords don\'t match. Please try again.');
  });
}(jQuery, JS_FORGOT, QUnit, USER_OBJ));