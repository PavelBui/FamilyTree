package com.bui.projects.service;

import com.bui.projects.dto.PersonDto;
import com.bui.projects.dto.PhotoDto;

import java.util.List;

public interface PersonService {

    PersonDto createPerson(PersonDto personDto);

    PersonDto updatePerson(Integer id, PersonDto personDto);

    String deletePerson(Integer id);

    PersonDto getPerson(Integer id);

    List<PersonDto> getAllPersons();

    void uploadPhoto(Integer id, PhotoDto photoDto);

    PhotoDto getPhoto(Integer personId, Integer photoId);

    List<PhotoDto> getAllPhotos(Integer id);

    String addRelationship(Integer peronId, Integer relatedPersonId, Integer relationType);

    List<String> getKids(Integer id);
}
