package com.bui.projects.service.impl;

import com.bui.projects.entity.RelationshipEntity;
import com.bui.projects.repository.RelationshipRepository;
import com.bui.projects.service.RelationshipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class RelationshipServiceImpl implements RelationshipService {

    private static final Integer FATHER_TYPE = 1;
    private static final Integer MOTHER_TYPE = 2;
    private static final Integer SON_TYPE = 5;
    private static final Integer DAUGHTER_TYPE = 6;
    private static final Integer BROTHER_TYPE = 9;
    private static final Integer SISTER_TYPE = 10;
    private static final Integer HUSBAND_TYPE = 11;
    private static final Integer WIFE_TYPE = 12;

    private RelationshipRepository relationshipRepository;

    @Override
    @Transactional
    public List<Integer> getRelationshipIds(Integer peronId) {
        return relationshipRepository.findAllByPersonId(peronId).stream()
                .map(RelationshipEntity::getRelatedPersonId)
                .toList();
    }

    @Override
    @Transactional
    public List<Integer> getFathers(Integer peronId) {
        return getRelationshipIdsByType(peronId, FATHER_TYPE);
    }

    @Override
    @Transactional
    public List<Integer> getMothers(Integer peronId) {
        return getRelationshipIdsByType(peronId, MOTHER_TYPE);
    }

    @Override
    @Transactional
    public List<Integer> getSons(Integer peronId) {
        return getRelationshipIdsByType(peronId, SON_TYPE);
    }

    @Override
    @Transactional
    public List<Integer> getDaughters(Integer peronId) {
        return getRelationshipIdsByType(peronId, DAUGHTER_TYPE);
    }

    @Override
    @Transactional
    public List<Integer> getBrothers(Integer peronId) {
        return getRelationshipIdsByType(peronId, BROTHER_TYPE);
    }

    @Override
    @Transactional
    public List<Integer> getSisters(Integer peronId) {
        return getRelationshipIdsByType(peronId, SISTER_TYPE);
    }

    @Override
    @Transactional
    public List<Integer> getHusbands(Integer peronId) {
        return getRelationshipIdsByType(peronId, HUSBAND_TYPE);
    }

    @Override
    @Transactional
    public List<Integer> getWifies(Integer peronId) {
        return getRelationshipIdsByType(peronId, WIFE_TYPE);
    }

    @Override
    @Transactional
    public boolean createRelationship(Integer peronId, Integer relatedPersonId, Integer relationType) {
        try {
            RelationshipEntity relationshipEntity = new RelationshipEntity(peronId, relatedPersonId, relationType);
            RelationshipEntity savedRelationshipEntity = relationshipRepository.save(relationshipEntity);
            return savedRelationshipEntity != null && savedRelationshipEntity.getId() != null;
        } catch (Exception e) {
            return false;
        }
    }

    private List<Integer> getRelationshipIdsByType(Integer peronId, Integer relationType) {
        return relationshipRepository.findAllByPersonIdAndRelationType(peronId, relationType).stream()
                .map(RelationshipEntity::getRelatedPersonId)
                .toList();
    }
}
