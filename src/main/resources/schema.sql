create table if not exists USERS
(
    user_id integer not null primary key auto_increment,
    email varchar(255) not null,
    login varchar(255) not null,
    name varchar(255),
    birthday date,
    deleted bool default false,
    UNIQUE(email),
    UNIQUE(login),
);

create table if not exists GENRES
(
    genre_id integer not null primary key auto_increment,
    name varchar(255),
);

create table if not exists MPA
(
    mpa_id integer not null primary key auto_increment,
    name varchar(255),
);

create table if not exists FILMS
(
    film_id integer not null primary key auto_increment,
    name varchar(255),
    description varchar(200),
    release_date date,
    rate integer,
    duration integer,
    mpa_id integer references MPA (mpa_id),
    deleted bool default false
);

create table if not exists FILMS_GENRE
(
    film_id integer references FILMS (film_id),
    genre_id integer references GENRES (genre_id),

    PRIMARY KEY (film_id, genre_id)
);

create table if not exists FRIENDS
(
    user_id integer not null references USERS,
    friend_id integer not null references USERS,

    PRIMARY KEY (user_id, friend_id)
);

create table if not exists LIKES
(
    user_id integer references USERS(user_id),
    film_id integer references FILMS (film_id),

    PRIMARY KEY (user_id, film_id)
);

