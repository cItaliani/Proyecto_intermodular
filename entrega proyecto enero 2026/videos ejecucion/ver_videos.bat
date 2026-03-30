@echo off
echo Los videos estan en Google Drive porque pesan demasiado.
choice /c SN /m "Pulsa S para abrirlos o N para salir"

if errorlevel 2 goto salir
if errorlevel 1 start https://drive.google.com/drive/folders/1y43jTOY1Ws24Hp_zKGZYTk7jjbdnMfyJ

:salir
echo Saliendo...
timeout /t 2 >nul