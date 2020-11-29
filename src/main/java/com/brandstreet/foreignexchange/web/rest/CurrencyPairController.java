package com.brandstreet.foreignexchange.web.rest;

import com.brandstreet.foreignexchange.domain.CurrencyPair;
import com.brandstreet.foreignexchange.service.CurrencyPairService;
import com.brandstreet.foreignexchange.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.brandstreet.foreignexchange.domain.CurrencyPair}.
 */
@RestController
@RequestMapping("/api")
public class CurrencyPairController {

    private final Logger log = LoggerFactory.getLogger(CurrencyPairController.class);

    private static final String ENTITY_NAME = "currencyPair";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CurrencyPairService currencyPairService;

    /**
     * {@code POST  /currency-pairs} : Create a new currencyPair.
     *
     * @param currencyPair the currencyPair to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyPair, or with status {@code 400 (Bad Request)} if the currencyPair has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currency-pairs")
    public ResponseEntity<CurrencyPair> createCurrencyPair(@Valid @RequestBody CurrencyPair currencyPair) throws URISyntaxException {
        log.debug("REST request to save CurrencyPair : {}", currencyPair);
        if (currencyPair.getId() != null) {
            throw new BadRequestAlertException("A new currencyPair cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyPair result = currencyPairService.save(currencyPair);
        return ResponseEntity.created(new URI("/api/currency-pairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /currency-pairs} : Updates an existing currencyPair.
     *
     * @param currencyPair the currencyPair to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyPair,
     * or with status {@code 400 (Bad Request)} if the currencyPair is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyPair couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currency-pairs")
    public ResponseEntity<CurrencyPair> updateCurrencyPair(@Valid @RequestBody CurrencyPair currencyPair) throws URISyntaxException {
        log.debug("REST request to update CurrencyPair : {}", currencyPair);
        if (currencyPair.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CurrencyPair result = currencyPairService.save(currencyPair);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyPair.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /currency-pairs} : get all the currencyPairs.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencyPairs in body.
     */
    @GetMapping("/currency-pairs")
    public ResponseEntity<List<CurrencyPair>> getAllCurrencyPairs(Pageable pageable) {
        log.debug("REST request to get a page of CurrencyPairs");
        Page<CurrencyPair> page = currencyPairService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /currency-pairs/:id} : get the "id" currencyPair.
     *
     * @param id the id of the currencyPair to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyPair, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currency-pairs/{id}")
    public ResponseEntity<CurrencyPair> getCurrencyPair(@PathVariable Long id) {
        log.debug("REST request to get CurrencyPair : {}", id);
        Optional<CurrencyPair> currencyPair = currencyPairService.findById(id);
        return ResponseUtil.wrapOrNotFound(currencyPair);
    }

    /**
     * {@code DELETE  /currency-pairs/:id} : delete the "id" currencyPair.
     *
     * @param id the id of the currencyPair to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currency-pairs/{id}")
    public ResponseEntity<Void> deleteCurrencyPair(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyPair : {}", id);
        currencyPairService.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
