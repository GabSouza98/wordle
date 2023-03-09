SELECT attempts, count(1) as sum
FROM UserWords
where user_id = :userId
AND attempts < 7
GROUP BY attempts
order by attempts