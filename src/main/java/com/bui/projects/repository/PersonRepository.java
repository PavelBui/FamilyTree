package com.bui.projects.repository;

import com.bui.projects.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {

    Optional<PersonEntity> findByIdAndIsDeletedFalse(Integer id);

    List<PersonEntity> findAllByIsDeletedFalse();
}
