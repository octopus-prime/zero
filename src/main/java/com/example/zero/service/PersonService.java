package com.example.zero.service;

import com.example.zero.controller.dto.PersonRequest;
import com.example.zero.controller.dto.PersonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PersonService {

    private static final Mono<Person> NOT_FOUND = Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));

    private final PersonRepository repository;
    private final PersonMapper mapper;

    @Autowired
    PersonService(final PersonRepository repository, final PersonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Mono<PersonResponse> createPerson(final PersonRequest dto) {
        log.debug("create person '{}'", dto);
        final Person person = mapper.map(dto);
        return repository.save(person)
                .map(mapper::map);
    }

    public Mono<PersonResponse> getPerson(final String id) {
        log.debug("get person '{}'", id);
        return repository.findById(id)
                .switchIfEmpty(NOT_FOUND)
                .map(mapper::map);
    }

    public Mono<PersonResponse> updatePerson(final String id, final PersonRequest dto) {
        log.debug("update person '{}' with '{}'", id, dto);
        return repository.findById(id)
                .switchIfEmpty(NOT_FOUND)
                .doOnNext(person -> mapper.merge(person, dto))
                .doOnNext(repository::save)
                .map(mapper::map);
    }

    public Flux<PersonResponse> searchPersons(final PersonRequest dto) {
        log.debug("search persons '{}'", dto);
        final Person person = mapper.map(dto);
        return repository.findAll(Example.of(person))
                .map(mapper::map);
    }

    public Mono<PersonResponse> deletePerson(final String id) {
        log.debug("delete person '{}'", id);
        return repository.findById(id)
                .switchIfEmpty(NOT_FOUND)
                .doOnNext(repository::delete)
                .map(mapper::map);
    }

    public void deletePersons() {
        repository.deleteAll()
                .block();
    }
}
