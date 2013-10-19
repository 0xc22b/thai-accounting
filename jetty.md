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
* Download Apache prunsrv.exe from http://commons.apache.org/proper/commons-daemon/binaries.html
* Place it in C:\Users\C60\Applications\jetty-distribution-8.1.13.v20130916
* Create a file called install_jetty_service.bat in the same folder with following command:

        prunsrv install JettyService --DisplayName="Jetty8 Service" --JavaHome="C:\Program Files\Java\jdk1.6.0_38" --Startup=auto ++JvmOptions=-Xmx1024M ++JvmOptions=-Djetty.home=C:\Users\C60\Applications\jetty-distribution-8.1.13.v20130916 ++JvmOptions=-Djetty.logs=C:\Users\C60\Applications\jetty-distribution-8.1.13.v20130916\logs\ --Install=C:\Users\C60\Applications\jetty-distribution-8.1.13.v20130916\prunsrv.exe ++StartMode=java --StopMode=java --Classpath=C:\Users\C60\Applications\jetty-distribution-8.1.13.v20130916\start.jar --StartClass=org.eclipse.jetty.start.Main --StopClass=org.eclipse.jetty.start.Main --StopParams=--stop

* Install the service: cmd -> go to the folder -> Run install_jetty_service.bat
* Start the service: Control panel -> Administrative Tools -> Services -> Find service name Jetty8 Service -> Start
* Stop and remove service at command line: prunsrv //DS//[Service name]
* src: http://stackoverflow.com/questions/2094429/running-jetty-7-as-windows-service
       http://www.davekb.com/browse_programming_tips:run_jetty8_as_windows_service:txt
       https://www.assembla.com/wiki/show/liftweb/configuring_jetty_as_a_windows_service
       http://commons.apache.org/proper/commons-daemon/procrun.html