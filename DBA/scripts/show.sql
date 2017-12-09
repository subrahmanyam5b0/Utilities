conn sys/sys@to_students as sysdba 
set echo off
set sqlp ''
set timing off
set heading off
set feedback off
prompt In Students Database
@list.sql
set sqlp '_user>>'
conn sys/sys@to_Marks as sysdba 
set echo off
set sqlp ''
set timing off
set heading off
set feedback off
prompt In Marks Database
@list.sql
set sqlp '_user>>'
conn sys/sys@to_attend as sysdba 
set echo off
set sqlp ''
set timing off
set heading off
set feedback off
prompt In Attend Database
@list.sql
set sqlp '_user>>'
