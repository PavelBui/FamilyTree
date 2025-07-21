package com.bui.projects.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photo")
public class PhotoEntity {

    public PhotoEntity(String name, String path, byte[] photoData) {
        this.name = name;
        this.path = path;
        this.photoData = photoData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @JsonIgnore
    @Lob
    @Column(name = "photo_data", length = 1000)
    private byte[] photoData;
}
