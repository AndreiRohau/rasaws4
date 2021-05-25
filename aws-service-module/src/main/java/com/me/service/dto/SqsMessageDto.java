package com.me.service.dto;

import com.me.domain.Image;
import com.me.service.enumType.EventEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SqsMessageDto {
    private String messageExplanation;
    private String imageSize;
    private String imageName;
    private String imageExtension;
    private String imageDownloadLink;

    public SqsMessageDto(EventEnum eventEnum, Image image, String resourceUrl) {
        this.messageExplanation = eventEnum.name();
        this.imageSize = String.valueOf(image.getFileSize());
        this.imageName = image.getName();
        this.imageExtension = image.getExtension();
        this.imageDownloadLink = resourceUrl;
    }
}
