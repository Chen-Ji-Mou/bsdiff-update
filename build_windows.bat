set ROOT=%~dp0

rmdir /s /q %ROOT%\outputs
mkdir %ROOT%\outputs

cd %ROOT%\original_app
call %ROOT%\original_app\gradlew.bat --parallel assembleRelease

cd %ROOT%\new_app
call %ROOT%\new_app\gradlew.bat --parallel assembleRelease

cd %ROOT%\bsdiff
bsdiff %ROOT%\outputs\original-app-release.apk %ROOT%\outputs\new-app-release.apk %ROOT%\outputs\patch