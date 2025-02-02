package retail.com.Services;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import retail.com.models.Customer;
import retail.com.repositories.CustomerRepository;

@Component
public class CustomerMessageReceiver {
    private final CustomerRepository customerRepository;

    public CustomerMessageReceiver(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @JmsListener(destination = "customers", containerFactory = "myFactory")
    public void receiveCustomerToSave(final Customer customer) {
        this.customerRepository.save(customer)
                .doOnSuccess(result -> {
                    System.out.println("Customer saved successfully: " + customer.toString());
                })
                .doOnError(error -> {
                    System.out.println("Error saving the customer: " + error.getMessage());
                })
                .subscribe();
    }

    @JmsListener(destination = "customers", containerFactory = "myFactory")
    public void receiveCustomerToDelete(final String id) {
        this.customerRepository.deleteById(id)
                .doOnSuccess(result -> {
                    System.out.println("Customer deleted successfully: " + id);
                })
                .doOnError(error -> {
                    System.out.println("Error deleting customer with ID: " + id + " - " + error.getMessage());
                })
                .subscribe();
    }
}
