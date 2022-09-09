package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    private final String CORRECT_FILM_NAME = "Test Film Name";
    private final String CORRECT_FILM_DESCRIPTION = RandomString.make(200);
    private final int CORRECT_FILM_DURATION = 1;
    private final static LocalDate TEST_DATE = LocalDate.of(1895, 12, 29);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private FilmController filmController;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCreateCorrectFilm() throws Exception {
        Film film = new Film(CORRECT_FILM_NAME, CORRECT_FILM_DESCRIPTION, TEST_DATE, CORRECT_FILM_DURATION);
        String body = objectMapper.writeValueAsString(film);

        film.setId(1);
        String correctPostResponse = objectMapper.writeValueAsString(film);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(correctPostResponse));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldGetFilms() throws Exception {
        Film film1 = new Film("Film 1 Name",
                "Film 1 Description", TEST_DATE.plusYears(50), CORRECT_FILM_DURATION + 60);

        Film film2 = new Film("Film 2 Name",
                "Film 2 Description", TEST_DATE.plusYears(100), CORRECT_FILM_DURATION + 120);

        Film film3 = new Film("Film 3 Name",
                "Film 3 Description", LocalDate.now(), CORRECT_FILM_DURATION + 240);

        filmController.createFilm(film1);
        filmController.createFilm(film2);
        filmController.createFilm(film3);

        String correctPostResponse = objectMapper.writeValueAsString(List.of(film1, film2, film3));

        this.mockMvc.perform(
                        get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(correctPostResponse));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateCorrectFilm() throws Exception {
        Film film = new Film(CORRECT_FILM_NAME, CORRECT_FILM_DESCRIPTION, TEST_DATE, CORRECT_FILM_DURATION);
        filmController.createFilm(film);

        Film updatedFilm = new Film("Updated Film Name",
                "Updated Film Description",
                TEST_DATE.plusYears(100).plusMonths(5).plusDays(7),
                CORRECT_FILM_DURATION + 120);
        updatedFilm.setId(1);
        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilIfIncorrectId() throws Exception {
        Film film = new Film(CORRECT_FILM_NAME, CORRECT_FILM_DESCRIPTION, TEST_DATE, CORRECT_FILM_DURATION);
        filmController.createFilm(film);

        Film updatedFilm = new Film("Updated Film Name",
                "Updated Film Description",
                TEST_DATE.plusYears(100).plusMonths(5).plusDays(7),
                CORRECT_FILM_DURATION + 120);
        updatedFilm.setId(999);
        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilmNameIsEmpty() throws Exception {
        Film film = new Film(CORRECT_FILM_NAME, CORRECT_FILM_DESCRIPTION, TEST_DATE, CORRECT_FILM_DURATION);
        filmController.createFilm(film);

        String emptyName = "";
        Film updatedFilm = new Film(emptyName,
                "Updated Film Description",
                TEST_DATE.plusYears(100).plusMonths(5).plusDays(7),
                CORRECT_FILM_DURATION + 60);
        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilmDescriptionIsLong() throws Exception {
        Film film = new Film(CORRECT_FILM_NAME, CORRECT_FILM_DESCRIPTION, TEST_DATE, CORRECT_FILM_DURATION);
        filmController.createFilm(film);

        String incorrectDescription = RandomString.make(201);
        Film updatedFilm = new Film("Updated Film Name",
                incorrectDescription,
                TEST_DATE.plusYears(100).plusMonths(5).plusDays(7),
                CORRECT_FILM_DURATION + 60);
        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilmReleaseIsEarly() throws Exception {
        Film film = new Film(CORRECT_FILM_NAME, CORRECT_FILM_DESCRIPTION, TEST_DATE, CORRECT_FILM_DURATION);
        filmController.createFilm(film);

        Film updatedFilm = new Film("Updated Film Name",
                "Updated Film Description",
                TEST_DATE.minusDays(1),
                CORRECT_FILM_DURATION + 60);
        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilmDurationIs0() throws Exception {
        Film film = new Film(CORRECT_FILM_NAME, CORRECT_FILM_DESCRIPTION, TEST_DATE, CORRECT_FILM_DURATION);
        filmController.createFilm(film);

        Film updatedFilm = new Film("Updated Film Name",
                "Updated Film Description",
                TEST_DATE.plusYears(100).plusMonths(5).plusDays(7),
                0);
        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateCorrectFilmIfDurationIs0() throws Exception {
        int filmDuration = 0;

        Film film = new Film(CORRECT_FILM_NAME, CORRECT_FILM_DESCRIPTION, TEST_DATE, filmDuration);
        String body = objectMapper.writeValueAsString(film);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateCorrectFilmIfEarlyReleaseDate() throws Exception {
        LocalDate testReleaseDate = TEST_DATE.minusDays(1);

        Film film = new Film(CORRECT_FILM_NAME, CORRECT_FILM_DESCRIPTION, testReleaseDate, CORRECT_FILM_DURATION);
        String body = objectMapper.writeValueAsString(film);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateCorrectFilmIfNameIsEmpty() throws Exception {
        String filmName = "";

        Film film = new Film(filmName, CORRECT_FILM_DESCRIPTION, TEST_DATE, CORRECT_FILM_DURATION);
        String body = objectMapper.writeValueAsString(film);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateCorrectFilmIfDescriptionIsLong() throws Exception {
        String filmDescription = RandomString.make(201);

        Film film = new Film(CORRECT_FILM_NAME, filmDescription, TEST_DATE, CORRECT_FILM_DURATION);
        String body = objectMapper.writeValueAsString(film);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}