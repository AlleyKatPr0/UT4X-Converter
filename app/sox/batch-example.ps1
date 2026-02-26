<#
.SYNOPSIS
  Batch convert audio files with SoX (robust Windows PowerShell wrapper).

.DESCRIPTION
  Place this script in the same folder as sox.exe (optional). Drag-and-drop files
  onto this script or run from a PowerShell prompt. Creates a "converted" folder
  by default and logs results to converted/sox-convert-YYYYMMDD-HHMMSS.log.

.NOTES
  - Requires PowerShell 5+ (works on Windows 10/11). For parallel processing use PowerShell 7+ and enable -Parallel.
#>

param(
    [Parameter(Mandatory=$false, ValueFromRemainingArguments=$true)]
    [string[]] $Files,

    [string] $OutDir = "converted",
    [string] $SoxArgs = 'rate -v 44100',
    [switch] $PreserveStructure
)

# Resolve script directory
$scriptDir = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent

# Locate sox: prefer same directory, else PATH
$soxLocal = Join-Path $scriptDir 'sox.exe'
if (Test-Path $soxLocal) {
    $soxExe = $soxLocal
} else {
    $soxExe = (Get-Command sox.exe -ErrorAction SilentlyContinue)?.Source
}
if (-not $soxExe) {
    Write-Error "sox.exe not found. Place sox.exe next to this script or add it to PATH."
    exit 2
}

# If invoked via Explorer drag/drop, $Files will be provided. If not, prompt
if (-not $Files -or $Files.Count -eq 0) {
    Write-Host "No files provided. Drag files onto this script or pass paths as arguments."
    exit 0
}

# Create output dir
$convertedRoot = Join-Path $scriptDir $OutDir
New-Item -ItemType Directory -Path $convertedRoot -Force | Out-Null

$timestamp = (Get-Date).ToString('yyyyMMdd-HHmmss')
$logFile = Join-Path $convertedRoot ("sox-convert-$timestamp.log")
"Start SoX batch on $(Get-Date) - sox: $soxExe" | Out-File -FilePath $logFile -Encoding utf8

$failures = @()

foreach ($f in $Files) {
    try {
        if (-not (Test-Path $f)) {
            "SKIP: '$f' not found" | Tee-Object -FilePath $logFile -Append
            $failures += $f
            continue
        }

        $fullIn = (Resolve-Path $f).Path
        $inName = Split-Path $fullIn -Leaf

        if ($PreserveStructure) {
            $rel = [IO.Path]::GetRelativePath($scriptDir, (Split-Path $fullIn -Parent))
            $outFolder = Join-Path $convertedRoot $rel
            New-Item -ItemType Directory -Path $outFolder -Force | Out-Null
            $outPath = Join-Path $outFolder $inName
        } else {
            $outPath = Join-Path $convertedRoot $inName
        }

        # Build and run command
        $args = @()
        $args += '"' + $fullIn + '"'
        $args += '"' + $outPath + '"'
        $args += $SoxArgs

        $cmd = "& `"$soxExe`" $($args -join ' ')"
        "RUN: $cmd" | Tee-Object -FilePath $logFile -Append

        # Execute and capture exit code
        $processInfo = Start-Process -FilePath $soxExe -ArgumentList @($fullIn, $outPath) -NoNewWindow -Wait -PassThru -RedirectStandardError ([IO.File]::OpenWrite($logFile)) -RedirectStandardOutput ([IO.File]::OpenWrite($logFile))
        # If you need to pass complex SoxArgs split them properly and use Start-Process -ArgumentList with array
        if ($processInfo.ExitCode -ne 0) {
            "ERROR: '$fullIn' -> exit $($processInfo.ExitCode)" | Tee-Object -FilePath $logFile -Append
            $failures += $fullIn
        } else {
            "OK: '$fullIn' -> '$outPath'" | Tee-Object -FilePath $logFile -Append
        }
    } catch {
        "EXCEPTION processing '$f' : $_" | Tee-Object -FilePath $logFile -Append
        $failures += $f
    }
}

"Batch finished at $(Get-Date). Failures: $($failures.Count)" | Tee-Object -FilePath $logFile -Append

if ($failures.Count -gt 0) {
    Write-Host "Completed with $($failures.Count) failures. See $logFile" -ForegroundColor Yellow
    exit 1
} else {
    Write-Host "All files processed successfully. Output: $convertedRoot" -ForegroundColor Green
    exit 0
}