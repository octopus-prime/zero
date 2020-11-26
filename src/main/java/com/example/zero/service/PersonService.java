package com.example.zero.service;

import com.example.zero.controller.PersonRequest;
import com.example.zero.controller.PersonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class PersonService {

    private static final Mono<Person> NOT_FOUND = Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));

    private final PersonRepository repository;
    private final PersonMapper mapper;

    @Autowired
    PersonService(final PersonRepository repository, final PersonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Mono<PersonResponse> createPerson(final Mono<PersonRequest> dto) {
        return dto
                .map(mapper::map)
                .flatMap(repository::save)
                .map(mapper::map);
    }

    public Mono<PersonResponse> getPerson(final Mono<String> id) {
        return id
                .flatMap(repository::findById)
                .switchIfEmpty(NOT_FOUND)
                .map(mapper::map);
    }

    public Mono<PersonResponse> updatePerson(final Mono<String> id, final Mono<PersonRequest> dto) {
        return id
                .flatMap(repository::findById)
                .switchIfEmpty(NOT_FOUND)
                .zipWith(dto, mapper::merge)
                .flatMap(repository::save)
                .map(mapper::map);
    }

    public Flux<PersonResponse> searchPersons(final Mono<PersonRequest> dto) {
        return dto
                .map(mapper::map)
                .map(Example::of)
                .map(repository::findAll)
                .flatMapMany(Flux::from)
                .map(mapper::map);
    }

    public Mono<PersonResponse> deletePerson(final Mono<String> id) {
        return id
                .flatMap(repository::findById)
                .switchIfEmpty(NOT_FOUND)
                .doOnNext(repository::delete)
                .map(mapper::map);
    }

    public Mono<Void> deletePersons() {
        return repository.deleteAll();
    }
}
