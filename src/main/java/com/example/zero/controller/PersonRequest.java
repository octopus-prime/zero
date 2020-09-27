package com.example.zero.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
public class PersonRequest {

    @NotNull(groups = CreateGroup.class)
    private String vorname;

    @NotNull(groups = CreateGroup.class)
    private String nachname;

    @NotNull(groups = CreateGroup.class)
    private String strasse;

    @NotNull(groups = CreateGroup.class)
    private String hausnummer;

    @NotNull(groups = CreateGroup.class)
//    @Size(min = 5, max = 5, groups = CreateGroup.class)
    @Digits(integer = 5, fraction = 0, groups = CreateGroup.class)
    private String plz;

    @NotNull(groups = CreateGroup.class)
    private String wohnort;
}
