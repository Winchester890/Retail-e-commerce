package retail.com.Services;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import retail.com.models.Order;
import retail.com.repositories.OrderRepository;

@Component
public class OrderMessageReceiver {
    private final OrderRepository orderRepository;

    public OrderMessageReceiver(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Mono<Order> saveOrder(final Order order) {
        return this.orderRepository.save(order)
                .doOnSuccess(result -> System.out.println("Order saved successfully: " + order.toString()))
                .doOnError(error -> System.out.println("Error saving the order: " + error.getMessage()));
    }

    @JmsListener(destination = "orders", containerFactory = "myFactory")
    public void receiveOrderToSave(final Order order) {
        saveOrder(order).subscribe();
    }

    @JmsListener(destination = "orders", containerFactory = "myFactory")
    public void receiveOrderToDelete(final String id) {
        this.orderRepository.deleteById(id)
                .doOnSuccess(result -> {
                    System.out.println("Order deleted successfully: " + id);
                })
                .doOnError(error -> {
                    System.out.println("Error deleting order with ID: " + id + " - " + error.getMessage());
                })
                .subscribe();
    }
}
