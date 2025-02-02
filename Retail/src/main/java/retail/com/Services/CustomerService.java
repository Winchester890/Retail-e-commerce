package retail.com.Services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import retail.com.models.Customer;
import retail.com.repositories.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMessageReceiver customerMessageReceiver;

    public CustomerService(CustomerRepository customerRepository, CustomerMessageReceiver customerMessageReceiver) {
        this.customerRepository = customerRepository;
        this.customerMessageReceiver = customerMessageReceiver;
    }

    public Flux<Customer> findAll() {
       return this.customerRepository.findAll();
    }

    public Mono<Customer> findById(final String id) {
        return this.customerRepository.findById(id);
    }

    public void save(final Customer customer) {
        customerMessageReceiver.receiveCustomerToSave(customer);
    }

    public void delete(final String id) {
        customerMessageReceiver.receiveCustomerToDelete(id);
    }
}
