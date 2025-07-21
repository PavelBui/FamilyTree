package com.bui.projects.exeption;

public class PersonNotFoundException extends  RuntimeException{

    public PersonNotFoundException(Integer id) {
        super("Person with id: " + id + " not found");
    }
}
