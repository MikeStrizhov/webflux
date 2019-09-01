package com.mgs.webflux.controller;

import com.mgs.webflux.model.Person;
import com.mgs.webflux.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class PersonController {

    @Autowired
    private PersonRepo personRepo;

    @GetMapping("/persons")
    public Flux<Person> getAllPersons(){
        return personRepo.findAll();
    }

    @PostMapping("/persons/{id}")
    public Mono<Person> createPerson(@Valid @RequestBody Person person){
        return personRepo.save(person);
    }

    @GetMapping("/persons/{id}")
    public Mono<ResponseEntity<Person>> getPersonById(@PathVariable(value = "id") String personId, @Valid @RequestBody Person person){
        return personRepo.findById(personId)
                .map(foundPerson-> ResponseEntity.ok(foundPerson))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/persons/{id}")
    public Mono<ResponseEntity<Person>> updatePerson(@PathVariable(value = "id") String personId, @Valid @RequestBody Person person){
        return personRepo.findById(personId)
                .flatMap(existingPerson->{
                    existingPerson.setName(person.getName());
                    return personRepo.save(existingPerson);
                })
                .map(updatedPerson-> new ResponseEntity<>(updatedPerson, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("/persons/{id}")
    public Mono<ResponseEntity<Void>> deletePerson(@PathVariable(value = "id") String personId, @Valid @RequestBody Person person){
        return personRepo.findById(personId)
                .flatMap(existingPerson-> personRepo.delete(existingPerson)
                    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "stream/persons", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Person> streamAllPersons(){
        return personRepo.findAll();
    }
}
