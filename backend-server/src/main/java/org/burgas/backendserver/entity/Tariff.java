package org.burgas.backendserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuppressWarnings(value = "unused")
public final class Tariff {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String plan;
    private Long price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Tariff tariff;

        public Builder() {
            tariff = new Tariff();
        }

        public Builder id(Long id) {
            this.tariff.id = id;
            return this;
        }

        public Builder plan(String plan) {
            this.tariff.plan = plan;
            return this;
        }

        public Builder price(Long price) {
            this.tariff.price = price;
            return this;
        }

        public Tariff build() {
            return this.tariff;
        }
    }
}
