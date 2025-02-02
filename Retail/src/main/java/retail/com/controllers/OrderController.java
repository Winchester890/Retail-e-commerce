package retail.com.controllers;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import retail.com.Services.OrderService;
import retail.com.controllers.dtos.NewOrderDTO;
import retail.com.controllers.dtos.UpdateOrderDTO;
import retail.com.models.Order;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final JmsTemplate jmsTemplate;

    public OrderController(OrderService orderService, JmsTemplate jmsTemplate) {
        this.orderService = orderService;
        this.jmsTemplate = jmsTemplate;
    }

    @RolesAllowed("adm")
    @GetMapping
    public ResponseEntity<Flux<Order>> findAll() {
        Flux<Order> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    @RolesAllowed("customer")
    @GetMapping("/{id}")
    public ResponseEntity<Mono<Order>> findById(@PathVariable final String id) {
        Mono<Order> order = orderService.findById(id);
        return order != null ?
                ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @RolesAllowed("adm")
    @GetMapping("/byProduct/{productId}")
    public ResponseEntity<Flux<Order>> findByProductId(@PathVariable final String productId) {
        Flux<Order> orders = orderService.findByProductId(productId);
        return ResponseEntity.ok(orders);
    }

    @RolesAllowed({"customer", "adm"})
    @PostMapping
    public ResponseEntity saveOrder(@RequestBody final NewOrderDTO newOrderDTO) {
        this.orderService.newOrder(newOrderDTO);
        jmsTemplate.convertAndSend("orders", "Saved order: " + newOrderDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RolesAllowed({"customer", "adm"})
    @PutMapping
    public ResponseEntity updateOrder(@RequestBody final UpdateOrderDTO updateOrderDTO) {
        this.orderService.updateOrder(updateOrderDTO);
        jmsTemplate.convertAndSend("orders", "Saved order: " + updateOrderDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RolesAllowed("adm")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable final String id) {
        this.orderService.deleteOrder(id);
        jmsTemplate.convertAndSend("orders", "Deleted order ID: " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
