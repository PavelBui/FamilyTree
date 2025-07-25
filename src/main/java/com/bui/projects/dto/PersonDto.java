package com.bui.projects.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
    @JsonIgnore
    private List<Integer> relationshipIds;
    @JsonIgnore
    private List<Integer> parentsIds;
    @JsonIgnore
    private List<Integer> kidsIds;
    @JsonIgnore
    private List<Integer> siblingsIds;
    @JsonIgnore
    private List<Integer> spousesIds;

    public String getFullDescription() {
        StringBuilder nameString = new StringBuilder();
        if (this.getLastName() != null) {
            nameString
                    .append(this.getLastName());
        }
        if (this.getMaidenName() != null) {
            nameString
                    .append(" (")
                    .append(this.getMaidenName())
                    .append(")");
        }
        if (this.getFirstName() != null) {
            nameString
                    .append(" ")
                    .append(this.getFirstName());
        }
        if (this.getMiddleName() != null) {
            nameString
                    .append(" ")
                    .append(this.getMiddleName());
        }
        if (nameString.isEmpty()) {
            nameString.append("Фамилия и имя неизвестны");
        }
        nameString.append("\n");
        StringBuilder dateString = new StringBuilder();
        if (this.getBirthDate() != null && this.getDeathDate() == null) {
            dateString
                    .append("род. ")
                    .append(this.getBirthDate());

        }
        if (this.getBirthPlace() != null) {
            dateString
                    .append(" (")
                    .append(this.getBirthPlace())
                    .append(")");

        }
        if (this.getBirthDate() != null && this.getDeathDate() != null) {
            dateString
                    .append(this.getBirthDate())
                    .append(" - ")
                    .append(this.getDeathDate());
        }
        if (this.getDeathPlace() != null) {
            dateString
                    .append(" (")
                    .append(this.getDeathPlace())
                    .append(")");

        }
        if (dateString.isEmpty()) {
            dateString.append("Даты жизни неизвестны");
        }
        dateString.append("\n");
        return nameString.toString() + dateString + this.getDescription();
    }

    public String getFullName(String personsType) {
        StringBuilder nameString = new StringBuilder();
        switch (personsType) {
            case "go_parents": nameString.append("M".equals(this.gender) ? "Папа " : "Мама "); break;
            case "go_kids": nameString.append("M".equals(this.gender) ? "Сын " : "Дочь "); break;
            case "go_siblings": nameString.append("M".equals(this.gender) ? "Брат " : "Сестра "); break;
            case "go_spouses": nameString.append("M".equals(this.gender) ? "Муж " : "Жена "); break;
        }
        if (this.getLastName() != null) {
            nameString
                    .append(this.getLastName());
        }
        if (this.getMaidenName() != null) {
            nameString
                    .append(" (")
                    .append(this.getMaidenName())
                    .append(")");
        }
        if (this.getFirstName() != null) {
            nameString
                    .append(" ")
                    .append(this.getFirstName());
        }
        if (this.getMiddleName() != null) {
            nameString
                    .append(" ")
                    .append(this.getMiddleName());
        }
        if (this.getBirthDate() != null && this.getDeathDate() == null) {
            nameString
                    .append(" (род. ")
                    .append(this.getBirthDate())
                    .append(")");

        }
        if (this.getBirthDate() != null && this.getDeathDate() != null) {
            nameString
                    .append(" (")
                    .append(this.getBirthDate())
                    .append(" - ")
                    .append(this.getDeathDate())
                    .append(")");
        }
        return nameString.toString();
    }
}
