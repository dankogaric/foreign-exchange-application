package com.brandstreet.foreignexchange.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "surcharge_amount")
    private Double surchargeAmount;

    @Column(name = "purchased_amount")
    private Double purchasedAmount;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "discount_percentage")
    private Double discountPercentage;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private Currency purchasedCurrency;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSurchargeAmount() {
        return surchargeAmount;
    }

    public Order surchargeAmount(Double surchargeAmount) {
        this.surchargeAmount = surchargeAmount;
        return this;
    }

    public void setSurchargeAmount(Double surchargeAmount) {
        this.surchargeAmount = surchargeAmount;
    }

    public Double getPurchasedAmount() {
        return purchasedAmount;
    }

    public Order purchasedAmount(Double purchasedAmount) {
        this.purchasedAmount = purchasedAmount;
        return this;
    }

    public void setPurchasedAmount(Double purchasedAmount) {
        this.purchasedAmount = purchasedAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public Order paidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
        return this;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public Order discountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
        return this;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public Order discountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Order creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Currency getPurchasedCurrency() {
        return purchasedCurrency;
    }

    public Order purchasedCurrency(Currency currency) {
        this.purchasedCurrency = currency;
        return this;
    }

    public void setPurchasedCurrency(Currency currency) {
        this.purchasedCurrency = currency;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", surchargeAmount=" + getSurchargeAmount() +
            ", purchasedAmount=" + getPurchasedAmount() +
            ", paidAmount=" + getPaidAmount() +
            ", discountPercentage=" + getDiscountPercentage() +
            ", discountAmount=" + getDiscountAmount() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
