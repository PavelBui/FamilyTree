package com.bui.projects.exeption;

public class PhotoNotFoundException extends  RuntimeException{

    public PhotoNotFoundException(Integer personId, Integer photoId) {
        super("Photo with id: " + photoId + " not found for person with id: " + personId);
    }
}
