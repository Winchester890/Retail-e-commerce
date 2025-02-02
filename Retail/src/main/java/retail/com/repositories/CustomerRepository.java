package retail.com.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import retail.com.models.Customer;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {

}
