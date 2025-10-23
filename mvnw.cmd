@echo off
setlocal

set MAVEN_CMD_LINE_ARGS=%*

if not "%MAVEN_SKIP_RC%" == "" goto skipRcPre
if exist "%HOME%\mavenrc_pre.bat" call "%HOME%\mavenrc_pre.bat"
if exist "%HOME%\mavenrc_pre.cmd" call "%HOME%\mavenrc_pre.cmd"
:skipRcPre

set ERROR_CODE=0

if not "%JAVA_HOME%" == "" goto OkJHome

for /f "usebackq tokens=*" %%i in (`java -version 2^>^&1`) do set JAVAINSTALLED=%%i
if "%JAVAINSTALLED%" == "" goto MissingJava

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkMHome

:MissingJava
echo The JAVA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
echo NB: JAVA_HOME should point to a JDK not a JRE
goto error

:chkMHome
if not "%M2_HOME%"=="" goto valMHome

if not "%MAVEN_HOME%"=="" goto valMHome

set "MAVEN_PROJECTBASEDIR=%WDIR%"
set EXEC_DIR=%CD%
goto findBaseDir

:valMHome
if not exist "%M2_HOME%\bin\mvn.bat" if not exist "%MAVEN_HOME%\bin\mvn.bat" goto MissingMavenHome
if not "%M2_HOME%"=="" set "MVN_HOME=%M2_HOME%"
if not "%MVN_HOME%"=="" set "M2_HOME=%MVN_HOME%"
goto findBaseDir

:MissingMavenHome
echo The M2_HOME or MAVEN_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto error

:findBaseDir
if exist "%WDIR%\.mvn\wrapper\maven-wrapper.jar" set "MVNW_REPOURL_CONFIG=%WDIR%\.mvn\wrapper\maven-wrapper.properties"
if exist "%WDIR%\.mvn\wrapper\maven-wrapper.jar" goto run

cd ..
set "WDIR=%CD%"
goto findBaseDir

:run
if exist "%MVNW_REPOURL_CONFIG%" (
    for /f "usebackq delims=" %%a in ("%MVNW_REPOURL_CONFIG%") do set "MVNW_REPOURL=%%a"
)
if not "%MVNW_REPOURL%" == "" (
    set "MVNW_REPO_PATTERN=%MVNW_REPOURL:/org/apache/maven/wrapper/maven-wrapper/="
    for %%i in (%MVNW_REPO_PATTERN%) do set "MVNW_REPO_PARENT=%%~dpi"
    set "MVNW_REPO=%MVNW_REPO_PARENT:~0,-1%"
    if "%MVNW_REPO%" == "" set "MVNW_REPO=%MVNW_REPOURL%"
)
if "%MVNW_REPO%" == "" set MVNW_REPO=%MVNW_REPOURL_DEFAULT%
if not exist "%MVNW_REPO%" set MVNW_REPO=https://repo1.maven.org/maven2

set DOWNLOAD_URL="%MVNW_REPO%/org/apache/maven/wrapper/maven-wrapper/3.1.0/maven-wrapper-3.1.0.jar"

FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%WDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)

if not exist "%WDIR%\.mvn\wrapper\maven-wrapper.jar" (
    if not "%MVNW_VERBOSE%" == "false" (
        echo Couldn't find %WDIR%\.mvn\wrapper\maven-wrapper.jar, downloading it ...
        echo Downloading from: %DOWNLOAD_URL%
    )

    powershell -Command "&{"^
		"$webclient = new-object System.Net.WebClient;"^
		"if (-not ([string]::IsNullOrEmpty('%MVNW_USERNAME%') -and [string]::IsNullOrEmpty('%MVNW_PASSWORD%'))) {"^
		"$webclient.Credentials = new-object System.Net.NetworkCredential('%MVNW_USERNAME%', '%MVNW_PASSWORD%');"^
		"}"^
		"[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $webclient.DownloadFile('%DOWNLOAD_URL%', '%WDIR%\.mvn\wrapper\maven-wrapper.jar')"^
		"}"
    if "%MVNW_VERBOSE%" == "false" (
        echo Finished downloading %WDIR%\.mvn\wrapper\maven-wrapper.jar
    )
)
@setlocal

set MAVEN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"
set WRAPPER_JAR="%WDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

set WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.1.0/maven-wrapper-3.1.0.jar"

FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%WDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    IF "%%A"=="wrapperUrl" SET WRAPPER_URL=%%B
)

@setlocal EnableExtensions EnableDelayedExpansion
set MAVEN_CMD_LINE_ARGS=%*

%MAVEN_JAVA_EXE% %JVM_CONFIG_MAVEN_PROPS% ^
  -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" ^
  -Dmaven.home="%M2_HOME%" ^
  -Dclassworlds.conf="%M2_HOME%\bin\m2.conf" ^
  -Dmaven.ext.class.path="%MAVEN_HOME%\lib\ext" ^
  -classpath %WRAPPER_JAR% ^
  "-Dmaven.wrapper.properties=file:///%WDIR%/.mvn/wrapper/maven-wrapper.properties" ^
  %WRAPPER_LAUNCHER% %MAVEN_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

if not "%MAVEN_SKIP_RC%" == "" goto skipRcPost
if exist "%HOME%\mavenrc_post.bat" call "%HOME%\mavenrc_post.bat"
if exist "%HOME%\mavenrc_post.cmd" call "%HOME%\mavenrc_post.cmd"
:skipRcPost

@exit /B %ERROR_CODE%