package com.vinnilmg.webfluxcourse.service;

import com.vinnilmg.webfluxcourse.entity.User;
import com.vinnilmg.webfluxcourse.mapper.UserMapper;
import com.vinnilmg.webfluxcourse.model.request.UserRequest;
import com.vinnilmg.webfluxcourse.repository.UserRepository;
import com.vinnilmg.webfluxcourse.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
        return repository.save(mapper.toEntity(request));
    }

    public Mono<User> findById(final String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ObjectNotFoundException(
                                format("Object not found. Id: %s, Type: %s", id, User.class.getSimpleName())
                        )
                ));
    }

    public Flux<User> findAll() {
        return repository.findAll();
    }

    public Mono<User> update(final String id, final UserRequest request) {
        return findById(id)
                .map(entity -> mapper.toEntity(request, entity))
                .flatMap(repository::save);
    }

}
