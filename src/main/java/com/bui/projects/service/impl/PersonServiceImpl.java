package com.bui.projects.service.impl;

import com.bui.projects.dto.PersonDto;
import com.bui.projects.dto.PhotoDto;
import com.bui.projects.entity.PersonEntity;
import com.bui.projects.entity.PhotoEntity;
import com.bui.projects.exeption.PersonNotFoundException;
import com.bui.projects.exeption.PhotoNotFoundException;
import com.bui.projects.mapper.PhotoMapper;
import com.bui.projects.service.PersonService;
import com.bui.projects.mapper.PersonMapper;
import com.bui.projects.repository.PersonRepository;
import com.bui.projects.service.RelationshipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private PersonMapper personMapper;
    private PersonRepository personRepository;
    private PhotoMapper photoMapper;
    private RelationshipService relationshipService;

    @Override
    @Transactional
    public PersonDto createPerson(PersonDto personDto) {
        PersonEntity personEntity = personMapper.dtoToEntity(personDto);
        personRepository.save(personEntity);
        return personMapper.entityToDto(personEntity);
    }

    @Override
    @Transactional
    public PersonDto updatePerson(Integer id, PersonDto personDto) {
        PersonEntity personEntity = personRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        PersonEntity updatedPersonEntity = personMapper.dtoToEntity(personEntity, personDto);
        personRepository.save(updatedPersonEntity);
        return personMapper.entityToDto(updatedPersonEntity);
    }

    @Override
    @Transactional
    public String deletePerson(Integer id) {
        PersonEntity personEntity = personRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        personEntity.setDeleteAt(LocalDateTime.now());
        personRepository.save(personEntity);
        return "Person was deleted successfully";
    }

    @Override
    @Transactional
    public PersonDto getPerson(Integer id) {
        return personMapper.entityToDto(personRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new PersonNotFoundException((id))));
    }

    @Override
    @Transactional
    public PersonDto getPersonByChatId(Long chatId) {
        Optional<PersonEntity> optionalPersonEntity = personRepository.findByChatIdAndIsDeletedFalse(chatId);
        return optionalPersonEntity.map(personEntity -> personMapper.entityToDto(personEntity)).orElse(null);
    }

    @Override
    @Transactional
    public List<PersonDto> getAllPersons() {
        return personRepository.findAllByIsDeletedFalse().stream()
                .map(entity -> personMapper.entityToDto(entity))
                .toList();
    }

    //Photos
    @Override
    @Transactional
    public void uploadPhoto(Integer id, PhotoDto photoDto) {
        PersonEntity personEntity = personRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new PersonNotFoundException((id)));
        personEntity.getPhotoEntities().add(photoMapper.dtoToEntity(photoDto));
        personRepository.save(personEntity);
    }

    @Override
    @Transactional
    public PhotoDto getPhoto(Integer personId, Integer photoId) {
        PersonEntity personEntity = personRepository.findByIdAndIsDeletedFalse(personId)
                .orElseThrow(() -> new PersonNotFoundException((personId)));
        PhotoEntity imageEntity = personEntity.getPhotoEntities().stream()
                .filter(entity -> entity.getId().equals(photoId))
                .findFirst().orElseThrow(() -> new PhotoNotFoundException(personId, photoId));
        return photoMapper.entityToDto(imageEntity);
    }

    @Override
    @Transactional
    public List<PhotoDto> getAllPhotos(Integer id) {
        PersonEntity personEntity = personRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new PersonNotFoundException((id)));
        return personEntity.getPhotoEntities().stream()
                .map(entity -> photoMapper.entityToDto(entity))
                .toList();
    }

    //Relationships
    @Override
    @Transactional
    public String addRelationship(Integer peronId, Integer relatedPersonId, Integer relationType) {
        PersonEntity personEntity = personRepository.findByIdAndIsDeletedFalse(peronId)
                .orElseThrow(() -> new PersonNotFoundException((peronId)));
        PersonEntity relatedPersonEntity = personRepository.findByIdAndIsDeletedFalse(relatedPersonId)
                .orElseThrow(() -> new PersonNotFoundException((relatedPersonId)));
        boolean result = relationshipService.createRelationship(personEntity.getId(), relatedPersonEntity.getId(), relationType);
        if (result) {
            return "Relationship was created successfully";
        } else {
            return "Relationship wasn't created";
        }
    }

    @Override
    @Transactional
    public List<String> getKids(Integer id) {
        List<Integer> sonList = relationshipService.getSons(id);
        List<String> resultList = new ArrayList<>(getPersonList(sonList, "сын"));
        List<Integer> daughtersList = relationshipService.getDaughters(id);
        resultList.addAll(getPersonList(daughtersList, "дочь"));
        return resultList;
    }

    private List<String> getPersonList(List<Integer> indexList, String personType) {
        List<String> resultList = new ArrayList<>();
        for (int index : indexList) {
            PersonEntity personEntity = personRepository.findByIdAndIsDeletedFalse(index)
                    .orElseThrow(() -> new PersonNotFoundException((index)));
            resultList.add(createPersonString(personEntity, personType));
        }
        return resultList;
    }

    private String createPersonString(PersonEntity personEntity, String personType) {
        StringBuilder resultString = new StringBuilder(personType);
        if (personEntity.getLastName() != null) {
            resultString
                    .append(" ")
                    .append(personEntity.getLastName());
        }
        if (personEntity.getMaidenName() != null) {
            resultString
                    .append(" (")
                    .append(personEntity.getMaidenName())
                    .append(")");
        }
        if (personEntity.getFirstName() != null) {
            resultString
                    .append(" ")
                    .append(personEntity.getFirstName());
        }
        if (personEntity.getMiddleName() != null) {
            resultString
                    .append(" ")
                    .append(personEntity.getMiddleName());
        }
        if (personEntity.getBirthDate() != null && personEntity.getDeathDate() == null) {
            resultString
                    .append(" (род. ")
                    .append(personEntity.getBirthDate())
                    .append(")");

        }
        if (personEntity.getBirthDate() != null && personEntity.getDeathDate() != null) {
            resultString
                    .append(" (")
                    .append(personEntity.getBirthDate())
                    .append(" - ")
                    .append(personEntity.getDeathDate())
                    .append(")");
        }
        return resultString.toString();
    }
}
