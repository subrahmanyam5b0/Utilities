STARTUP NOMOUNT
CREATE CONTROLFILE set  DATABASE "test" RESETLOGS
    MAXLOGFILES 16
    MAXLOGMEMBERS 2
    MAXDATAFILES 30
    MAXINSTANCES 1
    MAXLOGHISTORY 292
LOGFILE
  GROUP 1 '/oradata/test/redo1.log'  SIZE 8M,
  GROUP 2 '/oradata/test/redo2.log'  SIZE 8M
DATAFILE
  '/oradata/test/system1.dbf',
  '/oradata/test/sysaux.dbf',
  '/oradata/test/undo1.dbf',
  '/oradata/test/user1.dbf'
CHARACTER SET US7ASCII
;
