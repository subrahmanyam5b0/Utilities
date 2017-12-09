prompt "In Students DB"
conn sys/sys@to_students as sysdba
select distinct file_name,tablespace_name from dba_data_files;
prompt "In marks DB"
conn sys/sys@to_marks as sysdba
select distinct file_name,tablespace_name from dba_data_files;
prompt "In attend DB"
conn sys/sys@to_attend as sysdba
select distinct file_name,tablespace_name from dba_data_files;
