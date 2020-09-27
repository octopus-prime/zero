package com.example.zero.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
public class PersonResponse {

    @NotNull
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
