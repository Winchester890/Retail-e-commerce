package retail.com.controllers;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import retail.com.Services.ProductService;
import retail.com.models.Product;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final JmsTemplate jmsTemplate;

    public ProductController(ProductService productService, JmsTemplate jmsTemplate) {
        this.productService = productService;
        this.jmsTemplate = jmsTemplate;
    }

    @RolesAllowed({"customer", "adm"})
    @GetMapping
    public ResponseEntity<Flux<Product>> findAll() {
        Flux<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @RolesAllowed({"customer", "adm"})
    @GetMapping("/{id}")
    public ResponseEntity<Mono<Product>> findById(@PathVariable final String id) {
        Mono<Product> product = productService.findById(id);
        return product != null ?
                ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @RolesAllowed({"customer", "adm"})
    @GetMapping("/byDescription/{description}")
    public ResponseEntity<Mono<Product>> findByDescription(@PathVariable final String description) {
        Mono<Product> product = productService.findByDescription(description);
        return product != null ?
                ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @RolesAllowed("adm")
    @PostMapping
    public ResponseEntity saveProduct(@RequestBody final Product product) {
        this.productService.save(product);
        jmsTemplate.convertAndSend("products", "Saved product: " + product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RolesAllowed("adm")
    @PutMapping
    public ResponseEntity updateProduct(@RequestBody final Product product) {
        this.productService.save(product);
        jmsTemplate.convertAndSend("products", "Saved product: " + product);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RolesAllowed("adm")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable final String id) {
        this.productService.delete(id);
        jmsTemplate.convertAndSend("products", "Deleted product ID: " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
