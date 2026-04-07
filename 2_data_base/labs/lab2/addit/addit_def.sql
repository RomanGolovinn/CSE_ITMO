with student_averages as (
    select 
        ЧЛВК_ИД, 
        AVG(CAST(ОЦЕНКА as numeric)) as avg_grade
    from Н_ВЕДОМОСТИ
    where ОЦЕНКА IN ('2', '3', '4', '5')
    group by ЧЛВК_ИД
)
select COUNT(Н_УЧЕНИКИ.ИД) 
from Н_УЧЕНИКИ
join student_averages sa on Н_УЧЕНИКИ.ЧЛВК_ИД = sa.ЧЛВК_ИД
where sa.avg_grade > 3 and sa.avg_grade < 4;