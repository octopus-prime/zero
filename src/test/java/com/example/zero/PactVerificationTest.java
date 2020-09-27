package com.example.zero;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.example.zero.controller.dto.PersonRequest;
import com.example.zero.controller.dto.PersonResponse;
import com.example.zero.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Provider("person-service")
@PactFolder("src/test/resources/pacts")
class PactVerificationTest {

    private static final PersonRequest PERSON_1 = PersonRequest.builder()
            .vorname("Max")
            .nachname("Mustermann")
            .strasse("Musterstrasse")
            .hausnummer("123a")
            .plz("12345")
            .wohnort("Musterort")
            .build();
    private static final PersonRequest PERSON_2 = PersonRequest.builder()
            .vorname("Max")
            .nachname("Mustermann")
            .strasse("Musterstrasse")
            .hausnummer("234a")
            .plz("23456")
            .wohnort("Musterort")
            .build();

    @Autowired
    private PersonService service;

    @BeforeEach
    void setUp(@LocalServerPort final int port, final PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verify(final PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void setUp() {
        service.deletePersons();
    }

    @State("get person")
    Map<String, Object> getPerson() {
        final PersonResponse person = service.createPerson(PERSON_1).block();
        return Map.of("id", person.getId());
    }

    @State("search persons")
    Map<String, Object> searchPersons() {
        final PersonResponse person1 = service.createPerson(PERSON_1).block();
        final PersonResponse person2 = service.createPerson(PERSON_2).block();
        return Map.of("id1", person1.getId(), "id2", person2.getId());
    }
}
