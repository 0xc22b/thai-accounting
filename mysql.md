* Installation
** MySQL version 5.6.12 (bottled)
*** Mac OSX
    - brew install mysql (mysql_install_db is automatically run)
*** Windows 7
    - Installer(.msi) (mysql_install_db is automatically run)

** MySQL Connector/J 5.1.26
   - Download from http://dev.mysql.com/downloads/connector/j/
   - Put mysql-connector-java-5.1.26-bin.jar in war/WEB-INF/lib/

* Set up hostname, port
  - Defaults: hostname=localhost, port=3306

* Set up username, and password in Grant tables (mysql.user)
  - Defaults: username=root, password is empty

* Start and stop MySQL Server (mysqld, The SQL daemon)
** Mac OSX
   - mysql.server start (A server startup script)
   - mysql.server stop
** Windows 7
   - Config to automatically start at log in as Windows service.

* MySQLAdmin
  - Check status: mysqladmin status
  - Check version: mysqladmin version

* MySQL command-line tool
  - Connect to the SQL server as root user: mysql -uroot
  - Show databases: SHOW DATABASES;
  - Create a database: CREATE DATABASE [database name];
  - Select a database: USE [database name];
  - Delete a database: DROP IF EXISTS [database name];
  - Show tables: SHOW TABLES
  - Create a table: 
  - DESCRIBE [table name]: 
  - Delete a table: DROP TABLE IF EXISTS [table name];
  - Quite: quite

* a script file
  - ./bin/initdb.cmd

* Project setup
** [No need!] Google plugin for Eclipse
  - thai-accounting -> properties -> Google -> App Engine
  - Enable Google Cloud SQL
  - Use MySQL instance
  - configure
    + Hostname : localhost
    + Database name : thai_accounting
    + Port number : 3306
    + Database username : root
    + Database password : [empty]
    + Path to MySQL JDBC JAR : war/WEB-INF/lib/mysql-connector-java-5.1.26.jar
** Classpath
   - With Class.forName(), a class will be look for at run time so make sure it's in the classpath.
     + Put mysql-connector-java-5.1.26-bin.jar in war/WEB-INF/lib/
     + [Eclipse only] At Run Configurations, Classpath -> Add JARs...

* Backup
  - MySQLDump & MYSQLImport
