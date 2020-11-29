package com.brandstreet.foreignexchange.web.rest;

import com.brandstreet.foreignexchange.ForeignExchangeApp;
import com.brandstreet.foreignexchange.domain.Order;
import com.brandstreet.foreignexchange.repository.OrderRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.brandstreet.foreignexchange.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OrderController} REST controller.
 */
@SpringBootTest(classes = ForeignExchangeApp.class)
public class OrderResourceIT {

    private static final Double DEFAULT_SURCHARGE_AMOUNT = 1D;
    private static final Double UPDATED_SURCHARGE_AMOUNT = 2D;

    private static final Double DEFAULT_PURCHASED_AMOUNT = 1D;
    private static final Double UPDATED_PURCHASED_AMOUNT = 2D;

    private static final Double DEFAULT_PAID_AMOUNT = 1D;
    private static final Double UPDATED_PAID_AMOUNT = 2D;

    private static final Double DEFAULT_DISCOUNT_PERCENTAGE = 1D;
    private static final Double UPDATED_DISCOUNT_PERCENTAGE = 2D;

    private static final Double DEFAULT_DISCOUNT_AMOUNT = 1D;
    private static final Double UPDATED_DISCOUNT_AMOUNT = 2D;

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private OrderRepository orderRepository;

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

    private MockMvc restOrderMockMvc;

    private Order order;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderController orderResource = new OrderController();
        this.restOrderMockMvc = MockMvcBuilders.standaloneSetup(orderResource)
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
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .surchargeAmount(DEFAULT_SURCHARGE_AMOUNT)
            .purchasedAmount(DEFAULT_PURCHASED_AMOUNT)
            .paidAmount(DEFAULT_PAID_AMOUNT)
            .discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
            .creationDate(DEFAULT_CREATION_DATE);
        return order;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .surchargeAmount(UPDATED_SURCHARGE_AMOUNT)
            .purchasedAmount(UPDATED_PURCHASED_AMOUNT)
            .paidAmount(UPDATED_PAID_AMOUNT)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .creationDate(UPDATED_CREATION_DATE);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getSurchargeAmount()).isEqualTo(DEFAULT_SURCHARGE_AMOUNT);
        assertThat(testOrder.getPurchasedAmount()).isEqualTo(DEFAULT_PURCHASED_AMOUNT);
        assertThat(testOrder.getPaidAmount()).isEqualTo(DEFAULT_PAID_AMOUNT);
        assertThat(testOrder.getDiscountPercentage()).isEqualTo(DEFAULT_DISCOUNT_PERCENTAGE);
        assertThat(testOrder.getDiscountAmount()).isEqualTo(DEFAULT_DISCOUNT_AMOUNT);
        assertThat(testOrder.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order with an existing ID
        order.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].surchargeAmount").value(hasItem(DEFAULT_SURCHARGE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].purchasedAmount").value(hasItem(DEFAULT_PURCHASED_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].paidAmount").value(hasItem(DEFAULT_PAID_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(DEFAULT_DISCOUNT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.surchargeAmount").value(DEFAULT_SURCHARGE_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.purchasedAmount").value(DEFAULT_PURCHASED_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.paidAmount").value(DEFAULT_PAID_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.discountPercentage").value(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.discountAmount").value(DEFAULT_DISCOUNT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .surchargeAmount(UPDATED_SURCHARGE_AMOUNT)
            .purchasedAmount(UPDATED_PURCHASED_AMOUNT)
            .paidAmount(UPDATED_PAID_AMOUNT)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .creationDate(UPDATED_CREATION_DATE);

        restOrderMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrder)))
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getSurchargeAmount()).isEqualTo(UPDATED_SURCHARGE_AMOUNT);
        assertThat(testOrder.getPurchasedAmount()).isEqualTo(UPDATED_PURCHASED_AMOUNT);
        assertThat(testOrder.getPaidAmount()).isEqualTo(UPDATED_PAID_AMOUNT);
        assertThat(testOrder.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
        assertThat(testOrder.getDiscountAmount()).isEqualTo(UPDATED_DISCOUNT_AMOUNT);
        assertThat(testOrder.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Create the Order

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc.perform(delete("/api/orders/{id}", order.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
