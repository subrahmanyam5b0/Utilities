create database main
datafile '/oradata/main/system1.dbf' size 100m autoextend on
sysaux  datafile '/oradata/main/sysaux.dbf' size 100m autoextend on
default  tablespace userdata datafile '/oradata/main/user1.dbf' size 200m autoextend on
default temporary tablespace temp tempfile '/oradata/main/temp.tmp' size 100m
undo tablespace undotbs datafile '/oradata/main/undo1.dbf' size 100m
logfile
 group 1('/oradata/main/redo1.log') size 8m,
 group 2('/oradata/main/redo2.log') size 8m;
