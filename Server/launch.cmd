@echo off
set SELENIUM_VERSION=2.44.0
set SELENIUM_EXE=selenium-server-standalone-%SELENIUM_VERSION%.jar
set CHROME_DRIVER=drivers/chromedriver.exe
if "%PROCESSOR_ARCHITECTURE%"=="x86" (
  set IE_DRIVER=drivers/IEDriverServer_x86.exe
) else (
  set IE_DRIVER=drivers/IEDriverServer_x64.exe
)

start /min "Selenium Hub" java -jar "%SELENIUM_EXE%" -role hub
echo launching selenium's hub...
sleep 3
start /min "Selenium Node" java -jar "%SELENIUM_EXE%" -role node -nodeConfig node.json -Dwebdriver.ie.driver="%IE_DRIVER%" -Dwebdriver.chrome.driver="%CHROME_DRIVER%"
