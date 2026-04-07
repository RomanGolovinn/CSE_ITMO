drop materialized view if exists mv_student_averages;
create materialized view mv_student_averages as
select 
    ЧЛВК_ИД, 
    avg(cast(ОЦЕНКА as numeric)) as avg_grade
from Н_ВЕДОМОСТИ
where ОЦЕНКА in ('2', '3', '4', '5')
group by ЧЛВК_ИД;