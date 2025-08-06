package com.bui.projects.mapper;

import com.bui.projects.dto.PersonDto;
import com.bui.projects.entity.PersonEntity;
import com.bui.projects.entity.PhotoEntity;
import com.bui.projects.service.RelationshipService;
import com.bui.projects.util.DateTimeUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class PersonMapper {

    private RelationshipService relationshipService;

    public PersonEntity dtoToEntity(PersonDto personDto) {
        PersonEntity personEntity = new PersonEntity();
        return dtoToEntity(personEntity, personDto);
    }

    public PersonEntity dtoToEntity(PersonEntity personEntity, PersonDto personDto) {
        personEntity.setFirstName(personDto.getFirstName());
        personEntity.setLastName(personDto.getLastName());
        personEntity.setMiddleName(personDto.getMiddleName());
        personEntity.setMaidenName(personDto.getMaidenName());
        personEntity.setBirthDate(personDto.getBirthDate());
        personEntity.setBirthPlace(personDto.getBirthPlace());
        personEntity.setDeathDate(personDto.getDeathDate());
        personEntity.setDeathPlace(personDto.getDeathPlace());
        personEntity.setGender(personDto.getGender());
        personEntity.setDescription(personDto.getDescription());
        personEntity.setChatId(personDto.getChatId());
        personEntity.setUpdatedAt(LocalDateTime.now());
        return personEntity;
    }

    public PersonDto entityToDto(PersonEntity personEntity) {
        List<Integer> photoIdList = new ArrayList<>();
        List<PhotoEntity> imageEntitySet = personEntity.getPhotoEntities();
        if (imageEntitySet != null) {
            photoIdList = imageEntitySet.stream()
                    .map(PhotoEntity::getId)
                    .toList();
        }
        return PersonDto.builder()
                .id(personEntity.getId())
                .firstName(personEntity.getFirstName())
                .lastName(personEntity.getLastName())
                .middleName(personEntity.getMiddleName())
                .maidenName(personEntity.getMaidenName())
                .birthDate(personEntity.getBirthDate())
                .birthPlace(personEntity.getBirthPlace())
                .deathDate(personEntity.getDeathDate())
                .deathPlace(personEntity.getDeathPlace())
                .gender(personEntity.getGender())
                .description(personEntity.getDescription())
                .chatId(personEntity.getChatId())
                .updatedAt(DateTimeUtils.convertDateTimeToTimestamp(personEntity.getUpdatedAt()))
                .photoIds(photoIdList)
                .defaultPhotoId(personEntity.getDefaultPhoto() != null ? personEntity.getDefaultPhoto().getId() : null)
                .relationshipIds(relationshipService.getRelationships(personEntity.getId()))
                .parentsIds(relationshipService.getParents(personEntity.getId()))
                .kidsIds(relationshipService.getKids(personEntity.getId()))
                .siblingsIds(relationshipService.getSiblings(personEntity.getId()))
                .spousesIds(relationshipService.getSpouses(personEntity.getId()))
                .build();
    }
}
