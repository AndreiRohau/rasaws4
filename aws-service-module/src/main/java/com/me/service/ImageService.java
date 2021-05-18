package com.me.service;

import com.me.domain.Image;

import java.util.List;

public interface ImageService {
    List<Image> getImages();
    Image getImageById(Long id);
    Image saveImage(Image image);
    Image updateImage(Image image);
    void deleteImageById(Long id);

    Image getImageByName(String name);
    void deleteImageByName(String name);
}
