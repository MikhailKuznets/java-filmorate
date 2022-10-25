DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS films_genre CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS likes CASCADE;

CREATE TABLE IF NOT EXISTS USERS
(
    user_id   INTEGER      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email     varchar(255) not null,
    login     varchar(255) not null,
    user_name varchar(255),
    birthday  date,
    UNIQUE (email),
    UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS GENRES
(
    genre_id   integer not null primary key auto_increment,
    genre_name varchar(255)
);

CREATE TABLE IF NOT EXISTS MPA
(
    mpa_id   integer not null primary key auto_increment,
    mpa_name varchar(5)
);

CREATE TABLE IF NOT EXISTS FILMS
(
    film_id      integer not null primary key auto_increment,
    film_name    varchar(255),
    description  varchar(200),
    release_date date,
    rate         integer,
    duration     integer,
    mpa_id       integer references MPA (mpa_id)
);

CREATE TABLE IF NOT EXISTS FILMS_GENRE
(
    film_id  integer references FILMS (film_id),
    genre_id integer references GENRES (genre_id),

    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    user_id   integer not null references USERS,
    friend_id integer not null references USERS,

    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS LIKES
(
    user_id integer references USERS (user_id),
    film_id integer references FILMS (film_id),

    PRIMARY KEY (user_id, film_id)
);

