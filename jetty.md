# Install
* Download from 
* Unzip and put the folder at C:\Users\C60\Applications

# Run web apps in JETTY_HOME\webapps\
* Command line to JETTY_HOME where there is a file named start.jar
* java -jar start.jar

# Development
* If using GWT, alreay run with Jetty
* When done development, copy war folder to JETTY_HOME\webapps\ and change its name to 'root'

# Memory
* java.lang.OutOfMemoryError: Java heap space, increase memory with java -Xmx1024M -jar start.jar

# Run Jetty as a Windows service
* src: http://stackoverflow.com/questions/2094429/running-jetty-7-as-windows-service