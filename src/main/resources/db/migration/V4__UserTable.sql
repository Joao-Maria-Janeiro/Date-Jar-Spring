CREATE TABLE ConfirmationToken
(
  id           SERIAL,
  token        VARCHAR(100) UNIQUE NOT NULL,
  created_date DATE,
  user_id      INTEGER             NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES Users (id)
);
