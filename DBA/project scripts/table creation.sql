purge recyclebin;
select * from tab;

create table subject(subname varchar2(10),code varchar2(10));
insert into subject values('sub1','sc');
insert into subject values('sub2','sc_2');
insert into subject values('sub3','sc_3');
commit;
update subject set code='sc_2' where code='sc_4';
delete from subject;
commit;
select * from subject;

//creating tables
 create or replace procedure t_c(v in varchar)as 
  begin
   execute immediate 'create table subbu(id number(10))';
 end;

exec t_c('s');
