package com.example.zero.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
@Document
class Person {

    @Id
    private String id;

    @NotNull
    private String vorname;

    @NotNull
    private String nachname;

    @NotNull
    private String strasse;

    @NotNull
    private String hausnummer;

    @NotNull
    private String plz;

    @NotNull
    private String wohnort;
}
