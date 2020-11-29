package com.brandstreet.foreignexchange.web.rest;

import com.brandstreet.foreignexchange.domain.Currency;
import com.brandstreet.foreignexchange.domain.CurrencyPair;
import com.brandstreet.foreignexchange.domain.Order;
import com.brandstreet.foreignexchange.service.CurrencyPairService;
import com.brandstreet.foreignexchange.service.CurrencyService;
import com.brandstreet.foreignexchange.service.OrderService;
import com.brandstreet.foreignexchange.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

/**
 * REST controller for managing {@link com.brandstreet.foreignexchange.domain.Order}.
 */
@RestController
@RequestMapping("/api")
public class ExchangeController {

    private final Logger log = LoggerFactory.getLogger(ExchangeController.class);
    
    private final String USD = "USD";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CurrencyService currencyService;
    
    @Autowired
    private CurrencyPairService currencyPairService;
    
    @Autowired
    private OrderService orderService;

    /**
     * {@code GET  /exchange/from-usdollars} : get a List of currency pairs when selling the US dollar.
     *exchange-from-usdollars

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
     */
    @GetMapping("/exchange/from-usdollars")
    public ResponseEntity<List<CurrencyPair>> getCurrencyPairsFromDollar() {
        log.debug("REST request to get a List of currency pairs when us dollar selling");
        Currency usd = currencyService.findByAbbreviation(USD);
        List<CurrencyPair> currencyPairs = currencyPairService.findByCurrencyToSell(usd);
        return ResponseEntity.ok().body(currencyPairs);
    }
    
    /**
     * {@code POST  /exchange/save-order} : Create a new order.
     *
     * @param order the order to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new order, or with status {@code 400 (Bad Request)} if the order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exchange/save-order")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            throw new BadRequestAlertException("A new order cannot already have an ID", "order", "idexists");
        }
        Order result = orderService.save(order);
        return ResponseEntity.created(new URI("/api/orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "order", result.getId().toString()))
            .body(result);
    }
}
