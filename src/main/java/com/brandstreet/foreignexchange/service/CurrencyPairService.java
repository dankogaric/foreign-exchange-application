package com.brandstreet.foreignexchange.service;

import com.brandstreet.foreignexchange.domain.Currency;
import com.brandstreet.foreignexchange.domain.CurrencyPair;
import com.brandstreet.foreignexchange.repository.CurrencyPairRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CurrencyPairService {

    @Autowired
    private CurrencyPairRepository currencyPairRepository;

    public CurrencyPair save(CurrencyPair currencyPair) {
    	return currencyPairRepository.save(currencyPair);
    }
    
    public Page<CurrencyPair> findAll(Pageable pageable) {
    	return currencyPairRepository.findAll(pageable);
    }
    
    public Optional<CurrencyPair> findById(Long id) {
    	return currencyPairRepository.findById(id);
    }
    
    public List<CurrencyPair> findByCurrencyToSell(Currency currencyToSell) {
    	return currencyPairRepository.findByCurrencyToSell(currencyToSell);
    }
    
    public void deleteById(Long id) {
    	currencyPairRepository.deleteById(id);
    }

}
