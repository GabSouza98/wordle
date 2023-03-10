CREATE TABLE IF NOT EXISTS Users (
	id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
	uuid VARCHAR (255) UNIQUE NOT NULL,
	created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS Words (
	id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
	word VARCHAR (10) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS UserWords (
	id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
	word_id INTEGER NOT NULL,
	user_id INTEGER NOT NULL,
	attempts INTEGER NOT NULL,
	insert_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_word FOREIGN KEY(word_id) REFERENCES words(id),
	CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id)
)