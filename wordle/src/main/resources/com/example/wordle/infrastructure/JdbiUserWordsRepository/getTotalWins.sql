SELECT count(1)
FROM UserWords
WHERE user_id = :userId
AND attempts < 7