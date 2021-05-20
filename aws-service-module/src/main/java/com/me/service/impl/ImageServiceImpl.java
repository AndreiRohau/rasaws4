package com.me.service.impl;

import com.me.domain.Image;
import com.me.repository.ImageRepositoryRds;
import com.me.repository.ImageRepositoryS3;
import com.me.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class ImageServiceImpl implements ImageService {
    private static Logger log = Logger.getLogger(ImageServiceImpl.class.getName());
    private final ImageRepositoryS3 imageRepositoryS3;
    private final ImageRepositoryRds imageRepositoryRds;

    @Autowired
    public ImageServiceImpl(ImageRepositoryS3 imageRepositoryS3, ImageRepositoryRds imageRepositoryRds) {
        this.imageRepositoryS3 = imageRepositoryS3;
        this.imageRepositoryRds = imageRepositoryRds;
    }

    @Override
    public Image downloadImageByName(String fullName) {
        log.info("ImageServiceImpl#downloadImageByName()");
        return imageRepositoryS3.downloadImageByName(fullName);
    }

    @Override
    public List<Image> getMetadataImages() {
        log.info("ImageServiceImpl#getMetadataImages()");
        return imageRepositoryRds.findAll();
    }

    @Override
    public Image uploadImage(Image image) {
        log.info("ImageServiceImpl#uploadImage()");
        imageRepositoryRds.save(image);
        imageRepositoryS3.uploadImage(image);
        return image;
    }

    @Override
    public void deleteImageByName(String fullName) {
        log.info("ImageServiceImpl#deleteImageByName()");
        imageRepositoryRds.deleteById(fullName);
        imageRepositoryS3.deleteImageByName(fullName);
    }

    @Override
    public Image getRandomMetadataImage() {
        log.info("ImageServiceImpl#getRandomMetadataImage()");
        final List<Image> all = imageRepositoryRds.findAll();
        if (all.isEmpty()) {
            throw new RuntimeException("There are no images!!!");
        }
        final int randomIndex = getRandomNumberUsingNextInt(0, all.size());
        return all.get(randomIndex);
    }

    private int getRandomNumberUsingNextInt(int minIncl, int maxExcl) {
        return new Random().nextInt(maxExcl - minIncl) + minIncl;
    }
}
