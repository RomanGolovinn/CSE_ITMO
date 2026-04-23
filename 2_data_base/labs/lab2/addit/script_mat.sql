SELECT COUNT(Н_УЧЕНИКИ.ИД) FROM Н_УЧЕНИКИ
JOIN mv_student_averages sa ON Н_УЧЕНИКИ.ЧЛВК_ИД = sa.ЧЛВК_ИД
WHERE sa.avg_grade > 3 AND sa.avg_grade < 4;

\set update_chance random(1, 10)

\if :update_chance = 1
    DELETE FROM Н_ВЕДОМОСТИ WHERE ИД IN (SELECT ИД FROM Н_ВЕДОМОСТИ LIMIT 20);
    INSERT INTO Н_ВЕДОМОСТИ (ЧЛВК_ИД, ОЦЕНКА) 
    SELECT (random()*1000)::int, (ARRAY['3','4','5'])[floor(random()*3+1)] 
    FROM generate_series(1, 20);
    
    REFRESH MATERIALIZED VIEW mv_student_averages;
\endif