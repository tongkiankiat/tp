@echo off
setlocal enableextensions
pushd %~dp0

cd ..
call gradlew clean shadowJar

cd build\libs
for /f "tokens=*" %%a in (
    'dir /b *.jar'
) do (
    set jarloc=%%a
)

REM java -jar %jarloc% < ..\..\text-ui-test\input.txt > ..\..\text-ui-test\EXPECTED.TXT

java -jar %jarloc% < ..\..\text-ui-test\testinput.txt > ..\..\text-ui-test\ACTUAL.TXT
