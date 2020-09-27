package com.example.zero.service;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface PersonRepository extends ReactiveMongoRepository<Person, String> {
}
