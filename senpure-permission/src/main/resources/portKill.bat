@echo off
set /p port=请输入端口号
echo %port%
netstat -aon|findstr ":%port%"
set /p port=请输入关闭的进程id
taskkill -f /pid %port%
pause