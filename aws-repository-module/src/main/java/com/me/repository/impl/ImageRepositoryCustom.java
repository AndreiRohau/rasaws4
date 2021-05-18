package com.me.repository.impl;

import com.me.domain.Image;
import com.me.repository.ImageRepositoryWrapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Logger;

@Repository
public class ImageRepositoryCustom extends ImageRepositoryWrapper {
    private static Logger log = Logger.getLogger(ImageRepositoryCustom.class.getName());

    @Override
    public List<Image> getImages() {
        log.info("ImageRepositoryCustom#getImages()");
        return null;
    }

    @Override
    public Image getImageById(Long id) {
        log.info("ImageRepositoryCustom#getImageById()");
        return null;
    }

    @Override
    public Image saveImage(Image image) {
        log.info("ImageRepositoryCustom#saveImage()");
        return null;
    }

    @Override
    public Image updateImage(Image image) {
        log.info("ImageRepositoryCustom#updateImage()");
        return null;
    }

    @Override
    public void deleteImageById(Long id) {
        log.info("ImageRepositoryCustom#deleteImageById()");
    }

    @Override
    public Image getImageByName(String name) {
        log.info("ImageRepositoryCustom#getImageByName()");
        return null;
    }

    @Override
    public void deleteImageByName(String name) {
        log.info("ImageRepositoryCustom#deleteImageByName()");
    }
}
