create database marks
datafile '/oradata/marks/system1.dbf' size 100m autoextend on
sysaux  datafile '/oradata/marks/sysaux.dbf' size 100m autoextend on
default  tablespace userdata datafile '/oradata/marks/user1.dbf' size 70m autoextend on
default temporary tablespace temp tempfile '/oradata/marks/temp.tmp' size 70m
undo tablespace undotbs datafile '/oradata/marks/undo1.dbf' size 70m
logfile
 group 1('/oradata/marks/redo1.log') size 8m,
 group 2('/oradata/marks/redo2.log') size 8m;
