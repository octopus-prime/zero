package com.example.zero.controller;

import com.example.zero.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Persons")
@RestController
public class PersonController {

    private final PersonService service;

    @Autowired
    public PersonController(final PersonService service) {
        this.service = service;
    }

    @Operation(summary = "Create person")
    @PostMapping(path = "persons", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PersonResponse> createPerson(@Validated(CreateGroup.class) @RequestBody final Mono<PersonRequest> dto) {
        return service.createPerson(dto);
    }

    @Operation(summary = "Get person")
    @GetMapping(path = "persons/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<PersonResponse> getPerson(@PathVariable("id") final String id) {
        return service.getPerson(Mono.just(id));
    }

    @Operation(summary = "Update person")
    @PutMapping(path = "persons/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<PersonResponse> updatePerson(@PathVariable("id") final String id, @Validated @RequestBody final Mono<PersonRequest> dto) {
        return service.updatePerson(Mono.just(id), dto);
    }

    @Operation(summary = "Search persons")
    @PostMapping(path = "persons/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<PersonResponse> searchPersons(@Validated @RequestBody final Mono<PersonRequest> dto) {
        return service.searchPersons(dto);
    }

    @Operation(summary = "Delete person")
    @DeleteMapping(path = "persons/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<PersonResponse> deletePerson(@PathVariable("id") final String id) {
        return service.deletePerson(Mono.just(id));
    }
}
