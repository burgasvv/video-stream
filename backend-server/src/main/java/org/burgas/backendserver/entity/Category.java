package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public final class Category {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public String getDescription() {
        return description;
    }

    @SuppressWarnings("unused")
    public void setDescription(String description) {
        this.description = description;
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

        @SuppressWarnings("unused")
        public Builder description(String description) {
            this.category.description = description;
            return this;
        }

        public Category build() {
            return category;
        }
    }
}
