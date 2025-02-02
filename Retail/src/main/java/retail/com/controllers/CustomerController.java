package retail.com.controllers;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import retail.com.Services.CustomerService;
import retail.com.models.Customer;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final JmsTemplate jmsTemplate;

    public CustomerController(CustomerService customerService, JmsTemplate jmsTemplate) {
        this.customerService = customerService;
        this.jmsTemplate = jmsTemplate;
    }

    @RolesAllowed("adm")
    @GetMapping
    public ResponseEntity<Flux<Customer>> findAll() {
        Flux<Customer> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @RolesAllowed("adm")
    @GetMapping("/{id}")
    public ResponseEntity<Mono<Customer>> findById(@PathVariable final String id) {
        Mono<Customer> customer = customerService.findById(id);
        return customer != null ?
                ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @RolesAllowed({"adm", "customer"})
    @PostMapping
    public ResponseEntity saveCustomer(@RequestBody final Customer customer) {
        customerService.save(customer);
        jmsTemplate.convertAndSend("customers", "Saved customer: " + customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RolesAllowed({"adm", "customer"})
    @PutMapping
    public ResponseEntity updateCustomer(@RequestBody final Customer customer) {
        customerService.save(customer);
        jmsTemplate.convertAndSend("customers", "Saved customer: " + customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RolesAllowed("adm")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@PathVariable final String id) {
        customerService.delete(id);
        jmsTemplate.convertAndSend("customers", "Deleted customer ID: " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
