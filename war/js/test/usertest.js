/*global window, document, jQuery, JS_USER, QUnit, USER_OBJ */

(function ($, jsUser, QUnit, userObj) {
  'use strict';

  var changeUsernameLb, changeUsernameBtn, changeUsernameEditor,
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

    changeLangLb, changeLangBtn, changeLangEditor,
    changeLangErrLb, changeLangS, changeLangOkBtn, changeLangLoadingImg,

    deleteAccountBtn, deleteAccountEditor,
    deleteAccountPasswordErrLb, deleteAccountPasswordTB,
    deleteAccountOkBtn, deleteAccountLoadingImg;

  QUnit.module(
    'user.js',
    {
      setup: function () {

        jsUser.init();

        changeUsernameLb = $('#changeUsernameLb');
        changeUsernameBtn = $('#changeUsernameBtn');
        changeUsernameLoadingImg = $('#changeUsernameLoadingImg');
        changeUsernameEditor = $('#changeUsernameEditor');
        changeUsernameErrLb = $('#changeUsernameErrLb');
        changeUsernameTB = $('#changeUsernameTB');
        changeUsernamePasswordErrLb = $('#changeUsernamePasswordErrLb');
        changeUsernamePasswordTB = $('#changeUsernamePasswordTB');
        changeUsernameOkBtn = $('#changeUsernameOkBtn');

        changeEmailLb = $('#changeEmailLb');
        changeEmailBtn = $('#changeEmailBtn');
        changeEmailLoadingImg = $('#changeEmailLoadingImg');
        changeEmailEditor = $('#changeEmailEditor');
        changeEmailErrLb = $('#changeEmailErrLb');
        changeEmailTB = $('#changeEmailTB');
        changeEmailPasswordErrLb = $('#changeEmailPasswordErrLb');
        changeEmailPasswordTB = $('#changeEmailPasswordTB');
        changeEmailOkBtn = $('#changeEmailOkBtn');

        resendEmailConfirmBtn = $('#resendEmailConfirmBtn');

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

        changeLangLb = $('#changeLangLb');
        changeLangBtn = $('#changeLangBtn');
        changeLangLoadingImg = $('#changeLangLoadingImg');
        changeLangEditor = $('#changeLangEditor');
        changeLangErrLb = $('#changeLangErrLb');
        changeLangS = $('#changeLangS');
        changeLangOkBtn = $('#changeLangOkBtn');

        deleteAccountBtn = $('#deleteAccountBtn');
        deleteAccountLoadingImg = $('#deleteAccountLoadingImg');
        deleteAccountEditor = $('#deleteAccountEditor');
        deleteAccountPasswordErrLb = $('#deleteAccountPasswordErrLb');
        deleteAccountPasswordTB = $('#deleteAccountPasswordTB');
        deleteAccountOkBtn = $('#deleteAccountOkBtn');
      }
    }
  );

  QUnit.test('onChangeUsernameBtnClick', function () {

    jsUser.onChangeUsernameBtnClick();

    QUnit.deepEqual(changeUsernameBtn.html(), 'hide');
    QUnit.deepEqual(changeUsernameEditor.is(':visible'), true);

    jsUser.onChangeUsernameBtnClick();

    QUnit.deepEqual(changeUsernameBtn.html(), 'change');
    QUnit.deepEqual(changeUsernameEditor.is(':visible'), false);
  });

  QUnit.asyncTest('onChangeUsernameOkBtnClick1', 1, function () {
    jsUser.onChangeUsernameOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeUsernameErrLb.html(), 'This field is required.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeUsernameOkBtnClick2', 1, function () {

    changeUsernameTB.val('testtest');
    changeUsernamePasswordTB.val('wrongpassword');

    jsUser.onChangeUsernameOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeUsernamePasswordErrLb.html(),
          'Password is not correct.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeUsernameOkBtnClick3', 1, function () {

    changeUsernameTB.val('testtestnothisusername');
    changeUsernamePasswordTB.val(userObj.password);

    jsUser.onChangeUsernameOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeUsernameLb.html(),
          'testtestnothisusername');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeUsernameOkBtnClick4', 1, function () {

    changeUsernameTB.val(userObj.username);
    changeUsernamePasswordTB.val(userObj.password);

    jsUser.onChangeUsernameOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeUsernameLb.html(),
          userObj.username);
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeUsernameTBBlur', 1, function () {

    changeUsernameTB.val('wi');

    jsUser.onChangeUsernameTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(changeUsernameErrLb.html(),
          'Please use between 3 and 30 characters.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeUsernamePasswordTBBlur', 1, function () {

    changeUsernamePasswordTB.val('wi');

    jsUser.onChangeUsernamePasswordTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(changeUsernamePasswordErrLb.html(),
          'Short passwords are easy to guess. Try one with at least 7 characters.');
      QUnit.start();
    }, 2000);
  });

  QUnit.test('onChangeEmailBtnClick', function () {

    jsUser.onChangeEmailBtnClick();

    QUnit.deepEqual(changeEmailBtn.html(), 'hide');
    QUnit.deepEqual(changeEmailEditor.is(':visible'), true);

    jsUser.onChangeEmailBtnClick();

    QUnit.deepEqual(changeEmailBtn.html(), 'change');
    QUnit.deepEqual(changeEmailEditor.is(':visible'), false);
  });

  QUnit.asyncTest('onChangeEmailOkBtnClick1', 1, function () {
    jsUser.onChangeEmailOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeEmailErrLb.html(), 'This field is required.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeEmailOkBtnClick2', 1, function () {

    changeEmailTB.val('testtest@m.c');
    changeEmailPasswordTB.val('wrongpassword');

    jsUser.onChangeEmailOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeEmailPasswordErrLb.html(),
          'Password is not correct.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeEmailOkBtnClick3', 1, function () {

    changeEmailTB.val('newemail2@email.com');
    changeEmailPasswordTB.val(userObj.password);

    jsUser.onChangeEmailOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeEmailLb.html(), 'newemail2@email.com');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeEmailOkBtnClick4', 1, function () {

    changeEmailTB.val(userObj.email);
    changeEmailPasswordTB.val(userObj.password);

    jsUser.onChangeEmailOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeEmailLb.html(), userObj.email);
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeEmailTBBlur', 1, function () {

    changeEmailTB.val('wi');

    jsUser.onChangeEmailTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(changeEmailErrLb.html(),
          'Email must contain @ and domain name i.e. @example.com');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeEmailPasswordTBBlur', 1, function () {

    changeEmailPasswordTB.val('wi');

    jsUser.onChangeEmailPasswordTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(changeEmailPasswordErrLb.html(),
          'Short passwords are easy to guess. Try one with at least 7 characters.');
      QUnit.start();
    }, 2000);
  });

  QUnit.test('onChangePasswordBtnClick', function () {

    jsUser.onChangePasswordBtnClick();

    QUnit.deepEqual(changePasswordBtn.html(), 'hide');
    QUnit.deepEqual(changePasswordEditor.is(':visible'), true);

    jsUser.onChangePasswordBtnClick();

    QUnit.deepEqual(changePasswordBtn.html(), 'Change password');
    QUnit.deepEqual(changePasswordEditor.is(':visible'), false);
  });

  QUnit.asyncTest('onChangePasswordOkBtnClick1', 1, function () {
    jsUser.onChangePasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeNewPasswordErrLb.html(), 'This field is required.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangePasswordOkBtnClick2', 1, function () {

    changeNewPasswordTB.val('wrongpassword');
    changeRepeatPasswordTB.val('wrongpassword');
    changePasswordTB.val('testtest');

    jsUser.onChangePasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changePasswordErrLb.html(),
          'Password is not correct.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangePasswordOkBtnClick3', 0, function () {

    changeNewPasswordTB.val('temppassword');
    changeRepeatPasswordTB.val('temppassword');
    changePasswordTB.val(userObj.password);

    jsUser.onChangePasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangePasswordOkBtnClick4', 0, function () {

    changeNewPasswordTB.val(userObj.password);
    changeRepeatPasswordTB.val(userObj.password);
    changePasswordTB.val('temppassword');

    jsUser.onChangePasswordOkBtnClick();

    window.setTimeout(function () {
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeNewPasswordTBBlur', 1, function () {

    changeNewPasswordTB.val('wi');

    jsUser.onChangeNewPasswordTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(changeNewPasswordErrLb.html(),
          'Short passwords are easy to guess. Try one with at least 7 characters.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangeRepeatPasswordTBBlur', 1, function () {
    changeNewPasswordTB.val('witeekda89');
    changeRepeatPasswordTB.val('wife');

    jsUser.onChangeRepeatPasswordTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(changeRepeatPasswordErrLb.html(),
          'These passwords don\'t match. Please try again.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onChangePasswordTBBlur', 1, function () {

    changePasswordTB.val('wi');

    jsUser.onChangePasswordTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(changePasswordErrLb.html(),
          'Short passwords are easy to guess. Try one with at least 7 characters.');
      QUnit.start();
    }, 2000);
  });

  QUnit.test('onChangeLangBtnClick', function () {

    jsUser.onChangeLangBtnClick();

    QUnit.deepEqual(changeLangBtn.html(), 'hide');
    QUnit.deepEqual(changeLangEditor.is(':visible'), true);

    jsUser.onChangeLangBtnClick();

    QUnit.deepEqual(changeLangBtn.html(), 'change');
    QUnit.deepEqual(changeLangEditor.is(':visible'), false);
  });

  QUnit.asyncTest('onChangeLangOkBtnClick1', 1, function () {

    changeLangS.val('th');

    jsUser.onChangeLangOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(changeLangLb.html(), 'th');
      QUnit.start();
    }, 2000);
  });

  QUnit.test('onDeleteAccountBtnClick', function () {

    jsUser.onDeleteAccountBtnClick();

    QUnit.deepEqual(deleteAccountBtn.html(), 'hide');
    QUnit.deepEqual(deleteAccountEditor.is(':visible'), true);

    jsUser.onDeleteAccountBtnClick();

    QUnit.deepEqual(deleteAccountBtn.html(), 'Delete account');
    QUnit.deepEqual(deleteAccountEditor.is(':visible'), false);
  });

  QUnit.asyncTest('onDeleteAccountPasswordTBBlur', 1, function () {

    deleteAccountPasswordTB.val('wi');

    jsUser.onDeleteAccountPasswordTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(deleteAccountPasswordErrLb.html(),
          'Short passwords are easy to guess. Try one with at least 7 characters.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onDeleteAccountOkBtnClick1', 1, function () {
    jsUser.onDeleteAccountOkBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(deleteAccountPasswordErrLb.html(),
          'This field is required.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onDeleteAccountOkBtnClick2', 1, function () {

    jsUser.goHome = function () {
      QUnit.ok(true);
    };

    deleteAccountPasswordTB.val(userObj.password);

    jsUser.onDeleteAccountOkBtnClick();

    window.setTimeout(function () {
      QUnit.start();
    }, 2000);
  });
}(jQuery, JS_USER, QUnit, USER_OBJ));