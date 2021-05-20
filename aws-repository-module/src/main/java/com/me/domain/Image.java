package com.me.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.InputStream;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "images")
@NoArgsConstructor
public class Image {
    @Id
    private String fullName;
    private String name;
    private String extension;
    private Long fileSize; // in bytes
    private LocalDateTime lastModifiedDate; // LocalDateTime.now(ZoneOffset.UTC)
    @JsonIgnore
    @Transient
    private InputStream content;

    public Image(String fullName, String name, String extension, Long fileSize, LocalDateTime lastModifiedDate, InputStream content) {
        this.fullName = fullName;
        this.name = name;
        this.extension = extension;
        this.fileSize = fileSize;
        this.lastModifiedDate = lastModifiedDate;
        this.content = content;
    }
}
