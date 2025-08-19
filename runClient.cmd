@echo off
setlocal
for /f "tokens=2 delims=:." %%x in ('chcp') do set _codepage=%%x
chcp 65001>nul
cd F:\Programming\Roots-1.21\run
C:\Users\jon\.jdks\temurin-21.0.3\bin\java.exe @F:\Programming\Roots-1.21\build\moddev\clientRunClasspath.txt @F:\Programming\Roots-1.21\build\moddev\clientRunVmArgs.txt -Dfml.modFolders=roots%%%%F:\Programming\Roots-1.21\build\classes\java\main;roots%%%%F:\Programming\Roots-1.21\build\resources\main net.neoforged.devlaunch.Main @F:\Programming\Roots-1.21\build\moddev\clientRunProgramArgs.txt
if not ERRORLEVEL 0 (  echo Minecraft failed with exit code %ERRORLEVEL%  pause)
chcp %_codepage%>nul
endlocal
pause
