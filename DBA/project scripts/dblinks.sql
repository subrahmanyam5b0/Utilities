// finding necessary privilages to create database links and materialized views
select * from  session_privs;
grant create database link,create materialized view to <username>; as sysdba

// how many database links are exists
desc user_db_links

//creating database links to all other databases

/*create database link to_students connect to us identified by us using 'to_students';
create database link to_marks connect to us identified by us using 'to_marks';
create database link to_attend connect to us identified by us using 'to_attend';*/

create database link to_server connect to us identified by us using 'to_nag';

//checking database links are exists
select * from user_db_links;

// creating materialized Views

desc user_mviews

//creating a mv subbu by using database link to_marks

create materialized view f_server refresh complete with rowid  start with sysdate next sysdate+1/24*60*60
as select * from subject@to_server;

//how many mvs exists
select * from user_mviews;


//getting data from the mv
select * from f_server;
delete from f_server;

//manual refreshing a materialized view
execute DBMS_SNAPSHOT.refresh('f_server','c'); 
//c stand for complete

//altering the materialized view for fast refreshing

alter materialized view subbu refresh fast with primary key;
 //for this we must create a materialized view log on parent table on parent database
 
//drop the mv
drop materialized view f_server;

