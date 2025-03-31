package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings("unused")
@Component
public final class Category {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long imageId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "Category{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", imageId=" + imageId +
               '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Category category;

        public Builder() {
            category = new Category();
        }

        public Builder id(Long id) {
            this.category.id = id;
            return this;
        }

        public Builder name(String name) {
            this.category.name = name;
            return this;
        }

        public Builder description(String description) {
            this.category.description = description;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.category.imageId = imageId;
            return this;
        }

        public Category build() {
            return category;
        }
    }
}
