package org.burgas.identityserver.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public final class Authority implements Persistable<Long>{

    @Id
    private Long id;
    private String name;

    @Transient
    private Boolean isNew;

    @Override
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    @SuppressWarnings("unused")
    public Boolean getNew() {
        return isNew || id == null;
    }

    @SuppressWarnings("unused")
    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Authority authority;

        public Builder() {
            authority = new Authority();
        }

        public Builder id(Long id) {
            this.authority.id = id;
            return this;
        }

        public Builder name(String name) {
            this.authority.name = name;
            return this;
        }

        public Builder isNew(Boolean isNew) {
            this.authority.isNew = isNew;
            return this;
        }

        public Authority build() {
            return authority;
        }
    }
}
