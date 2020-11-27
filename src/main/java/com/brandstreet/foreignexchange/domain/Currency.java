package com.brandstreet.foreignexchange.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Currency.
 */
@Entity
@Table(name = "currency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 3)
    @Column(name = "abbreviation", length = 3)
    private String abbreviation;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "fee")
    private Double fee;

    @Column(name = "discount")
    private Double discount;

    @OneToMany(mappedBy = "purchasedCurrency")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Order> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Currency abbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public Currency name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public Currency country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getFee() {
        return fee;
    }

    public Currency fee(Double fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getDiscount() {
        return discount;
    }

    public Currency discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Currency orders(Set<Order> orders) {
        this.orders = orders;
        return this;
    }

    public Currency addOrder(Order order) {
        this.orders.add(order);
        order.setPurchasedCurrency(this);
        return this;
    }

    public Currency removeOrder(Order order) {
        this.orders.remove(order);
        order.setPurchasedCurrency(null);
        return this;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currency)) {
            return false;
        }
        return id != null && id.equals(((Currency) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Currency{" +
            "id=" + getId() +
            ", abbreviation='" + getAbbreviation() + "'" +
            ", name='" + getName() + "'" +
            ", country='" + getCountry() + "'" +
            ", fee=" + getFee() +
            ", discount=" + getDiscount() +
            "}";
    }
}
