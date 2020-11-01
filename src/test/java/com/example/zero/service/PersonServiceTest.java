package com.example.zero.service;

import com.example.zero.controller.PersonRequest;
import com.example.zero.controller.PersonResponse;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository repository;

    @Mock
    private PersonMapper mapper;

    @Nested
    class CreatePerson {

        private final PersonRequest request = PersonRequest.builder().build();

        @Test
        void success(@Mock final Person person, @Mock final PersonResponse response) {
            given(mapper.map(same(request))).willReturn(person);
            given(repository.save(same(person))).willReturn(Mono.just(person));
            given(mapper.map(same(person))).willReturn(response);

            final PersonResponse result = service.createPerson(request).block();

            then(result).isSameAs(response);
        }

        @Test
        void error(@Mock final Person person) {
            given(mapper.map(same(request))).willReturn(person);
            given(repository.save(same(person))).willThrow(IllegalArgumentException.class);

            final ThrowingCallable createPerson = () -> service.createPerson(request).block();

            thenThrownBy(createPerson).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class GetPerson {

        private final String id = UUID.randomUUID().toString();

        @Test
        void success(@Mock final Person person, @Mock final PersonResponse response) {
            given(repository.findById(same(id))).willReturn(Mono.just(person));
            given(mapper.map(same(person))).willReturn(response);

            final PersonResponse result = service.getPerson(id).block();

            then(result).isSameAs(response);
        }

        @Test
        void notFound() {
            given(repository.findById(same(id))).willReturn(Mono.empty());

            final ThrowingCallable getPerson = () -> service.getPerson(id).block();

            thenThrownBy(getPerson).isInstanceOf(ResponseStatusException.class).hasMessage("404 NOT_FOUND");
        }

        @Test
        void error() {
            given(repository.findById(same(id))).willThrow(IllegalArgumentException.class);

            final ThrowingCallable getPerson = () -> service.getPerson(id).block();

            thenThrownBy(getPerson).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class SearchPersons {

        private final PersonRequest request = PersonRequest.builder().build();

        @Test
        void success(@Mock final Person person, @Mock final PersonResponse response) {
            given(mapper.map(same(request))).willReturn(person);
            given(repository.findAll(Example.of(person))).willReturn(Flux.just(person));
            given(mapper.map(same(person))).willReturn(response);

            final Iterable<PersonResponse> result = service.searchPersons(request).toIterable();

            then(result).contains(response);
        }

        @Test
        void notFound(@Mock final Person person) {
            given(mapper.map(same(request))).willReturn(person);
            given(repository.findAll(Example.of(person))).willReturn(Flux.empty());

            final Iterable<PersonResponse> result = service.searchPersons(request).toIterable();

            then(result).isEmpty();
        }

        @Test
        void error(@Mock final Person person) {
            given(mapper.map(same(request))).willReturn(person);
            given(repository.findAll(Example.of(person))).willThrow(IllegalArgumentException.class);

            final ThrowingCallable searchPersons = () -> service.searchPersons(request).blockFirst();

            thenThrownBy(searchPersons).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class UpdatePerson {

        private final String id = UUID.randomUUID().toString();
        private final PersonRequest request = PersonRequest.builder().build();

        @Test
        void success(@Mock final Person person, @Mock final PersonResponse response) {
            given(repository.findById(same(id))).willReturn(Mono.just(person));
            given(repository.save(same(person))).willReturn(Mono.just(person));
            given(mapper.map(same(person))).willReturn(response);

            final PersonResponse result = service.updatePerson(id, request).block();

            then(result).isSameAs(response);
            verify(mapper).merge(same(person), same(request));
        }

        @Test
        void notFound() {
            given(repository.findById(same(id))).willReturn(Mono.empty());

            final ThrowingCallable updatePerson = () -> service.updatePerson(id, request).block();

            thenThrownBy(updatePerson).isInstanceOf(ResponseStatusException.class).hasMessage("404 NOT_FOUND");
        }

        @Test
        void error() {
            given(repository.findById(same(id))).willThrow(IllegalArgumentException.class);

            final ThrowingCallable updatePerson = () -> service.updatePerson(id, request).block();

            thenThrownBy(updatePerson).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class DeletePerson {

        private final String id = UUID.randomUUID().toString();

        @Test
        void success(@Mock final Person person, @Mock final PersonResponse response) {
            given(repository.findById(same(id))).willReturn(Mono.just(person));
            given(mapper.map(same(person))).willReturn(response);

            final PersonResponse result = service.deletePerson(id).block();

            then(result).isSameAs(response);
            verify(repository).delete(same(person));
        }

        @Test
        void notFound() {
            given(repository.findById(same(id))).willReturn(Mono.empty());

            final ThrowingCallable deletePerson = () -> service.deletePerson(id).block();

            thenThrownBy(deletePerson).isInstanceOf(ResponseStatusException.class).hasMessage("404 NOT_FOUND");
        }

        @Test
        void error() {
            given(repository.findById(same(id))).willThrow(IllegalArgumentException.class);

            final ThrowingCallable deletePerson = () -> service.deletePerson(id).block();

            thenThrownBy(deletePerson).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class DeletePersons {

        @Test
        void success(@Mock final Mono<Void> nothing) {
            given(repository.deleteAll()).willReturn(nothing);

            final Mono<Void> result = service.deletePersons();

            then(result).isSameAs(nothing);
        }

        @Test
        void error() {
            given(repository.deleteAll()).willThrow(IllegalArgumentException.class);

            final ThrowingCallable deletePersons = () -> service.deletePersons();

            thenThrownBy(deletePersons).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
