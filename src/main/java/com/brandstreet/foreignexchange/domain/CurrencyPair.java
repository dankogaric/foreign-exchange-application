package com.brandstreet.foreignexchange.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A CurrencyPair.
 */
@Entity
@Table(name = "currency_pair")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CurrencyPair implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 6)
    @Column(name = "name", length = 6)
    private String name;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @ManyToOne
    @JsonIgnoreProperties("currencyPairs")
    private Currency currencyToSell;

    @ManyToOne
    @JsonIgnoreProperties("currencyPairs")
    private Currency currencyToBuy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CurrencyPair name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public CurrencyPair exchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Currency getCurrencyToSell() {
        return currencyToSell;
    }

    public CurrencyPair currencyToSell(Currency currency) {
        this.currencyToSell = currency;
        return this;
    }

    public void setCurrencyToSell(Currency currency) {
        this.currencyToSell = currency;
    }

    public Currency getCurrencyToBuy() {
        return currencyToBuy;
    }

    public CurrencyPair currencyToBuy(Currency currency) {
        this.currencyToBuy = currency;
        return this;
    }

    public void setCurrencyToBuy(Currency currency) {
        this.currencyToBuy = currency;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyPair)) {
            return false;
        }
        return id != null && id.equals(((CurrencyPair) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CurrencyPair{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            "}";
    }
}
