package com.brandstreet.foreignexchange.web.rest;

import com.brandstreet.foreignexchange.ForeignExchangeApp;
import com.brandstreet.foreignexchange.domain.CurrencyPair;
import com.brandstreet.foreignexchange.repository.CurrencyPairRepository;
import com.brandstreet.foreignexchange.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.brandstreet.foreignexchange.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CurrencyPairResource} REST controller.
 */
@SpringBootTest(classes = ForeignExchangeApp.class)
public class CurrencyPairResourceIT {

    private static final String DEFAULT_NAME = "AAAAAA";
    private static final String UPDATED_NAME = "BBBBBB";

    private static final Double DEFAULT_EXCHANGE_RATE = 1D;
    private static final Double UPDATED_EXCHANGE_RATE = 2D;

    @Autowired
    private CurrencyPairRepository currencyPairRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCurrencyPairMockMvc;

    private CurrencyPair currencyPair;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CurrencyPairResource currencyPairResource = new CurrencyPairResource(currencyPairRepository);
        this.restCurrencyPairMockMvc = MockMvcBuilders.standaloneSetup(currencyPairResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyPair createEntity(EntityManager em) {
        CurrencyPair currencyPair = new CurrencyPair()
            .name(DEFAULT_NAME)
            .exchangeRate(DEFAULT_EXCHANGE_RATE);
        return currencyPair;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyPair createUpdatedEntity(EntityManager em) {
        CurrencyPair currencyPair = new CurrencyPair()
            .name(UPDATED_NAME)
            .exchangeRate(UPDATED_EXCHANGE_RATE);
        return currencyPair;
    }

    @BeforeEach
    public void initTest() {
        currencyPair = createEntity(em);
    }

    @Test
    @Transactional
    public void createCurrencyPair() throws Exception {
        int databaseSizeBeforeCreate = currencyPairRepository.findAll().size();

        // Create the CurrencyPair
        restCurrencyPairMockMvc.perform(post("/api/currency-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyPair)))
            .andExpect(status().isCreated());

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyPair testCurrencyPair = currencyPairList.get(currencyPairList.size() - 1);
        assertThat(testCurrencyPair.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCurrencyPair.getExchangeRate()).isEqualTo(DEFAULT_EXCHANGE_RATE);
    }

    @Test
    @Transactional
    public void createCurrencyPairWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = currencyPairRepository.findAll().size();

        // Create the CurrencyPair with an existing ID
        currencyPair.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyPairMockMvc.perform(post("/api/currency-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyPair)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCurrencyPairs() throws Exception {
        // Initialize the database
        currencyPairRepository.saveAndFlush(currencyPair);

        // Get all the currencyPairList
        restCurrencyPairMockMvc.perform(get("/api/currency-pairs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyPair.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].exchangeRate").value(hasItem(DEFAULT_EXCHANGE_RATE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCurrencyPair() throws Exception {
        // Initialize the database
        currencyPairRepository.saveAndFlush(currencyPair);

        // Get the currencyPair
        restCurrencyPairMockMvc.perform(get("/api/currency-pairs/{id}", currencyPair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(currencyPair.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.exchangeRate").value(DEFAULT_EXCHANGE_RATE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCurrencyPair() throws Exception {
        // Get the currencyPair
        restCurrencyPairMockMvc.perform(get("/api/currency-pairs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrencyPair() throws Exception {
        // Initialize the database
        currencyPairRepository.saveAndFlush(currencyPair);

        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().size();

        // Update the currencyPair
        CurrencyPair updatedCurrencyPair = currencyPairRepository.findById(currencyPair.getId()).get();
        // Disconnect from session so that the updates on updatedCurrencyPair are not directly saved in db
        em.detach(updatedCurrencyPair);
        updatedCurrencyPair
            .name(UPDATED_NAME)
            .exchangeRate(UPDATED_EXCHANGE_RATE);

        restCurrencyPairMockMvc.perform(put("/api/currency-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCurrencyPair)))
            .andExpect(status().isOk());

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
        CurrencyPair testCurrencyPair = currencyPairList.get(currencyPairList.size() - 1);
        assertThat(testCurrencyPair.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrencyPair.getExchangeRate()).isEqualTo(UPDATED_EXCHANGE_RATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCurrencyPair() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().size();

        // Create the CurrencyPair

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyPairMockMvc.perform(put("/api/currency-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyPair)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCurrencyPair() throws Exception {
        // Initialize the database
        currencyPairRepository.saveAndFlush(currencyPair);

        int databaseSizeBeforeDelete = currencyPairRepository.findAll().size();

        // Delete the currencyPair
        restCurrencyPairMockMvc.perform(delete("/api/currency-pairs/{id}", currencyPair.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
