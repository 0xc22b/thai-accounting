mysql -uroot -proot -e "DROP DATABASE IF EXISTS thai_accounting;"
mysql -uroot -proot -e "CREATE DATABASE thai_accounting;"
mysql -uroot -proot < ./bin/init_mysql.txt