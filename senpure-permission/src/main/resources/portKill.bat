@echo off
set /p port=������˿ں�
echo %port%
netstat -aon|findstr ":%port%"
set /p port=������رյĽ���id
taskkill -f /pid %port%
pause