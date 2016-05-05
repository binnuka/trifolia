Param(
    [Parameter(Mandatory=$True)]
    $databaseDirectory='Database',
    [Parameter(Mandatory=$True)]
    $appVersion,
    [Parameter(Mandatory=$True)]
    $databaseServer,
    [Parameter(Mandatory=$True)]
    $databaseName)


Write-Host "Update scripts will be applied to $databaseServer, database $databaseName"

$updateScripts = Get-ChildItem . "$databaseDirectory\DDL\$appVersion*.sql" | Sort-Object Name

foreach ($cUpdateScript in $updateScripts) {    
    $scriptToRun = $cUpdateScript.FullName
    
    if ($scriptToRun -eq $null) {
        continue;
    }
    
    Write-Host "Running script: $scriptToRun"
    sqlcmd -E -i $scriptToRun -S $databaseServer -d $databaseName
}

$updateScripts = Get-ChildItem . "$databaseDirectory\Data\$appVersion*.sql" | Sort-Object Name

foreach ($cUpdateScript in $updateScripts) {    
    $scriptToRun = $cUpdateScript.FullName
    
    if ($scriptToRun -eq $null) {
        continue;
    }
    
    Write-Host "Running script: $scriptToRun"
    sqlcmd -E -i $scriptToRun -S $databaseServer -d $databaseName
}