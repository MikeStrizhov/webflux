package com.mgs.webflux.repository;

import com.mgs.webflux.model.Person;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends ReactiveMongoRepository<Person, String> {
}
