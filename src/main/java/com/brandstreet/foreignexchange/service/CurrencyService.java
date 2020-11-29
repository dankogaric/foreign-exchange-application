package com.brandstreet.foreignexchange.service;

import com.brandstreet.foreignexchange.domain.Currency;
import com.brandstreet.foreignexchange.repository.CurrencyRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public Currency save(Currency currency) {
    	return currencyRepository.save(currency);
    }
    
    public Page<Currency> findAll(Pageable pageable) {
    	return currencyRepository.findAll(pageable);
    }
    
    public Optional<Currency> findById(Long id) {
    	return currencyRepository.findById(id);
    }
    
    public Currency findByAbbreviation(String abbreviation) {
    	return currencyRepository.findByAbbreviation(abbreviation);
    }
    
    public void deleteById(Long id) {
    	currencyRepository.deleteById(id);
    }

}
