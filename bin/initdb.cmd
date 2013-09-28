mysql -uroot -e 'DROP DATABASE IF EXISTS thai_accounting;'
mysql -uroot -e 'CREATE DATABASE thai_accounting;'
mysql -uroot < ./bin/init.sql
