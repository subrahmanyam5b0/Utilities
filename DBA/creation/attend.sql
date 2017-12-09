create database attend
datafile '/oradata/attend/system1.dbf' size 100m autoextend on
sysaux  datafile '/oradata/attend/sysaux.dbf' size 100m autoextend on
default  tablespace userdata datafile '/oradata/attend/user1.dbf' size 70m autoextend on
default temporary tablespace temp tempfile '/oradata/attend/temp.tmp' size 70m
undo tablespace undotbs datafile '/oradata/attend/undo1.dbf' size 70m
logfile
 group 1('/oradata/attend/redo1.log') size 8m,
 group 2('/oradata/attend/redo2.log') size 8m;
