package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
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

    private Film correctFilm;

    @BeforeEach
    void setUp() {
        correctFilm = Film.builder()
                .name(CORRECT_FILM_NAME)
                .description(CORRECT_FILM_DESCRIPTION)
                .releaseDate(TEST_DATE)
                .duration(CORRECT_FILM_DURATION)
                .build();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCreateCorrectFilm() throws Exception {
        String body = objectMapper.writeValueAsString(correctFilm);

        correctFilm.setId(1);
        String correctPostResponse = objectMapper.writeValueAsString(correctFilm);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(correctPostResponse));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldGetFilms() throws Exception {
        Film film1 = Film.builder()
                .name("Film 1 Name")
                .description("Film 1 Description")
                .releaseDate(TEST_DATE.plusYears(50))
                .duration(CORRECT_FILM_DURATION + 60)
                .build();

        Film film2 = Film.builder()
                .name("Film 2 Name")
                .description("Film 2 Description")
                .releaseDate(TEST_DATE.plusYears(100))
                .duration(CORRECT_FILM_DURATION + 120)
                .build();

        Film film3 = Film.builder()
                .name("Film 3 Name")
                .description("Film 3 Description")
                .releaseDate(TEST_DATE.plusYears(110))
                .duration(CORRECT_FILM_DURATION + 240)
                .build();

        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);

        String correctPostResponse = objectMapper.writeValueAsString(List.of(film1, film2, film3));

        this.mockMvc.perform(
                        get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(correctPostResponse));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateCorrectFilm() throws Exception {
        filmController.create(correctFilm);

        Film updatedFilm = Film.builder()
                .id(1)
                .name("Updated Film Name")
                .description("Updated Film Description")
                .releaseDate(TEST_DATE.plusYears(100).plusMonths(5).plusDays(7))
                .duration(CORRECT_FILM_DURATION + 120)
                .build();

        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilIfIncorrectId() throws Exception {
        filmController.create(correctFilm);

        Film updatedFilm = Film.builder()
                .id(999)
                .name("Updated Film Name")
                .description("Updated Film Description")
                .releaseDate(TEST_DATE.plusYears(100).plusMonths(5).plusDays(7))
                .duration(CORRECT_FILM_DURATION + 120)
                .build();

        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        put("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilmNameIsEmpty() throws Exception {
        filmController.create(correctFilm);

        String emptyName = "";

        Film updatedFilm = Film.builder()
                .id(1)
                .name(emptyName)
                .description("Updated Film Description")
                .releaseDate(TEST_DATE.plusYears(100).plusMonths(5).plusDays(7))
                .duration(CORRECT_FILM_DURATION + 120)
                .build();

        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilmDescriptionIsLong() throws Exception {
        filmController.create(correctFilm);

        String incorrectDescription = RandomString.make(201);

        Film updatedFilm = Film.builder()
                .id(1)
                .name("Updated Film Name")
                .description(incorrectDescription)
                .releaseDate(TEST_DATE.plusYears(100).plusMonths(5).plusDays(7))
                .duration(CORRECT_FILM_DURATION + 120)
                .build();

        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilmReleaseIsEarly() throws Exception {
        filmController.create(correctFilm);

        Film updatedFilm = Film.builder()
                .id(1)
                .name("Updated Film Name")
                .description("Updated Film Description")
                .releaseDate(TEST_DATE.minusDays(1))
                .duration(CORRECT_FILM_DURATION + 120)
                .build();

        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFilmDurationIs0() throws Exception {
        filmController.create(correctFilm);

        Film updatedFilm = Film.builder()
                .id(1)
                .name("Updated Film Name")
                .description("Updated Film Description")
                .releaseDate(TEST_DATE.plusYears(100).plusMonths(5).plusDays(7))
                .duration(0)
                .build();

        String body = objectMapper.writeValueAsString(updatedFilm);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateCorrectFilmIfDurationIs0() throws Exception {
        int filmDuration = 0;

        Film film = Film.builder()
                .name(CORRECT_FILM_NAME)
                .description(CORRECT_FILM_DESCRIPTION)
                .releaseDate(TEST_DATE)
                .duration(filmDuration)
                .build();

        String body = objectMapper.writeValueAsString(film);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateCorrectFilmIfEarlyReleaseDate() throws Exception {
        LocalDate testReleaseDate = TEST_DATE.minusDays(1);

        Film film = Film.builder()
                .name(CORRECT_FILM_NAME)
                .description(CORRECT_FILM_DESCRIPTION)
                .releaseDate(testReleaseDate)
                .duration(CORRECT_FILM_DURATION)
                .build();

        String body = objectMapper.writeValueAsString(film);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateCorrectFilmIfNameIsEmpty() throws Exception {
        String filmName = "";

        Film film = Film.builder()
                .name(filmName)
                .description(CORRECT_FILM_DESCRIPTION)
                .releaseDate(TEST_DATE)
                .duration(CORRECT_FILM_DURATION)
                .build();

        String body = objectMapper.writeValueAsString(film);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateCorrectFilmIfDescriptionIsLong() throws Exception {
        String filmDescription = RandomString.make(201);

        Film film = Film.builder()
                .name(CORRECT_FILM_NAME)
                .description(filmDescription)
                .releaseDate(TEST_DATE)
                .duration(CORRECT_FILM_DURATION)
                .build();

        String body = objectMapper.writeValueAsString(film);

        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}