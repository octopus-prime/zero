package com.example.zero.controller;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@Jacksonized
public class PersonRequest {

    @NotNull(groups = CreateGroup.class)
    private final String vorname;

    @NotNull(groups = CreateGroup.class)
    private final String nachname;

    @NotNull(groups = CreateGroup.class)
    private final String strasse;

    @NotNull(groups = CreateGroup.class)
    private final String hausnummer;

    @NotNull(groups = CreateGroup.class)
    @Digits(integer = 5, fraction = 0, groups = CreateGroup.class)
    private final String plz;

    @NotNull(groups = CreateGroup.class)
    private final String wohnort;
}
