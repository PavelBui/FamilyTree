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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private PersonMapper personMapper;
    private PersonRepository personRepository;
    private PhotoMapper photoMapper;

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
        PersonEntity atlasEntity = personRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        PersonEntity updatedAtlasEntity = personMapper.dtoToEntity(atlasEntity, personDto);
        personRepository.save(updatedAtlasEntity);
        return personMapper.entityToDto(updatedAtlasEntity);
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
    public List<PersonDto> getAllPersons() {
        return personRepository.findAllByIsDeletedFalse().stream()
                .map(entity -> personMapper.entityToDto(entity))
                .toList();
    }

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
}
