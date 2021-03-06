package com.me.service;

import com.me.domain.Image;

import java.util.List;

public interface ImageService {
    Image downloadImageByName(String fullName);
    List<Image> getMetadataImages();
    Image uploadImage(Image image);
    void deleteImageByName(String fullName);
    Image getRandomMetadataImage();
}
