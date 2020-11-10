package com.example.zero.service;

import com.example.zero.controller.PersonRequest;
import com.example.zero.controller.PersonResponse;
import org.mapstruct.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Mapper
interface PersonMapper {

    @Retention(RetentionPolicy.CLASS)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "changedAt", ignore = true)
    @interface DocumentMapping {
    }

    @DocumentMapping
    Person map(PersonRequest dto);

    @DocumentMapping
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(@MappingTarget Person entity, PersonRequest dto);

    PersonResponse map(Person entity);
}
