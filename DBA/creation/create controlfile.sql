STARTUP NOMOUNT
CREATE CONTROLFILE set  DATABASE "main" RESETLOGS
    MAXLOGFILES 16
    MAXLOGMEMBERS 2
    MAXDATAFILES 30
    MAXINSTANCES 1
    MAXLOGHISTORY 292
LOGFILE
  GROUP 1 '/oradata/main/redo1.log'  SIZE 8M,
  GROUP 2 '/oradata/main/redo2.log'  SIZE 8M
DATAFILE
  '/oradata/main/system1.dbf',
  '/oradata/main/sysaux.dbf',
  '/oradata/main/undo1.dbf',
  '/oradata/main/user1.dbf'
CHARACTER SET US7ASCII
;
