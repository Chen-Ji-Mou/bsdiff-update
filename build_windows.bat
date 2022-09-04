@echo off

set ROOT=%~dp0
set PATCH="%ROOT%outputs\patch"

rmdir /s /q "%ROOT%outputs"
mkdir "%ROOT%outputs"

cd "%ROOT%original_app"
call "%ROOT%original_app\gradlew.bat" --parallel assemblerelease

cd "%ROOT%new_app"
call "%ROOT%new_app\gradlew.bat" --parallel assemblerelease

cd "%ROOT%bsdiff_windows"
bsdiff "%ROOT%outputs\original-app-release.apk" "%ROOT%outputs\new-app-release.apk" %PATCH%

if exist "%ROOT%ftp.bat" del "%ROOT%ftp.bat"

echo open 43.143.169.6 > "%ROOT%ftp.bat"
echo user ubuntu Chenjimou1* >> "%ROOT%ftp.bat"
echo quote pasv >> "%ROOT%ftp.bat"
echo delete patch >> "%ROOT%ftp.bat"
echo put %PATCH% >> "%ROOT%ftp.bat"
echo quit >> "%ROOT%ftp.bat"
echo exit >> "%ROOT%ftp.bat"

ftp -n -s:"%ROOT%ftp.bat"

del "%ROOT%ftp.bat"

adb install -r "%ROOT%outputs\original-app-release.apk"

exit