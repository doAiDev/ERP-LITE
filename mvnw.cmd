@REM Maven Wrapper for Windows
@IF "%MAVEN_BATCH_ECHO%"=="on" echo %MAVEN_BATCH_ECHO%
@SET ERROR_CODE=0
@IF "%OS%"=="Windows_NT" @SETLOCAL

@IF NOT "%JAVA_HOME%"=="" GOTO OkJHome
@ECHO Error: JAVA_HOME not found. Please set JAVA_HOME. >&2
@GOTO error

:OkJHome
@IF EXIST "%JAVA_HOME%\bin\java.exe" GOTO init
@ECHO Error: JAVA_HOME is set to an invalid directory: %JAVA_HOME% >&2
@GOTO error

:init
set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir
set EXEC_DIR=%CD%
set WDIR=%EXEC_DIR%

:findBaseDir
IF EXIST "%WDIR%\.mvn" goto baseDirFound
cd ..
IF "%WDIR%"=="%CD%" goto baseDirNotFound
set WDIR=%CD%
goto findBaseDir

:baseDirNotFound
set MAVEN_PROJECTBASEDIR=%EXEC_DIR%
goto endDetectBaseDir

:baseDirFound
set MAVEN_PROJECTBASEDIR=%WDIR%
cd "%EXEC_DIR%"

:endDetectBaseDir

SET MAVEN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
set WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    IF "%%A"=="wrapperUrl" SET WRAPPER_URL=%%B
)

if exist %WRAPPER_JAR% (
    echo Found %WRAPPER_JAR%
) else (
    echo Downloading Maven Wrapper from: %WRAPPER_URL%
    powershell -Command "&{$webclient = new-object System.Net.WebClient; [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $webclient.DownloadFile('%WRAPPER_URL%', '%WRAPPER_JAR%')}"
    echo Finished downloading Maven Wrapper
)

%MAVEN_JAVA_EXE% ^  
  %MAVEN_OPTS% ^  
  -classpath %WRAPPER_JAR% ^  
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^  
  %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
if ERRORLEVEL 1 goto error
goto end

:error
SET ERROR_CODE=1

:end
@IF "%MAVEN_BATCH_PAUSE%"=="on" PAUSE
@ENDLOCAL & SET ERROR_CODE=%ERROR_CODE%
EXIT /B %ERROR_CODE%
