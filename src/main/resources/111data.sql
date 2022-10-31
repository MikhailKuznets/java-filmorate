INSERT INTO USERS (login, user_name, email, birthday)
VALUES ('Artem', 'Artemka1988', 'artem@yandex.ru', '1988-12-05');
INSERT INTO USERS (login, user_name, email, birthday)
VALUES ('Danila', 'Danila1991', 'danila@google.com', '1991-10-10');
INSERT INTO USERS (login, user_name, email, birthday)
VALUES ('Mikhail', 'Mikhail1989', 'mikhail@mail.ru', '1989-08-16');
INSERT INTO USERS (login, user_name, email, birthday)
VALUES ('Natasha', 'Natali1989', 'natali@list.ru', '1989-12-13');
INSERT INTO USERS (login, user_name, email, birthday)
VALUES ('Tanya', 'Tanya1986', 'tanyusha@bk.ru', '1986-01-25');


MERGE INTO MPA (mpa_id, mpa_name)
    VALUES ('1', 'G');
MERGE INTO MPA (mpa_id, mpa_name)
    VALUES ('2', 'PG');
MERGE INTO MPA (mpa_id, mpa_name)
    VALUES ('3', 'PG-13');
MERGE INTO MPA (mpa_id, mpa_name)
    VALUES ('4', 'R');
MERGE INTO MPA (mpa_id, mpa_name)
    VALUES ('5', 'NC-17');


MERGE INTO GENRES (genre_id, genre_name)
    VALUES ('1', 'Animation');
MERGE INTO GENRES (genre_id, genre_name)
    VALUES ('2', 'Thriller');
MERGE INTO GENRES (genre_id, genre_name)
    VALUES ('3', 'Comedy');
MERGE INTO GENRES (genre_id, genre_name)
    VALUES ('4', 'Western');
MERGE INTO GENRES (genre_id, genre_name)
    VALUES ('5', 'Documentary');
MERGE INTO GENRES (genre_id, genre_name)
    VALUES ('6', 'Drama');
MERGE INTO GENRES (genre_id, genre_name)
    VALUES ('7', 'Sci-fi');
MERGE INTO GENRES (genre_id, genre_name)
    VALUES ('8', 'Fantasy');
MERGE INTO GENRES (genre_id, genre_name)
    VALUES ('9', 'Action');


INSERT INTO FRIENDS (user_id, friend_id)
VALUES ('1', '2');
INSERT INTO FRIENDS (user_id, friend_id)
VALUES ('2', '1');
INSERT INTO FRIENDS (user_id, friend_id)
VALUES ('2', '3');
INSERT INTO FRIENDS (user_id, friend_id)
VALUES ('3', '4');
INSERT INTO FRIENDS (user_id, friend_id)
VALUES ('4', '3');
INSERT INTO FRIENDS (user_id, friend_id)
VALUES ('4', '1');


INSERT INTO FILMS (film_name, description, RELEASE_DATE, DURATION, MPA_ID)
VALUES ('The Lord of the Rings: The Fellowship of the Ring',
        'Frodo Baggins goes to save Middle-earth. The first part of Peter Jackson''s cult fantasy trilogy.',
        '2001-12-13', '178', '3');

INSERT INTO FILMS (film_name, description, RELEASE_DATE, DURATION, MPA_ID)
VALUES ('The Matrix',
        'A simulated reality that intelligent machines have created to distract humans' ||
                      ' while using their bodies as an energy source.',
        '2000-09-12', '136', '4');

INSERT INTO FILMS (film_name, description, RELEASE_DATE, DURATION, MPA_ID)
VALUES ('Dr. No',
        'The trail leads him to the underground base of Dr. Julius No, who is plotting to disrupt' ||
                  ' an early American space launch from Cape Canaveral with a radio beam weapon.',
        '1962-10-05', '111', '2');

INSERT INTO FILMS (film_name, description, RELEASE_DATE, DURATION, MPA_ID)
VALUES ('Harry Potter and the Sorcerer''s Stone',
        'Harry enters Hogwarts School of Magic and makes friends. ' ||
        'The first part of a big franchise about a little wizard.',
        '2002-08-28', '152', '2');

INSERT INTO FILMS (film_name, description, RELEASE_DATE, DURATION, MPA_ID)
VALUES ('Star Wars',
        'A Jedi and a smuggler help a rebel escape from captivity. The Beginning of George Lucas'' Epic Space Saga.',
        '1977-05-25', '121', '2');

INSERT INTO FILMS (film_name, description, RELEASE_DATE, DURATION, MPA_ID)
VALUES ('Batman: The Movie',
        'MEN DIE! WOMEN SIGH! Beneath that Batcape — he’s all man!',
        '1966-07-30', '105', '2');

INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES ( 1, 8 );
INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES ( 2, 7 );
INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES ( 3, 9 );
INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES ( 4, 8 );
INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES ( 5, 7 );
INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES ( 6, 9 );

