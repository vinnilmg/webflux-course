package com.vinnilmg.webfluxcourse.controller.impl;

import com.vinnilmg.webfluxcourse.controller.UserController;
import com.vinnilmg.webfluxcourse.model.request.UserRequest;
import com.vinnilmg.webfluxcourse.model.response.UserResponse;
import com.vinnilmg.webfluxcourse.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserControllerImpl implements UserController {

    private final UserService service;

    @Override
    public ResponseEntity<Mono<Void>> save(final UserRequest request) {
        log.info("Iniciando save()");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(request).then());
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> find(String id) {
        log.info("Iniciando find()");
        return null;
    }

    @Override
    public ResponseEntity<Flux<UserResponse>> findAll() {
        log.info("Iniciando findAll()");
        return null;
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
        log.info("Iniciando update()");
        return null;
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        log.info("Iniciando delete()");
        return null;
    }

}
