package com.brandstreet.foreignexchange.repository;

import com.brandstreet.foreignexchange.domain.Currency;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Currency entity.
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
	Currency findByAbbreviation(String abbreviation);
}
