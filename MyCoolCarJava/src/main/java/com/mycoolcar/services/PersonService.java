package com.mycoolcar.services;

import com.mycoolcar.dtos.PersonCreationDto;
import com.mycoolcar.entities.PersonEntity;
import com.mycoolcar.exceptions.PersonFoundException;
import com.mycoolcar.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService{
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<PersonEntity> get(String name) {
        return personRepository.findAllByName(name);
    }
    public Optional <PersonEntity> createPerson(PersonCreationDto personCreationDto) {
        personRepository.findByEmail(personCreationDto.email()).ifPresent(person -> {
            throw new PersonFoundException("User with ID: " + person.getEmail() + " is ffound");
        });
        PersonEntity person = new PersonEntity();
        person.setName(personCreationDto.name());
        person.setEmail(personCreationDto.email());
        return Optional.of(personRepository.save(person));
    }


}
