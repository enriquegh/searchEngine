-- Testing if user already exists.
SELECT username FROM Login_Users WHERE user = ?

-- Adding a new user to database.
INSERT INTO Login_Users (username, password) VALUES (?, ?);

-- Testing if correct username and password provided.
SELECT username FROM Login_Users WHERE username = ? AND password = ?