package com.me.сontroller;

import com.me.domain.Image;
import com.me.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/images")
public class ImageController {
    private static Logger log = Logger.getLogger(ImageController.class.getName());
    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<List<Image>> getImages() {
        log.info("ImageController#getImages()");
        final List<Image> images = imageService.getImages();
        return ResponseEntity.status(HttpStatus.OK)
                .body(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) {
        log.info("ImageController#getImageById(), params=[" + id + "]");
        final Image imageById = imageService.getImageById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageById);
    }

    @PostMapping
    public ResponseEntity<Image> saveImage(Image image) {
        log.info("ImageController#saveImage(), params=[" + image + "]");
        final Image savedImage = imageService.saveImage(image);
        return ResponseEntity.status(HttpStatus.OK)
                .body(savedImage);
    }

    @PutMapping
    public ResponseEntity<Image> updateImage(Image image) {
        log.info("ImageController#updateImage(), params=[" + image + "]");
        final Image updatedImage = imageService.updateImage(image);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedImage);
    }

    @DeleteMapping("/{id}")
    public void deleteImageById(@PathVariable Long id) {
        log.info("ImageController#deleteImageById(), params=[" + id + "]");
        imageService.deleteImageById(id);
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Image> getImageByName(@PathVariable String name) {
        log.info("ImageController#getImageByName(), params=[" + name + "]");
        final Image imageByName = imageService.getImageByName(name);
        return ResponseEntity.status(HttpStatus.OK)
                .body(imageByName);
    }

    @DeleteMapping("/by-name/{name}")
    public void deleteImageByName(@PathVariable String name) {
        log.info("ImageController#deleteImageByName(), params=[" + name + "]");
        imageService.deleteImageByName(name);
    }
}
