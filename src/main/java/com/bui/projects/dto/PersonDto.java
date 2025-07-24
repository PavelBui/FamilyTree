package com.bui.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    private Integer id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String maidenName;
    private String birthDate;
    private String birthPlace;
    private String deathDate;
    private String deathPlace;
    private String gender;
    private String description;
    private Long chatId;
    private Long updatedAt;
    private List<Integer> photoIds;
    private Integer defaultPhotoId;
    private List<Integer> relationshipIds;
}
