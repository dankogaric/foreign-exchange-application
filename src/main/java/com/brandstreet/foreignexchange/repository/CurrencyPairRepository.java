package com.brandstreet.foreignexchange.repository;

import com.brandstreet.foreignexchange.domain.Currency;
import com.brandstreet.foreignexchange.domain.CurrencyPair;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CurrencyPair entity.
 */
@Repository
public interface CurrencyPairRepository extends JpaRepository<CurrencyPair, Long> {
	List<CurrencyPair> findByCurrencyToSell(Currency currencyToSell);
}
