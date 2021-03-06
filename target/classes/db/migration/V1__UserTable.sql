CREATE TABLE Users
(
  id         SERIAL PRIMARY KEY,
  username   VARCHAR(100) UNIQUE NOT NULL,
  email      VARCHAR(100) UNIQUE NOT NULL,
  password   VARCHAR(50)         NOT NULL,
  picture    VARCHAR(10000000),
  roles      VARCHAR(100),
  partner_id INTEGER,
  FOREIGN KEY (partner_id) REFERENCES Users (id)
);

CREATE TABLE Category
(
  id      SERIAL,
  name    VARCHAR(100) NOT NULL,
  type    INTEGER      NOT NULL,
  user_id INTEGER      NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES Users (id)
);


CREATE TABLE Activity
(
  id          SERIAL,
  name        VARCHAR(100) NOT NULL,
  category_id INTEGER      NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (category_id) REFERENCES Category (id)
);

