/*global window, document, jQuery, USER_VERIFIER, QUnit */
(function ($, UserVerifier, QUnit) {
  'use strict';

  QUnit.module('userverifier.js');

  QUnit.test('addLogInfo', function () {
    var log = [];
    UserVerifier.addLogInfo(log, 'test', 'value', true, 'msg');
    UserVerifier.addLogInfo(log, 'test1', 'value1', false, 'msg1');
    UserVerifier.addLogInfo(log, 'test2', 'value2', false, 'msg2');

    QUnit.deepEqual(log[0].type, 'test');
    QUnit.deepEqual(log[0].value, 'value');
    QUnit.deepEqual(log[0].isValid, true);
    QUnit.deepEqual(log[0].msg, 'msg');

    QUnit.deepEqual(log[1].type, 'test1');
    QUnit.deepEqual(log[1].value, 'value1');
    QUnit.deepEqual(log[1].isValid, false);
    QUnit.deepEqual(log[1].msg, 'msg1');

    QUnit.deepEqual(log[2].type, 'test2');
    QUnit.deepEqual(log[2].value, 'value2');
    QUnit.deepEqual(log[2].isValid, false);
    QUnit.deepEqual(log[2].msg, 'msg2');
  });

  QUnit.test('getLogInfo', function () {
    var log = [], logInfo;
    UserVerifier.addLogInfo(log, 'test', 'value', true, 'msg');
    UserVerifier.addLogInfo(log, 'test1', 'value1', false, 'msg1');
    UserVerifier.addLogInfo(log, 'test2', 'value2', true, 'msg2');
    UserVerifier.addLogInfo(log, 'test2', 'value3', true, 'msg3');
    UserVerifier.addLogInfo(log, 'test2', 'value4', false, 'msg4');

    logInfo = UserVerifier.getLogInfo(log, 'test');
    QUnit.deepEqual(logInfo.value, 'value');
    QUnit.deepEqual(logInfo.isValid, true);
    QUnit.deepEqual(logInfo.msg, 'msg');

    logInfo = UserVerifier.getLogInfo(log, 'test2', false);
    QUnit.deepEqual(logInfo.value, 'value4');
    QUnit.deepEqual(logInfo.isValid, false);
    QUnit.deepEqual(logInfo.msg, 'msg4');
  });

  QUnit.test('setErrLb', function () {
    var $errLb = $("#errLb"), log = [];

    UserVerifier.addLogInfo(log, 'test', 'value', true, 'msg');
    UserVerifier.setErrLb($errLb, log, 'test', false);
    QUnit.deepEqual($errLb.html(), '&nbsp;');

    UserVerifier.setErrLb($errLb, log, 'test');
    QUnit.deepEqual($errLb.html(), 'msg');
  });

  QUnit.test('isUsernameValid', function () {
    var log = [], logInfo;
    QUnit.deepEqual(UserVerifier.isUsernameValid('', log), false);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.username);
    QUnit.deepEqual(logInfo.value, '');
    QUnit.deepEqual(logInfo.isValid, false);
    QUnit.deepEqual(logInfo.msg, 'This field is required.');

    log = [];
    QUnit.deepEqual(UserVerifier.isUsernameValid('wi', log), false);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.username);
    QUnit.deepEqual(logInfo.value, 'wi');
    QUnit.deepEqual(logInfo.isValid, false);
    QUnit.deepEqual(logInfo.msg, 'Please use between 3 and 30 characters.');

    log = [];
    QUnit.deepEqual(UserVerifier.isUsernameValid('wit', log), true);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.username);
    QUnit.deepEqual(logInfo.value, 'wit');
    QUnit.deepEqual(logInfo.isValid, true);
    QUnit.deepEqual(logInfo.msg, null);
  });

  QUnit.asyncTest('isUsernameValid', 3, function () {
    var log = [], logInfo;
    UserVerifier.isUsernameValid('nothisusername', log, function (log) {
      logInfo = UserVerifier.getLogInfo(log,
                                        UserVerifier.LogInfoType.username);
      QUnit.deepEqual(logInfo.value, 'nothisusername');
      QUnit.deepEqual(logInfo.isValid, true);
      QUnit.deepEqual(logInfo.msg, undefined);
      QUnit.start();
    });
  });

  QUnit.test('isEmailValid', function () {
    var log = [], logInfo;
    QUnit.deepEqual(UserVerifier.isEmailValid('', log), false);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
    QUnit.deepEqual(logInfo.value, '');
    QUnit.deepEqual(logInfo.isValid, false);
    QUnit.deepEqual(logInfo.msg, 'This field is required.');

    log = [];
    QUnit.deepEqual(UserVerifier.isEmailValid('wi', log), false);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
    QUnit.deepEqual(logInfo.value, 'wi');
    QUnit.deepEqual(logInfo.isValid, false);
    QUnit.deepEqual(logInfo.msg,
                    'Email must contain @ and domain name i.e. @example.com');

    log = [];
    QUnit.deepEqual(UserVerifier.isEmailValid('wit.', log), false);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
    QUnit.deepEqual(logInfo.value, 'wit.');
    QUnit.deepEqual(logInfo.isValid, false);
    QUnit.deepEqual(logInfo.msg,
                    'Email must contain @ and domain name i.e. @example.com');

    log = [];
    QUnit.deepEqual(UserVerifier.isEmailValid('wit@t', log), false);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
    QUnit.deepEqual(logInfo.value, 'wit@t');
    QUnit.deepEqual(logInfo.isValid, false);
    QUnit.deepEqual(logInfo.msg,
                    'Email must contain @ and domain name i.e. @example.com');

    log = [];
    QUnit.deepEqual(UserVerifier.isEmailValid('wit@t.', log), false);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
    QUnit.deepEqual(logInfo.value, 'wit@t.');
    QUnit.deepEqual(logInfo.isValid, false);
    QUnit.deepEqual(logInfo.msg,
                    'Email must contain @ and domain name i.e. @example.com');

    log = [];
    QUnit.deepEqual(UserVerifier.isEmailValid('w.i-t@t.c', log), true);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
    QUnit.deepEqual(logInfo.value, 'w.i-t@t.c');
    QUnit.deepEqual(logInfo.isValid, true);
    QUnit.deepEqual(logInfo.msg, null);

    log = [];
    QUnit.deepEqual(UserVerifier.isEmailValid('w.i-t@t.c.t', log), true);
    logInfo = UserVerifier.getLogInfo(log, UserVerifier.LogInfoType.email);
    QUnit.deepEqual(logInfo.value, 'w.i-t@t.c.t');
    QUnit.deepEqual(logInfo.isValid, true);
    QUnit.deepEqual(logInfo.msg, null);
  });

  QUnit.asyncTest('isEmailValid', 3, function () {
    var log = [], logInfo;
    UserVerifier.isEmailValid('newemail@email.com', log, function (log) {
      logInfo = UserVerifier.getLogInfo(log,
                                        UserVerifier.LogInfoType.email);
      QUnit.deepEqual(logInfo.value, 'newemail@email.com');
      QUnit.deepEqual(logInfo.isValid, true);
      QUnit.deepEqual(logInfo.msg, undefined);
      QUnit.start();
    });
  });

  QUnit.test('isPasswordValid', function () {
    var log = [];
    QUnit.deepEqual(UserVerifier.isPasswordValid('', log,
        UserVerifier.LogInfoType.password), false);
    QUnit.deepEqual(UserVerifier.isPasswordValid('sdfiif', log,
        UserVerifier.LogInfoType.password), false);
    QUnit.deepEqual(UserVerifier.isPasswordValid('eikadife', log,
        UserVerifier.LogInfoType.password), true);
  });

  QUnit.test('isRepeatPasswordValid', function () {
    var log = [];
    QUnit.deepEqual(UserVerifier.isRepeatPasswordValid('', 'i', log), false);
    QUnit.deepEqual(UserVerifier.isRepeatPasswordValid('s', '', log), false);
    QUnit.deepEqual(UserVerifier.isRepeatPasswordValid('eikadife', 'kifiksll',
                                                       log), false);
    QUnit.deepEqual(UserVerifier.isRepeatPasswordValid('eikadife', 'eikadife',
                                                       log), true);
  });

}(jQuery, USER_VERIFIER, QUnit));