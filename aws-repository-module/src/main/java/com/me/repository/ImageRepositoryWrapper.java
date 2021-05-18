package com.me.repository;

import com.me.domain.Image;

import java.util.List;

public abstract class ImageRepositoryWrapper {
    public abstract List<Image> getImages();
    public abstract Image getImageById(Long id);
    public abstract Image saveImage(Image image);
    public abstract Image updateImage(Image image);
    public abstract void deleteImageById(Long id);

    public abstract Image getImageByName(String name);
    public abstract void deleteImageByName(String name);
}
