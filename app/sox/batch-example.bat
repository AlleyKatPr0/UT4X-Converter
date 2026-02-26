rem -----------------------------------------------------------------------------
rem batch-example.bat
rem -----------------------------------------------------------------------------
rem Purpose:
rem   Example Windows batch script demonstrating batch-processing audio files
rem   with SoX (Sound eXchange). Designed for easy drag-and-drop usage: place
rem   this batch file in the same folder as sox.exe (or ensure sox.exe is on
rem   PATH), then drag files onto the batch file (or onto a shortcut to it).
rem 
rem Prerequisites:
rem   - Windows 7/8/10/11 (32/64-bit as appropriate for your sox.exe).
rem   - sox.exe present in the same folder as this batch file OR available on PATH.
rem   - Write permission to create the output folder ("converted") in this folder.
rem 
rem Behavior:
rem   - Changes current working directory to the directory containing this script.
rem   - Creates a "converted" subfolder (if it doesn't exist).
rem   - For each file provided as an argument (drag-and-drop populates %*):
rem       - Runs sox on the file and writes result into converted\<original-name>
rem       - In this example the command resamples audio to 44100 Hz using a
rem         high-quality resampling (-v 44100). Modify the sox arguments as needed.
rem   - Pauses at the end so you can review messages/errors.
rem 
rem Usage examples:
rem   1) Drag one or more audio files onto this batch file in Explorer.
rem   2) From cmd.exe:
rem        "C:\path\to\batch-example.bat" "C:\music\song1.wav" "C:\music\song2.mp3"
rem   3) To run from another folder while using bundled sox.exe:
rem        pushd "C:\path\to\this\folder" && batch-example.bat "C:\file.wav" && popd
rem 
rem Key variables / syntax explained:
rem   %~dp0      - Directory path of the running batch file (includes trailing backslash).
rem   %*         - All command-line arguments passed to the batch file.
rem   FOR %%A IN (%*) DO ...
rem               - Iterates over each argument; in a batch file use %%A (single %A on CLI).
rem   %%~nxA     - Expands %%A to its file name and extension only (no path).
rem 
rem Customization tips:
rem   - Change "converted" to any relative/absolute output folder you prefer.
rem   - Modify the sox arguments (here: rate -v 44100) to change conversion behavior.
rem   - To preserve subfolders or recurse, use a FOR /R loop and adjust %%~pnxA expansions.
rem   - If sox.exe is not in the same folder, either add it to PATH or replace "sox"
rem     with the full path: "%~dp0sox.exe" (quoted if path contains spaces).
rem 
rem Troubleshooting:
rem   - If files are not processed, open a cmd window and run:
rem       where sox
rem     to verify which sox.exe is being executed.
rem   - If permission errors occur creating "converted", run cmd as Administrator
rem     or choose an output folder under a writeable location (e.g., %USERPROFILE%).
rem   - If audio quality or format issues appear, consult SoX docs for appropriate
rem     format-specific options and codecs.
rem 
rem Exit codes:
rem   - SoX returns non-zero on failure. This batch example does not stop on first
rem     error; to abort on error, add "if errorlevel 1 exit /b %errorlevel%" after
rem     the sox call inside the loop.
rem 
rem License / Attribution:
rem   - This is an example helper file; adapt as needed for your workflow.
rem -----------------------------------------------------------------------------

cd %~dp0
rem ensure output directory exists (no error if already present)
mkdir converted 2>nul

rem Iterate over all provided files. For each argument %%A:
rem   %%~nxA -> file name + extension (used to construct output name)
FOR %%A IN (%*) DO (
    rem If sox.exe isn't on PATH but located next to this batch file, use:
    rem "%~dp0sox.exe" "%%A" "converted/%%~nxA" rate -v 44100
    rem The example uses the plain 'sox' command which works if sox is on PATH
    sox "%%A" "converted/%%~nxA" rate -v 44100
    rem Optional: report success/failure per file
    if errorlevel 1 (
        echo Failed to process "%%A"
    ) else (
        echo Processed "%%A" -> "converted/%%~nxA"
    )
)

rem Pause so user can read output; remove this line to run unattended.
pause
