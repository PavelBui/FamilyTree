package com.bui.projects.service;

import com.bui.projects.dto.PersonDto;
import com.bui.projects.dto.PhotoDto;

import java.util.List;

public interface PersonService {

    PersonDto createPerson(PersonDto personDto);

    PersonDto updatePerson(Integer id, PersonDto personDto);

    String deletePerson(Integer id);

    PersonDto getPerson(Integer id);

    void uploadPersonPhoto(Integer id, PhotoDto photoDto);

    PersonDto getPersonByChatId(Long chatId);

    List<PersonDto> getAllPersons();

    PhotoDto getPersonPhoto(Integer personId, Integer photoId);

    List<PhotoDto> getAllPersonPhotos(Integer id);

    String addRelationship(Integer peronId, Integer relatedPersonId, Integer relationType);

    List<String> getKids(Integer id);
}
