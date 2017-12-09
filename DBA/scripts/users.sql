conn sys/sys@to_students as sysdba
grant connect,resource,create database link,create materialized view ,create table to admin identified by admin;
conn sys/sys@to_marks as sysdba
grant connect,resource,create database link,create materialized view,create table to admin identified by admin;
conn sys/sys@to_attend as sysdba
grant connect,resource,create database link,create materialized view ,create table to admin identified by admin;
conn sys/sys@to_main as sysdba
set sqlp '_user>>'
