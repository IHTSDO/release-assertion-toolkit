@echo off
set ANTPATH=c:\apache-ant-1.6.2
set JAVA_HOME=.\jdk1.6.0_20
set PATH=%JAVA_HOME%\bin;%PATH%
set CLASSPATH=.;%ANTPATH%\lib\mail.jar;%ANTPATH%\lib\ant-javamail.jar;%ANTPATH%\lib\activation.jar;%ANTPATH%\lib\ant.jar;%ANTPATH%\lib\ant-launcher.jar;%ANTPATH%\lib\jaxp.jar;%ANTPATH%\lib\crimson.jar;%ANTPATH%\lib\optional.jar;%ANTPATH%\lib\NetComponents.jar;%CLASSPATH%

echo Release Files QA  Report
java -Dant.home=%ANTPATH% org.apache.tools.ant.launch.Launcher -buildfile build.xml