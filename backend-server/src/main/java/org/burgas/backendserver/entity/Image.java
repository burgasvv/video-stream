package org.burgas.backendserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
public final class Image {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String contentType;
    private Long size;
    private String format;

    @JsonIgnore
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Image image;

        public Builder() {
            image = new Image();
        }

        public Builder id(Long id) {
            this.image.id = id;
            return this;
        }

        public Builder name(String name) {
            this.image.name = name;
            return this;
        }

        public Builder contentType(String contentType) {
            this.image.contentType = contentType;
            return this;
        }

        public Builder size(Long size) {
            this.image.size = size;
            return this;
        }

        public Builder format(String format) {
            this.image.format = format;
            return this;
        }

        public Builder data(byte[] data) {
            this.image.data = data;
            return this;
        }

        public Image build() {
            return image;
        }
    }
}
