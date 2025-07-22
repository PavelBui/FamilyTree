package com.bui.projects.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "relationship")
public class RelationshipEntity {

    public RelationshipEntity(Integer personId, Integer relatedPersonId, Integer relationType) {
        this.personId = personId;
        this.relatedPersonId = relatedPersonId;
        this.relationType = relationType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "related_person_id")
    private Integer relatedPersonId;

    @Column(name = "relation_type")
    private Integer relationType;
}
