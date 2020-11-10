package com.example.zero.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@Document
class Person {

    @Id
    private String id;

    @Version
    private Long version;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime changedAt;

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
