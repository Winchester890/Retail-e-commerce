package retail.com.Services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import retail.com.models.Product;
import retail.com.repositories.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMessageReceiver productMessageReceiver;

    public ProductService(ProductRepository productRepository, ProductMessageReceiver productMessageReceiver) {
        this.productRepository = productRepository;
        this.productMessageReceiver = productMessageReceiver;
    }

    public Flux<Product> findAll() {
        return this.productRepository.findAll();
    }

    public Mono<Product> findById(final String id) {
        return this.productRepository.findById(id);
    }

    public Mono<Product> findByDescription(final String description) {
        return this.productRepository.findByDescription(description);
    }

    public void save(final Product product) {
        productMessageReceiver.receiveProductToSave(product);
    }

    public void delete(final String id) {
        productMessageReceiver.receiveProductToDelete(id);
    }

}
