package com.example.zero.service;

import com.example.zero.controller.PersonRequest;
import com.example.zero.controller.PersonResponse;
import org.mapstruct.*;

@Mapper
interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    Person map(PersonRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Person entity, PersonRequest dto);

    PersonResponse map(Person entity);
}
