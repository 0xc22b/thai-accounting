/*global window, document, jQuery, JS_HOME, QUnit, USER_OBJ */

(function ($, jsHome, QUnit, userObj) {
  'use strict';

  var logInUsernameTB, logInPasswordTB, logInErrLb, logInBtn, logInLoadingImg,
    signUpUsernameTB, signUpEmailTB, signUpPasswordTB, signUpRepeatPasswordTB,
    signUpUsernameErrLb, signUpEmailErrLb, signUpPasswordErrLb,
    signUpRepeatPasswordErrLb, signUpBtn, signUpLoadingImg;

  QUnit.module(
    'home.js',
    {
      setup: function () {
        jsHome.onSuccessLogIn = function () {
          QUnit.ok(true);
        };
        jsHome.init();

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
      }
    }
  );

  QUnit.asyncTest('onSignUpBtnClick1', 5, function () {

    // Try to random username and email
    userObj.username = 'testusername' + (Math.floor(Math.random() * 100000));
    userObj.email = userObj.username + '@email.com';
    userObj.password = 'asdfjkl;';

    signUpUsernameTB.val(userObj.username);
    signUpEmailTB.val(userObj.email);
    signUpPasswordTB.val(userObj.password);
    signUpRepeatPasswordTB.val(userObj.password);

    jsHome.onSignUpBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(signUpUsernameErrLb.html(),
          '&nbsp;');
      QUnit.deepEqual(signUpEmailErrLb.html(),
          '&nbsp;');
      QUnit.deepEqual(signUpPasswordErrLb.html(),
          '&nbsp;');
      QUnit.deepEqual(signUpRepeatPasswordErrLb.html(),
          '&nbsp;');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onSignUpBtnClick2', 4, function () {

    signUpUsernameTB.val('wt');
    signUpEmailTB.val('wit@e');
    signUpPasswordTB.val('asdf');
    signUpRepeatPasswordTB.val('asjkl;');

    jsHome.onSignUpBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(signUpUsernameErrLb.html(),
          'Please use between 3 and 30 characters.');
      QUnit.deepEqual(signUpEmailErrLb.html(),
          'Email must contain @ and domain name i.e. @example.com');
      QUnit.deepEqual(signUpPasswordErrLb.html(),
          'Short passwords are easy to guess. Try one with at least 7 characters.');
      QUnit.deepEqual(signUpRepeatPasswordErrLb.html(),
          'These passwords don\'t match. Please try again.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onSignUpBtnClick3', 4, function () {

    signUpUsernameTB.val(userObj.username);
    signUpEmailTB.val(userObj.email);
    signUpPasswordTB.val('asdfjkl;');
    signUpRepeatPasswordTB.val('asdfjkl;');

    jsHome.onSignUpBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(signUpUsernameErrLb.html(),
          'Someone already has that username. Please try another.');
      QUnit.deepEqual(signUpEmailErrLb.html(),
          '&nbsp;');
      QUnit.deepEqual(signUpPasswordErrLb.html(),
          '&nbsp;');
      QUnit.deepEqual(signUpRepeatPasswordErrLb.html(),
          '&nbsp;');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onSignUpBtnClick4', 4, function () {

    signUpUsernameTB.val(userObj.username + 'test123');
    signUpEmailTB.val(userObj.email);
    signUpPasswordTB.val('asdfjkl;');
    signUpRepeatPasswordTB.val('asdfjkl;');

    jsHome.onSignUpBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(signUpUsernameErrLb.html(),
          '&nbsp;');
      QUnit.deepEqual(signUpEmailErrLb.html(),
          'Someone already has that email. Please try another.');
      QUnit.deepEqual(signUpPasswordErrLb.html(),
          '&nbsp;');
      QUnit.deepEqual(signUpRepeatPasswordErrLb.html(),
          '&nbsp;');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onSignUpUsernameTBBlur', 1, function () {

    signUpUsernameTB.val('wi');

    jsHome.onSignUpUsernameTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(signUpUsernameErrLb.html(),
          'Please use between 3 and 30 characters.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onSignUpEmailTBBlur', 1, function () {

    signUpEmailTB.val('wi');

    jsHome.onSignUpEmailTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(signUpEmailErrLb.html(),
          'Email must contain @ and domain name i.e. @example.com');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onSignUpPasswordTBBlur', 1, function () {

    signUpPasswordTB.val('wi');

    jsHome.onSignUpPasswordTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(signUpPasswordErrLb.html(),
          'Short passwords are easy to guess. Try one with at least 7 characters.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onSignUpRepeatPasswordTBBlur', 1, function () {

    signUpPasswordTB.val('wifjik');
    signUpRepeatPasswordTB.val('wi');

    jsHome.onSignUpRepeatPasswordTBBlur();

    window.setTimeout(function () {
      QUnit.deepEqual(signUpRepeatPasswordErrLb.html(),
          'These passwords don\'t match. Please try again.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onLogInBtnClick1', 1, function () {

    jsHome.onLogInBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(logInErrLb.html(),
          'Your username or password are not correct.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onLogInBtnClick2', 1, function () {

    logInUsernameTB.val('wite');
    logInPasswordTB.val('asdfg');

    jsHome.onLogInBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(logInErrLb.html(),
          'Your username or password are not correct.');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onLogInBtnClick3', 2, function () {

    logInUsernameTB.val(userObj.username);
    logInPasswordTB.val('asdfjkl;');

    jsHome.onLogInBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(logInErrLb.html(),
          '&nbsp;');
      QUnit.start();
    }, 2000);
  });

  QUnit.asyncTest('onLogInBtnClick4', 2, function () {

    logInUsernameTB.val(userObj.email);
    logInPasswordTB.val('asdfjkl;');

    jsHome.onLogInBtnClick();

    window.setTimeout(function () {
      QUnit.deepEqual(logInErrLb.html(),
          '&nbsp;');
      QUnit.start();
    }, 2000);
  });
}(jQuery, JS_HOME, QUnit, USER_OBJ));