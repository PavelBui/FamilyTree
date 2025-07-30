package com.bui.projects.service.impl;

import com.bui.projects.entity.RelationshipEntity;
import com.bui.projects.repository.RelationshipRepository;
import com.bui.projects.service.RelationshipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.bui.projects.telegram.util.Constants.*;

@Service
@AllArgsConstructor
public class RelationshipServiceImpl implements RelationshipService {

    private RelationshipRepository relationshipRepository;

    @Override
    @Transactional
    public List<Integer> getRelationships(Integer peronId) {
        return relationshipRepository.findAllByPersonId(peronId).stream()
                .map(RelationshipEntity::getRelatedPersonId)
                .toList();
    }

    @Override
    @Transactional
    public List<Integer> getParents(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(FATHER_TYPE, MOTHER_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getKids(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(SON_TYPE, DAUGHTER_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getSiblings(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(BROTHER_TYPE, SISTER_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getSpouses(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(HUSBAND_TYPE, WIFE_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getFathers(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(FATHER_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getMothers(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(MOTHER_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getSons(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(SON_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getDaughters(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(DAUGHTER_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getBrothers(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(BROTHER_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getSisters(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(SISTER_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getHusbands(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(HUSBAND_TYPE));
    }

    @Override
    @Transactional
    public List<Integer> getWifies(Integer peronId) {
        return getRelationshipIdsByType(peronId, List.of(WIFE_TYPE));
    }

    @Override
    @Transactional
    public boolean createRelationship(Integer peronId, Integer relatedPersonId, Integer relationType) {
        try {
            RelationshipEntity relationshipEntity = new RelationshipEntity(peronId, relatedPersonId, relationType);
            RelationshipEntity savedRelationshipEntity = relationshipRepository.save(relationshipEntity);
            return savedRelationshipEntity.getId() != null;
        } catch (Exception e) {
            return false;
        }
    }

    private List<Integer> getRelationshipIdsByType(Integer peronId, List<Integer> relationTypes) {
        return relationshipRepository.findAllByPersonIdAndRelationTypeIn(peronId, relationTypes).stream()
                .map(RelationshipEntity::getRelatedPersonId)
                .toList();
    }
}
