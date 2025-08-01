package com.bui.projects.repository;

import com.bui.projects.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Integer> {

        Optional<PhotoEntity> findByPath(String path);
}
