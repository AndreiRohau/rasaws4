package com.me.repository;

import com.me.domain.Image;

public interface ImageRepositoryS3 {
    Image downloadImageByName(String fullName);
    Image uploadImage(Image image);
    void deleteImageByName(String fullName);
}
