@echo off
setlocal

if "%JAVA_HOME%"=="" (
    echo [ERROR] JAVA_HOME이 설정되지 않았습니다.
    echo JAVA_HOME 환경변수를 C:\opt\jdk\jdk-11.0.24+8 등으로 설정해 주세요.
    exit /b 1
)

set MAVEN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"
set MAVEN_PROJECTBASEDIR=%~dp0
if "%MAVEN_PROJECTBASEDIR:~-1%"=="\" set MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

if not exist %WRAPPER_JAR% (
    echo Maven Wrapper 다운로드 중...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('%WRAPPER_URL%', %WRAPPER_JAR%)"
)

%MAVEN_JAVA_EXE% -classpath %WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*

endlocal
