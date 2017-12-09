conn sys/sys@to_marks as sysdba
create tablespace cse datafile '/oradata/marks/cse.dbf' size 30m autoextend on;
create tablespace ece datafile '/oradata/marks/ece.dbf' size 30m autoextend on;
create tablespace eee datafile '/oradata/marks/eee.dbf' size 30m autoextend on;
create tablespace it datafile '/oradata/marks/it.dbf' size 30m autoextend on;
create tablespace civil datafile '/oradata/marks/civil.dbf' size 30m autoextend on;
conn sys/sys@to_attend as sysdba
create tablespace cse datafile '/oradata/attend/cse.dbf' size 30m autoextend on;
create tablespace ece datafile '/oradata/attend/ece.dbf' size 30m autoextend on;
create tablespace eee datafile '/oradata/attend/eee.dbf' size 30m autoextend on;
create tablespace it datafile '/oradata/attend/it.dbf' size 30m autoextend on;
create tablespace civil datafile '/oradata/attend/civil.dbf' size 30m autoextend on;
conn sys/sys@to_students as sysdba
create tablespace cse datafile '/oradata/students/cse.dbf' size 30m autoextend on;
create tablespace ece datafile '/oradata/students/ece.dbf' size 30m autoextend on;
create tablespace eee datafile '/oradata/students/eee.dbf' size 30m autoextend on;
create tablespace it datafile '/oradata/students/it.dbf' size 30m autoextend on;
create tablespace civil datafile '/oradata/students/civil.dbf' size 30m autoextend on;
exit
