package com.brandstreet.foreignexchange.service;

import com.brandstreet.foreignexchange.domain.Order;
import com.brandstreet.foreignexchange.repository.OrderRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {
	
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private MailService mailService;
    
    // @Value("${jhipster.mail.to}") - This should be specified mail to send notification on.
    // Because of specific jhipster tools, it can not be added new property without 
    // including the blueprint the usual way. But it is a usual way to (@Value) to put the
    // configurable property.
    // So that is why a put it in variable.
    private final String mailTo = "garic.danko@gmail.com";
    
    public final String SUBJECT = "New exchange transaction executed.";
    
    public final String ABB_FOR_MAIL_SENDING = "GBP";
    
    public final boolean IS_MULTIPART= false;
    
    public final boolean IS_HTML = false;

    public Order save(Order order) {
    	Order savedOrder = orderRepository.save(order);
    	if (savedOrder.getPurchasedCurrency().getAbbreviation().equalsIgnoreCase(ABB_FOR_MAIL_SENDING)) {
    		mailService.sendEmail(mailTo, SUBJECT, order.toString(), IS_MULTIPART, IS_HTML);
    	}
    	return savedOrder;
    }
    
    public Page<Order> findAll(Pageable pageable) {
    	return orderRepository.findAll(pageable);
    }
    
    public Optional<Order> findById(Long id) {
    	return orderRepository.findById(id);
    }
    
    public void deleteById(Long id) {
    	orderRepository.deleteById(id);
    }
}
