set app_data_path=E:\Workspace\thai-accounting\war\WEB-INF\appengine-generated
set dev_appserver_path=C:\Users\c60\Applications\eclipse\plugins\com.google.appengine.eclipse.sdkbundle_1.8.3\appengine-java-sdk-1.8.3\bin

For /f "tokens=1-2 delims=/:" %%a in ("%TIME%") do (set mytime=%%a%%b)

copy "%app_data_path%\local_db.bin" "%app_data_path%\backup\local_db_%DATE%_%mytime%.bin"

start CMD /C CALL "%dev_appserver_path%\dev_appserver.cmd" --port=8888 "e:\Workspace\thai-accounting\war"

ping 1.1.1.1 -n 1 -w 5000 > nul
start iexplore "http://localhost:8888"