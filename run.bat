@echo off
setlocal enabledelayedexpansion

:: 1. Intentar mvn en PATH
where mvn >nul 2>&1
if %ERRORLEVEL% equ 0 (
    set MVN=mvn
    goto :run
)

:: 2. Buscar en ubicaciones comunes
for %%D in (
    "C:\Program Files"
    "C:\Program Files (x86)"
    "C:\Tools"
    "C:\Dev"
    "C:\devtools"
    "C:\java"
    "%USERPROFILE%"
    "%USERPROFILE%\Downloads"
    "%USERPROFILE%\Documents"
) do (
    if exist %%D (
        for /d %%F in (%%~D\*maven* %%~D\*Maven* %%~D\*apache-maven*) do (
            if exist "%%F\bin\mvn.cmd" (
                set MVN="%%F\bin\mvn.cmd"
                echo Maven encontrado en: %%F
                goto :run
            )
        )
    )
)

:: 3. Busqueda recursiva (ultimo recurso)
echo Buscando Maven en el sistema, espera...
for /f "delims=" %%F in ('dir /b /s /ad "C:\*maven*" 2^>nul') do (
    if exist "%%F\bin\mvn.cmd" (
        set MVN="%%F\bin\mvn.cmd"
        echo Maven encontrado en: %%F
        goto :run
    )
)

echo No se encontro Maven en el sistema.
echo Descargalo de: https://maven.apache.org/download.cgi
pause
exit /b 1

:run
echo Iniciando SG-SST (modo desarrollo)...
call %MVN% clean compile vaadin:prepare-frontend exec:java
pause
