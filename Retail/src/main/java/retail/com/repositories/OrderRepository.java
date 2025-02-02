package retail.com.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import retail.com.models.Order;

public interface OrderRepository extends ReactiveCrudRepository<Order, String> {
    Flux<Order> findByProductId(final String productId);
}
