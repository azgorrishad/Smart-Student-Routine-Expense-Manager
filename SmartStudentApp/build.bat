@echo off
echo =====================================================
echo   Smart Student Routine and Expense Manager
echo =====================================================
echo.

:: create output and lib folders if not there yet
if not exist "out" mkdir out
if not exist "lib" mkdir lib

:: download SQLite JDBC driver if not already downloaded
if not exist "lib\sqlite-jdbc.jar" (
    echo Downloading SQLite JDBC driver... (one-time download)
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar' -OutFile 'lib\sqlite-jdbc.jar'"
    if exist "lib\sqlite-jdbc.jar" (
        echo Download successful.
    ) else (
        echo Download failed. Please download manually:
        echo https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar
        echo Save it as: lib\sqlite-jdbc.jar
        pause
        exit /b 1
    )
)

echo.
echo Compiling all Java source files...
echo.

javac -cp "lib\sqlite-jdbc.jar" -d "out" ^
    src\Main.java ^
    src\database\DatabaseConnection.java ^
    src\utils\PasswordUtil.java ^
    src\utils\ValidationUtil.java ^
    src\model\User.java ^
    src\model\Student.java ^
    src\model\Routine.java ^
    src\model\Expense.java ^
    src\model\Budget.java ^
    src\model\Reminder.java ^
    src\service\ReportGenerator.java ^
    src\service\ExpenseReport.java ^
    src\dao\UserDAO.java ^
    src\dao\RoutineDAO.java ^
    src\dao\ExpenseDAO.java ^
    src\dao\BudgetDAO.java ^
    src\service\AuthenticationService.java ^
    src\service\RoutineService.java ^
    src\service\ExpenseService.java ^
    src\service\BudgetService.java ^
    src\service\ReminderService.java ^
    src\view\LoginFrame.java ^
    src\view\RegisterFrame.java ^
    src\view\DashboardFrame.java ^
    src\view\RoutinePanel.java ^
    src\view\ExpensePanel.java ^
    src\view\BudgetPanel.java ^
    src\view\ReportPanel.java

if %errorlevel% == 0 (
    echo.
    echo Build successful! Run the app with: run.bat
) else (
    echo.
    echo Build failed. See errors above.
)

pause
