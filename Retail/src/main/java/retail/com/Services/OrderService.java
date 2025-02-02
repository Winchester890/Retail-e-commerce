package retail.com.Services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import retail.com.controllers.dtos.NewOrderDTO;
import retail.com.controllers.dtos.UpdateOrderDTO;
import retail.com.models.Customer;
import retail.com.models.Order;
import retail.com.models.Product;
import retail.com.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMessageReceiver orderMessageReceiver;
    private final ProductService productService;
    private final CustomerService customerService;

    public OrderService(OrderRepository orderRepository, OrderMessageReceiver orderMessageReceiver, ProductService productService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.orderMessageReceiver = orderMessageReceiver;
        this.productService = productService;
        this.customerService = customerService;
    }

    public Flux<Order> findAll() {
        return this.orderRepository.findAll();
    }

    public Mono<Order> findById(final String id) {
        return this.orderRepository.findById(id);
    }

    public Flux<Order> findByProductId(final String productId) {
        return this.orderRepository.findByProductId(productId);
    }
    public void newOrder(final NewOrderDTO newOrderDTO) {
        productService.findById(newOrderDTO.getProductId())
                .flatMap(product -> {
                    if (!isDiscountValid(product, newOrderDTO.getDiscount())) {
                        System.out.println("The order's discount must not exceed product's max discount.");
                        return Mono.empty();
                    } else {
                        return customerService.findById(newOrderDTO.getCustomerId())
                                .map(customer -> mapToOrder(product, customer, newOrderDTO))
                                .flatMap(order -> {
                                    return Mono.just(order);
                                });
                    }
                })
                .subscribe(orderMessageReceiver::receiveOrderToSave);
    }

    public void updateOrder(final UpdateOrderDTO updateOrderDTO) {
        productService.findById(updateOrderDTO.getProductId())
                .flatMap(product -> {
                    if (!isDiscountValid(product, updateOrderDTO.getDiscount())) {
                        System.out.println("The order's discount must not exceed product's max discount.");
                        return Mono.empty();
                    } else {
                        return customerService.findById(updateOrderDTO.getCustomerId())
                                .map(customer -> mapToUpdateOrder(product, customer, updateOrderDTO))
                                .flatMap(order -> {
                                    return Mono.just(order);
                                });
                    }
                })
                .subscribe(orderMessageReceiver::receiveOrderToSave);
    }

    public void deleteOrder(final String id) {
        orderMessageReceiver.receiveOrderToDelete(id);
    }

    public Order mapToOrder(Product product, Customer customer, NewOrderDTO newOrderDTO) {
        Order order = new Order();
        order.setProduct(product);
        order.setCustomer(customer);
        order.setQuantity(newOrderDTO.getQuantity());
        order.setPaymentMethod(newOrderDTO.getPaymentMethod());
        order.setDiscount(newOrderDTO.getDiscount());
        return order;
    }

    public Order mapToUpdateOrder(Product product, Customer customer, UpdateOrderDTO updateOrderDTO) {
        Order order = new Order();
        order.setId(updateOrderDTO.getId());
        order.setProduct(product);
        order.setCustomer(customer);
        order.setQuantity(updateOrderDTO.getQuantity());
        order.setPaymentMethod(updateOrderDTO.getPaymentMethod());
        order.setDiscount(updateOrderDTO.getDiscount());
        return order;
    }

    public Boolean isDiscountValid(Product product, Double discount) {

        if (discount < 0 || product.getMaxDiscount() < 0) {
            throw new IllegalArgumentException("Discount values must not be negative!");
        }

        return discount <= product.getMaxDiscount();
    }
}
