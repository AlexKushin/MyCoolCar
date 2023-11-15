package com.mycoolcar.controllers;

import com.mycoolcar.dtos.PersonCreationDto;
import com.mycoolcar.entities.PersonEntity;
import com.mycoolcar.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/persons")
    public ResponseEntity<PersonEntity> postPerson(PersonCreationDto personCreationDto) {
        Optional<PersonEntity> result = personService.createPerson(personCreationDto);
        return result.isEmpty() ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(result.get(), HttpStatus.CREATED);
    }

    @GetMapping("/persons")
    public ResponseEntity<PersonCreationDto> getPerson() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/persons")
    public ResponseEntity<PersonCreationDto> putPerson() {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
