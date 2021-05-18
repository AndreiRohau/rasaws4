package com.me.service.impl;

import com.me.domain.Image;
import com.me.repository.ImageRepositoryWrapper;
import com.me.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ImageServiceImpl implements ImageService {
    private static Logger log = Logger.getLogger(ImageServiceImpl.class.getName());
    @Autowired
    private ImageRepositoryWrapper imageRepositoryWrapper;

    @Override
    public List<Image> getImages() {
        log.info("ImageServiceImpl#getImages()");
        return imageRepositoryWrapper.getImages();
    }

    @Override
    public Image getImageById(Long id) {
        log.info("ImageServiceImpl#getImageById()");
        return imageRepositoryWrapper.getImageById(id);
    }

    @Override
    public Image saveImage(Image image) {
        log.info("ImageServiceImpl#saveImage()");
        return imageRepositoryWrapper.saveImage(image);
    }

    @Override
    public Image updateImage(Image image) {
        log.info("ImageServiceImpl#updateImage()");
        return imageRepositoryWrapper.updateImage(image);
    }

    @Override
    public void deleteImageById(Long id) {
        log.info("ImageServiceImpl#deleteImageById()");
        imageRepositoryWrapper.deleteImageById(id);
    }

    @Override
    public Image getImageByName(String name) {
        log.info("ImageServiceImpl#getImageByName()");
        return imageRepositoryWrapper.getImageByName(name);
    }

    @Override
    public void deleteImageByName(String name) {
        log.info("ImageServiceImpl#deleteImageByName()");
        imageRepositoryWrapper.deleteImageByName(name);
    }
}
