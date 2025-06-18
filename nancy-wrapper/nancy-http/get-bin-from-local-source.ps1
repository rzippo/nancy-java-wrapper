param(
    [string]$NancyHttpRepositoryPath = "C:\GitHub\nancy-http"
);

$ErrorActionPreference = 'Stop';

Push-Location $NancyHttpRepositoryPath;

dotnet publish -c Release

$sourceBinLocation = "./bin/Release/net8.0/publish";
$targetBinLocation = "$PSScriptRoot/bin";

Get-ChildItem $targetBinLocation -Exclude ".gitkeep" | Remove-Item -Force -Recurse;
Copy-Item -Recurse "$sourceBinLocation/*" $targetBinLocation;

Pop-Location;
