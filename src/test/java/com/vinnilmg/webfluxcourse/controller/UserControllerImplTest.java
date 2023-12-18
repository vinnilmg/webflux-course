package com.vinnilmg.webfluxcourse.controller;

import com.mongodb.reactivestreams.client.MongoClient;
import com.vinnilmg.webfluxcourse.entity.User;
import com.vinnilmg.webfluxcourse.mapper.UserMapper;
import com.vinnilmg.webfluxcourse.model.request.UserRequest;
import com.vinnilmg.webfluxcourse.model.response.UserResponse;
import com.vinnilmg.webfluxcourse.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;

    private static final String ENDPOINT_USERS = "/users";

    @Test
    @DisplayName("Test endpoint save with success")
    void testSaveWithSuccess() {
        final var request = makeUserRequest("Mariazinha", "maria@mail.com", "password123");

        when(service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post()
                .uri(ENDPOINT_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        verify(service).save(any(UserRequest.class)); // Default: times == 1
    }

    @Test
    @DisplayName("Test endpoint save with invalid name then return bad request")
    void testSaveWithInvalidNameBadRequest() {
        final var request = makeUserRequest(" Mariazinha", "maria@mail.com", "password123");

        webTestClient.post()
                .uri(ENDPOINT_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(ENDPOINT_USERS)
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("field cannot have blank spaces at the end or the begin");
    }

    @Test
    @DisplayName("Test endpoint save with empty name then return bad request")
    void testSaveWithEmptyNameBadRequest() {
        final var request = makeUserRequest("", "maria@mail.com", "password123");

        webTestClient.post()
                .uri(ENDPOINT_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(ENDPOINT_USERS)
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("must not be null or empty");
    }

    @Test
    @DisplayName("Test endpoint save with invalid size name then return bad request")
    void testSaveWithInvalidSizeNameBadRequest() {
        final var request = makeUserRequest("ma", "maria@mail.com", "password123");

        webTestClient.post()
                .uri(ENDPOINT_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(ENDPOINT_USERS)
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("must be between 3 and 50 characters");
    }

    @Test
    @DisplayName("Test endpoint save with invalid email then return bad request")
    void testSaveWithInvalidEmailBadRequest() {
        final var request = makeUserRequest("Maria", "mariamail.com", "password123");

        webTestClient.post()
                .uri(ENDPOINT_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(ENDPOINT_USERS)
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("email")
                .jsonPath("$.errors[0].message").isEqualTo("invalid e-mail");
    }

    @Test
    @DisplayName("Test find by id endpoint with success")
    void testFindByIdWithSuccess() {
        final var id = "12345";
        final var name = "Vinicius";
        final var email = "vini@mail.com";
        final var password = "password123";
        final var response = new UserResponse(id, name, email, password);

        when(service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(response);

        webTestClient.get()
                .uri(String.format("%s/%s", ENDPOINT_USERS, id))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.name").isEqualTo(name)
                .jsonPath("$.email").isEqualTo(email)
                .jsonPath("$.password").isEqualTo(password);
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    private static UserRequest makeUserRequest(final String nome, final String email, final String passw) {
        return new UserRequest(nome, email, passw);
    }
}