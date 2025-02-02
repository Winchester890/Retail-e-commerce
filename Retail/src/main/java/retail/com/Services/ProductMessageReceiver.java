package retail.com.Services;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import retail.com.models.Product;
import retail.com.repositories.ProductRepository;

@Component
public class ProductMessageReceiver {

    private final ProductRepository productRepository;

    public ProductMessageReceiver(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @JmsListener(destination = "products", containerFactory = "myFactory")
    public void receiveProductToSave(final Product product) {
        this.productRepository.save(product)
                .doOnSuccess(result -> {
                    System.out.println("Product saved successfully: " + product.toString());
                })
                .doOnError(error -> {
                    System.out.println("Error saving the product: " + error.getMessage());
                })
                .subscribe();
    }

    @JmsListener(destination = "products", containerFactory = "myFactory")
    public void receiveProductToDelete(final String id) {
        this.productRepository.deleteById(id)
                .doOnSuccess(result -> {
                    System.out.println("Product deleted successfully: " + id);
                })
                .doOnError(error -> {
                    System.out.println("Error deleting product with ID: " + id + " - " + error.getMessage());
                })
                .subscribe();
    }
}
