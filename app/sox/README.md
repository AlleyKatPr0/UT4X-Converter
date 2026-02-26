# SoX helper for UT Converter

This folder contains helper scripts and binaries used by UT Converter to convert audio files using SoX (Sound eXchange). SoX itself is a third‑party program and is not part of this repository.

## What is here
- batch-example.ps1 — PowerShell wrapper to run SoX in batch (handles spaces, quoted paths, logging and optional folder structure preservation).
- (optional) sox.exe — place a suitable Windows SoX binary here, or ensure SoX is available on PATH.

## Quick usage
1. Place a Windows build of `sox.exe` next to this README/script or install SoX and add it to your PATH.
2. To process files by drag-and-drop in Explorer: select audio files, drop them on `batch-example.ps1` (you may need to create a shortcut and set it to run with PowerShell) or run it from a PowerShell console:

   ```powershell
   # example: run the script with explicit files and custom args
   .\batch-example.ps1 -Files "C:\path\to\in.wav" "C:\path\to\in2.mp3" -SoxArgs 'rate -v 44100' -OutDir converted
   ```

3. Outputs are written to the `converted` folder by default. A log file `sox-convert-YYYYMMDD-HHMMSS.log` is created inside the output folder.

## Script options
- `-Files` — list of files to convert (drag & drop provides these automatically).
- `-OutDir` — output directory (relative to script folder by default: `converted`).
- `-SoxArgs` — SoX effect/format arguments (default in the example is `rate -v 44100`).
- `-PreserveStructure` — preserve input folder structure under the output folder.

Adjust `SoxArgs` to change resampling, format conversion, effects, etc.

## Integration notes for packaging
- If you bundle `sox.exe` with the UT Converter installer, include SoX's binary and its license text in the installer and obey SoX's licensing terms.
- Consider adding a SHA256 checksum file for the included `sox.exe` and verify it at runtime if you want integrity checks.

## Licensing and redistribution
- SoX is a third‑party program maintained separately from this project. This repository only provides helper scripts and does not include SoX source code by default.
- Before redistributing SoX binaries with the UT Converter, verify the SoX license that comes with the binary you downloaded and include that LICENSE file in your distribution. If you are unsure about compatibility with your distribution, consult the SoX project page (https://sox.sourceforge.net/) or the binary provider for licensing details.

## Troubleshooting
- `sox.exe not found` — ensure `sox.exe` is next to the script or available on PATH.
- Permission errors creating `converted` — run PowerShell with sufficient permissions or pick an output folder under your user profile.
- Quoted/long path problems — use the bundled PowerShell script (handles quoted paths) or run SoX from PowerShell with properly quoted arguments.

## Links
- Official SoX project: https://sox.sourceforge.net/

