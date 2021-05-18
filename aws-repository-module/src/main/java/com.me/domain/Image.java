package com.me.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Image {
    @Id
    private Long id;
    private String name;
    private String extension;
    private String size; // in bytes
    private LocalDateTime lastUpdate;
}
