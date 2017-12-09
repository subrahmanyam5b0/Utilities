create database students
datafile '/oradata/students/system1.dbf' size 100m autoextend on
sysaux  datafile '/oradata/students/sysaux.dbf' size 100m autoextend on
default  tablespace userdata datafile '/oradata/students/user1.dbf' size 70m autoextend on
default temporary tablespace temp tempfile '/oradata/students/temp.tmp' size 70m
undo tablespace undotbs datafile '/oradata/students/undo1.dbf' size 70m
logfile
 group 1('/oradata/students/redo1.log') size 8m,
 group 2('/oradata/students/redo2.log') size 8m;
