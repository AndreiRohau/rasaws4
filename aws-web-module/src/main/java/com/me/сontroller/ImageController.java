package com.me.сontroller;

import com.me.domain.Image;
import com.me.service.ImageService;
import com.me.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/images")
public class ImageController {
    private static Logger log = Logger.getLogger(ImageController.class.getName());
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/{fullName:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadImageByName(@PathVariable String fullName) {
        log.info("ImageController#downloadImageByName(), params=[" + fullName + "]");
        final InputStream content = imageService.downloadImageByName(fullName).getContent();
        return ResponseEntity.status(HttpStatus.OK)
                .body(Util.inputStreamToBytes(content));
    }

    @GetMapping
    public ResponseEntity<List<Image>> getMetadataImages() {
        log.info("ImageController#getMetadataImages()");
        final List<Image> images = imageService.getMetadataImages();
        return ResponseEntity.status(HttpStatus.OK)
                .body(images);
    }

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam Image image) {
        log.info("ImageController#uploadImage(), params=[" + image + "]");
        if (isNull(image.getContent())) {
            throw new NullPointerException("No image received!");
        }
        final Image uploadedImage = imageService.uploadImage(image);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadedImage.getFullName());
    }

    @DeleteMapping("/{fullName:.+}")
    public void deleteImageByName(@PathVariable String fullName) {
        log.info("ImageController#deleteImageByName(), params=[" + fullName + "]");
        imageService.deleteImageByName(fullName);
    }

    @GetMapping("/random-metadata")
    public ResponseEntity<Image> getRandomMetadataImage() {
        log.info("ImageController#getRandomMetadataImage()");
        final Image randomImage = imageService.getRandomMetadataImage();
        randomImage.setContent(null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(randomImage);
    }
}
