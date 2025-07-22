package com.bui.projects.repository;

import com.bui.projects.entity.RelationshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<RelationshipEntity, Integer> {

    List<RelationshipEntity> findAllByPersonId(Integer personId);

    List<RelationshipEntity> findAllByPersonIdAndRelationType(Integer personId, Integer relationType);

}
