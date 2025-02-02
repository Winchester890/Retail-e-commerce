package retail.com.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import retail.com.models.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
    Mono<Product> findByDescription(final String description);
}
