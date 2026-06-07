SELECT o.КОД, v.ИД
FROM Н_ОЦЕНКИ o
RIGHT JOIN Н_ВЕДОМОСТИ v ON o.КОД = v.ИД 
WHERE o.КОД < 5 
    AND v.ИД < 1250981 
    AND v.ИД = 1250981;

SELECT l.ФАМИЛИЯ, v.ЧЛВК_ИД, s.УЧГОД
FROM Н_ЛЮДИ l
LEFT JOIN Н_ВЕДОМОСТИ v ON l.ИД = v.ЧЛВК_ИД
LEFT JOIN Н_СЕССИЯ s ON v.СЕССИЯ_ИД = s.ИД
WHERE l.ОТЧЕСТВО = 'Александрович' 
    AND v.ИД > 1490007;

CREATE INDEX idx_people_patronymic ON Н_ЛЮДИ (ОТЧЕСТВО);

CREATE INDEX idx_vedomosti_chlnk ON Н_ВЕДОМОСТИ (ЧЛВК_ИД);

CREATE INDEX idx_vedomosti_ses_id ON Н_ВЕДОМОСТИ (СЭССИЯ_ИД);
