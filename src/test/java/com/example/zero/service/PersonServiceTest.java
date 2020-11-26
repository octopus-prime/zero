package com.example.zero.service;

import com.example.zero.controller.PersonRequest;
import com.example.zero.controller.PersonResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static reactor.test.StepVerifier.create;

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
        void success(@Mock final Person person1, @Mock final Person person2, @Mock final PersonResponse response) {
            given(mapper.map(same(request))).willReturn(person1);
            given(repository.save(same(person1))).willReturn(Mono.just(person2));
            given(mapper.map(same(person2))).willReturn(response);

            create(service.createPerson(Mono.just(request)))
                    .expectNext(response)
                    .expectComplete()
                    .verify();
        }

        @Test
        void error(@Mock final Person person) {
            given(mapper.map(same(request))).willReturn(person);
            given(repository.save(same(person))).willThrow(IllegalArgumentException.class);

            create(service.createPerson(Mono.just(request)))
                    .expectError(IllegalArgumentException.class)
                    .verify();
        }
    }

    @Nested
    class GetPerson {

        private final String id = UUID.randomUUID().toString();

        @Test
        void success(@Mock final Person person, @Mock final PersonResponse response) {
            given(repository.findById(same(id)))
                    .willReturn(Mono.just(person));
            given(mapper.map(same(person)))
                    .willReturn(response);

            create(service.getPerson(Mono.just(id)))
                    .expectNext(response)
                    .expectComplete()
                    .verify();
        }

        @Test
        void notFound() {
            given(repository.findById(same(id)))
                    .willReturn(Mono.empty());

            create(service.getPerson(Mono.just(id)))
                    .expectError(ResponseStatusException.class)
                    .verify();
        }

        @Test
        void error() {
            given(repository.findById(same(id)))
                    .willThrow(IllegalArgumentException.class);

            create(service.getPerson(Mono.just(id)))
                    .expectError(IllegalArgumentException.class)
                    .verify();
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

            create(service.searchPersons(Mono.just(request)))
                    .expectNext(response)
                    .expectComplete()
                    .verify();
        }

        @Test
        void notFound(@Mock final Person person) {
            given(mapper.map(same(request))).willReturn(person);
            given(repository.findAll(Example.of(person))).willReturn(Flux.empty());

            create(service.searchPersons(Mono.just(request)))
                    .expectComplete()
                    .verify();
        }

        @Test
        void error(@Mock final Person person) {
            given(mapper.map(same(request))).willReturn(person);
            given(repository.findAll(Example.of(person))).willThrow(IllegalArgumentException.class);

            create(service.searchPersons(Mono.just(request)))
                    .expectError(IllegalArgumentException.class)
                    .verify();
        }
    }

    @Nested
    class UpdatePerson {

        private final String id = UUID.randomUUID().toString();
        private final PersonRequest request = PersonRequest.builder().build();

        @Test
        void success(@Mock final Person person1, @Mock final Person person2, @Mock final Person person3, @Mock final PersonResponse response) {
            given(repository.findById(same(id))).willReturn(Mono.just(person1));
            given(mapper.merge(same(person1), same(request))).willReturn(person2);
            given(repository.save(same(person2))).willReturn(Mono.just(person3));
            given(mapper.map(same(person3))).willReturn(response);

            create(service.updatePerson(Mono.just(id), Mono.just(request)))
                    .expectNext(response)
                    .expectComplete()
                    .verify();
        }

        @Test
        void notFound() {
            given(repository.findById(same(id))).willReturn(Mono.empty());

            create(service.updatePerson(Mono.just(id), Mono.just(request)))
                    .expectError(ResponseStatusException.class)
                    .verify();
        }

        @Test
        void error() {
            given(repository.findById(same(id))).willThrow(IllegalArgumentException.class);

            create(service.updatePerson(Mono.just(id), Mono.just(request)))
                    .expectError(IllegalArgumentException.class)
                    .verify();
        }
    }

    @Nested
    class DeletePerson {

        private final String id = UUID.randomUUID().toString();

        @Test
        void success(@Mock final Person person, @Mock final PersonResponse response) {
            given(repository.findById(same(id))).willReturn(Mono.just(person));
            given(mapper.map(same(person))).willReturn(response);

            create(service.deletePerson(Mono.just(id)))
                    .expectNext(response)
                    .expectComplete()
                    .verify();
            verify(repository).delete(same(person));
        }

        @Test
        void notFound() {
            given(repository.findById(same(id)))
                    .willReturn(Mono.empty());

            create(service.deletePerson(Mono.just(id)))
                    .expectError(ResponseStatusException.class)
                    .verify();
            verify(repository, never())
                    .delete(any());
        }

        @Test
        void error() {
            given(repository.findById(same(id)))
                    .willThrow(IllegalArgumentException.class);

            create(service.deletePerson(Mono.just(id)))
                    .expectError(IllegalArgumentException.class)
                    .verify();
            verify(repository, never())
                    .delete(any());
        }
    }

    @Nested
    class DeletePersons {

        @Test
        void success() {
            given(repository.deleteAll())
                    .willReturn(Mono.empty());

            create(service.deletePersons())
                    .expectComplete()
                    .verify();
        }
    }
}
