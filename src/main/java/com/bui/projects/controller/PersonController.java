package com.bui.projects.controller;

import com.bui.projects.dto.PersonDto;
import com.bui.projects.dto.PhotoDto;
import com.bui.projects.service.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/person")
@Api(tags = "Person Endpoints")
public class PersonController {

    private static final String PREFIX_PATH = "photos/";

    private final PersonService personService;

    @PostMapping
    @ApiOperation("Create Person")
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto) {
        return ResponseEntity.ok(personService.createPerson(personDto));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update Person by id")
    public ResponseEntity<PersonDto> updatePerson(@PathVariable Integer id, @RequestBody PersonDto personDto) {
        return ResponseEntity.ok(personService.updatePerson(id, personDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete Person by id")
    public ResponseEntity<String> deletePerson(@PathVariable Integer id) {
        return ResponseEntity.ok(personService.deletePerson(id));
    }

    @GetMapping("/{id}")
    @ApiOperation("Get Person by id")
    public ResponseEntity<PersonDto> getPerson(@PathVariable Integer id) {
        return ResponseEntity.ok(personService.getPerson(id));
    }

    @GetMapping
    @ApiOperation("Get list of all Persons")
    public ResponseEntity<List<PersonDto>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    //Photos
    @PostMapping("/{id}/photo")
    @ApiOperation("Upload Photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Integer id, @RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Photo is empty");
        }

        if (!"image/jpeg".equalsIgnoreCase(Objects.requireNonNull(file.getContentType()))) {
            return ResponseEntity.badRequest().body("Only JPG files are allowed");
        }

        try {
            PhotoDto photoDto = PhotoDto.builder()
                    .name(file.getOriginalFilename())
                    .path(PREFIX_PATH + file.getOriginalFilename())
                    .photoBytes(file.getBytes())
                    .build();
            personService.uploadPhoto(id, photoDto);
            return ResponseEntity.ok("Photo was uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process the file: " + file.getOriginalFilename());
        }
    }

    @GetMapping("/{id}/photo/{photoId}")
    @ApiOperation("Get Photo by photoId for Person by id")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Integer id, @PathVariable Integer photoId) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(personService.getPhoto(id, photoId).getPhotoBytes());
    }

    @GetMapping("/{id}/photo")
    @ApiOperation("Get all photos for Person by id")
    public ResponseEntity<List<PhotoDto>> getAllPhotos(@PathVariable Integer id) {
        return ResponseEntity.ok(personService.getAllPhotos(id));
    }

    //Relationships
    @GetMapping("/{id}/kids")
    @ApiOperation("Get kids for Person by id")
    public ResponseEntity<List<String>> getKids(@PathVariable Integer id) {
        return ResponseEntity.ok(personService.getKids(id));
    }

}
