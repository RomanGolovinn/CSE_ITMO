select count(Н_УЧЕНИКИ.ИД) 
from Н_УЧЕНИКИ
join mv_student_averages sa on Н_УЧЕНИКИ.ЧЛВК_ИД = sa.ЧЛВК_ИД
where sa.avg_grade > 3 and sa.avg_grade < 4;