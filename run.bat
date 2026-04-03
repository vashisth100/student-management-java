@echo off
echo ========================================
echo   Student Management System - VIT
echo ========================================

:: Check if JAR already exists
if exist "target\StudentManagementSystem-1.0-jar-with-dependencies.jar" (
    echo JAR found. Launching app...
    goto RUN
)

:: JAR not found - need to build
echo JAR not found. Building project...
"C:\Users\HP\Downloads\apache-maven-3.9.14-bin\apache-maven-3.9.14\bin\mvn.cmd" package -q

if %errorlevel% neq 0 (
    echo.
    echo Build failed. Trying clean build...
    rmdir /s /q target 2>nul
    "C:\Users\HP\Downloads\apache-maven-3.9.14-bin\apache-maven-3.9.14\bin\mvn.cmd" package -q
)

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Build failed. Please check the errors above.
    pause
    exit /b 1
)

echo Build successful!

:RUN
echo Starting application...
java -jar "target\StudentManagementSystem-1.0-jar-with-dependencies.jar"
pause
