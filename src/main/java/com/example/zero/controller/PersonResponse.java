package com.example.zero.controller;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@Jacksonized
public class PersonResponse {

    @NotNull
    private final String id;

    @NotNull
    private final String vorname;

    @NotNull
    private final String nachname;

    @NotNull
    private final String strasse;

    @NotNull
    private final String hausnummer;

    @NotNull
    private final String plz;

    @NotNull
    private final String wohnort;
}
