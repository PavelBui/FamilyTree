package com.bui.projects.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "person")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "maiden_name")
    private String maidenName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "birth_place")
    private String birthPlace;

    @Column(name = "death_date")
    private LocalDate deathDate;

    @Column(name = "death_place")
    private String deathPlace;

    @Column(name = "gender", length = 1)
    private String gender;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deleteAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "default_photo_id")
    private PhotoEntity defaultPhoto;

    @Column
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "persons_photos")
    private Set<PhotoEntity> photoEntities;
}
