package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private static final String CORRECT_USER_NAME = "User Name";
    private static final String CORRECT_USER_EMAIL = "practicum@yandex.ru";
    private static final String CORRECT_USER_LOGIN = "USER_LOGIN";
    private final static LocalDate CORRECT_USER_BIRTHDAY = LocalDate.now();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private UserController userController;

    private User correctUser;
    private User correctUpdatedUser;

    @BeforeEach
    void setUp() {
        correctUser = User.builder()
                .name(CORRECT_USER_NAME)
                .email(CORRECT_USER_EMAIL)
                .login(CORRECT_USER_LOGIN)
                .birthday(CORRECT_USER_BIRTHDAY)
                .build();

        correctUpdatedUser = User.builder()
                .name("Updated User")
                .email("updateduser@yandex.ru")
                .login("UPDATED_USER")
                .birthday(CORRECT_USER_BIRTHDAY.minusYears(20))
                .build();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCreateCorrectUser() throws Exception {
        String body = objectMapper.writeValueAsString(correctUser);
        correctUser.setId(1L);
        String correctPostResponse = objectMapper.writeValueAsString(correctUser);

        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(correctPostResponse));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCreateCorrectUserWithEmptyName() throws Exception {
        String emptyName = "";
        correctUser.setName(emptyName);
        String body = objectMapper.writeValueAsString(correctUser);

        correctUser.setId(1L);
        correctUser.setName(CORRECT_USER_LOGIN);
        String correctPostResponse = objectMapper.writeValueAsString(correctUser);

        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(correctPostResponse));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldGetUsers() throws Exception {
        User user1 = User.builder()
                .name("User 1 Name")
                .email("user1@yandex.ru")
                .login("USER_1_LOGIN")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        User user2 = User.builder()
                .name("User 2 Name")
                .email("user2@yandex.ru")
                .login("USER_2_LOGIN")
                .birthday(LocalDate.of(1990, 6, 15))
                .build();

        User user3 = User.builder()
                .name("User 3 Name")
                .email("user3@yandex.ru")
                .login("USER_3_LOGIN")
                .birthday(LocalDate.of(2000, 12, 31))
                .build();

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        String correctPostResponse = objectMapper.writeValueAsString(List.of(user1, user2, user3));

        this.mockMvc.perform(
                        get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(correctPostResponse));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateCorrectUser() throws Exception {
        userController.create(correctUser);

        correctUpdatedUser.setId(1L);
        String body = objectMapper.writeValueAsString(correctUpdatedUser);

        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfIncorrectId() throws Exception {
        userController.create(correctUser);

        correctUpdatedUser.setId(999L);
        String body = objectMapper.writeValueAsString(correctUpdatedUser);

        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateUserIfLoginIsEmpty() throws Exception {
        String emptyLogin = "";
        User user = User.builder()
                .name(CORRECT_USER_NAME)
                .email(CORRECT_USER_EMAIL)
                .login(emptyLogin)
                .birthday(CORRECT_USER_BIRTHDAY)
                .build();
        String body = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateUserIfLoginHaveBlank() throws Exception {
        String incorrectLogin = "INCORRECT LOGIN";
        User user = User.builder()
                .name(CORRECT_USER_NAME)
                .email(CORRECT_USER_EMAIL)
                .login(incorrectLogin)
                .birthday(CORRECT_USER_BIRTHDAY)
                .build();
        String body = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateUserWithFutureBirthday() throws Exception {
        User user = User.builder()
                .name(CORRECT_USER_NAME)
                .email(CORRECT_USER_EMAIL)
                .login(CORRECT_USER_LOGIN)
                .birthday(LocalDate.now().plusDays(1))
                .build();
        String body = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateUserIfEmailIsEmpty() throws Exception {
        String emptyEmail = "";
        User user = User.builder()
                .name(CORRECT_USER_NAME)
                .email(emptyEmail)
                .login(CORRECT_USER_LOGIN)
                .birthday(LocalDate.now().plusDays(1))
                .build();
        String body = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotCreateUserIfEmailIsWrong() throws Exception {
        String wrongEmail = "userwithoutdogs.ru";
        User user = User.builder()
                .name(CORRECT_USER_NAME)
                .email(wrongEmail)
                .login(CORRECT_USER_LOGIN)
                .birthday(LocalDate.now().plusDays(1))
                .build();
        String body = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfEmailIsWrong() throws Exception {
        userController.create(correctUser);

        String wrongEmail = "userwithoutdogs.ru";
        User updatedUser = User.builder()
                .name("Updated User")
                .email(wrongEmail)
                .login("UPDATED_USER")
                .birthday(CORRECT_USER_BIRTHDAY.minusYears(20))
                .build();
        updatedUser.setId(1L);
        String body = objectMapper.writeValueAsString(updatedUser);

        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfEmailIsEmpty() throws Exception {
        userController.create(correctUser);

        String emptyEmail = "";
        User updatedUser = User.builder()
                .name("Updated User")
                .email(emptyEmail)
                .login("UPDATED_USER")
                .birthday(CORRECT_USER_BIRTHDAY.minusYears(20))
                .build();
        updatedUser.setId(1L);
        String body = objectMapper.writeValueAsString(updatedUser);

        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfFutureBirthday() throws Exception {
        userController.create(correctUser);

        User updatedUser = User.builder()
                .name("Updated User")
                .email("updateduser@yandex.ru")
                .login("UPDATED_USER")
                .birthday(CORRECT_USER_BIRTHDAY.plusDays(1))
                .build();
        updatedUser.setId(1L);
        String body = objectMapper.writeValueAsString(updatedUser);

        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfLoginIsEmpty() throws Exception {
        userController.create(correctUser);

        String emptyLogin = "";
        User updatedUser = User.builder()
                .name("Updated User")
                .email("updateduser@yandex.ru")
                .login(emptyLogin)
                .birthday(CORRECT_USER_BIRTHDAY.minusYears(20))
                .build();
        updatedUser.setId(1L);
        String body = objectMapper.writeValueAsString(updatedUser);

        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldNotUpdateIfLoginHaveBlank() throws Exception {
        userController.create(correctUser);

        String incorrectLogin = "INCORRECT LOGIN";
        User updatedUser = User.builder()
                .name("Updated User")
                .email("updateduser@yandex.ru")
                .login(incorrectLogin)
                .birthday(CORRECT_USER_BIRTHDAY.minusYears(20))
                .build();
        updatedUser.setId(1L);
        String body = objectMapper.writeValueAsString(updatedUser);

        this.mockMvc.perform(
                        put("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}