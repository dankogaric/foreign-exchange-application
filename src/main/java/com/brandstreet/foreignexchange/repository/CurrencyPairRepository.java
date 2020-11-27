package com.brandstreet.foreignexchange.repository;

import com.brandstreet.foreignexchange.domain.CurrencyPair;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CurrencyPair entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyPairRepository extends JpaRepository<CurrencyPair, Long> {

}
