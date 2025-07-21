package com.bui.projects.mapper;

import com.bui.projects.dto.PhotoDto;
import com.bui.projects.entity.PhotoEntity;
import com.bui.projects.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PhotoMapper {

    private PhotoRepository photoRepository;

    public PhotoEntity dtoToEntity(PhotoDto photoDto) {
        return photoRepository.findByPath(photoDto.getPath())
                        .orElse(new PhotoEntity(photoDto.getName(), photoDto.getPath(), photoDto.getPhotoBytes()));
    }

    public PhotoDto entityToDto(PhotoEntity photoEntity) {
        return PhotoDto.builder()
                .name(photoEntity.getName())
                .path(photoEntity.getPath())
                .photoBytes(photoEntity.getPhotoData())
                .build();
    }
}
