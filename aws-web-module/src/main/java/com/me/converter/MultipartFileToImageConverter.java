package com.me.converter;

import com.me.domain.Image;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

public class MultipartFileToImageConverter implements Converter<MultipartFile, Image> {

    /*
     * Will convert only files, having content type starting with word [image]
     */
    private static final String IMAGE_MATCHER_REGEX = "^image.*$";

    @Override
    public Image convert(MultipartFile uploadingImage) throws ConversionException {
        try {
            validateFileOfTypeImage(uploadingImage);
            return convertMultipartFileToImage(uploadingImage);
        } catch (IOException e) {
            throw new ConversionNotSupportedException(uploadingImage, Image.class, e);
        }
    }

    private Image convertMultipartFileToImage(MultipartFile uploadingImage) throws IOException {
        final String fullName = uploadingImage.getOriginalFilename();
        final String name = getFileName(uploadingImage);
        final String extension = getExtension(uploadingImage);
        final Long fileSize = uploadingImage.getSize();
        final LocalDateTime lastModifiedDate = LocalDateTime.now(ZoneOffset.UTC);
        final InputStream content = uploadingImage.getInputStream();
        return new Image(fullName, name, extension, fileSize, lastModifiedDate, content);
    }

    private String getFileName(MultipartFile uploadingImage) {
        final String[] array = uploadingImage.getOriginalFilename().split("\\.");
        final String[] newArray = Arrays.copyOf(array, array.length - 1);
        return String.join(".", newArray);
    }

    private String getExtension(MultipartFile uploadingImage) {
        final String[] array = uploadingImage.getOriginalFilename().split("\\.");
        return array[array.length - 1];
    }

    private void validateFileOfTypeImage(MultipartFile uploadingImage) throws ConversionException {
        if (!uploadingImage.getContentType().matches(IMAGE_MATCHER_REGEX)) {
            throw new ConversionNotSupportedException(uploadingImage, Image.class, null);
        }
    }

}
