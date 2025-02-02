package retail.com;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import retail.com.Services.OrderService;
import retail.com.models.Product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RetailApplicationTests {

	private final OrderService orderService = new OrderService(null, null, null, null);

	@Test
	void shouldReturnTrueWhenDiscountIsValid() {
		Product product = new Product();
		product.setMaxDiscount(20.0);

		assertTrue(orderService.isDiscountValid(product, 10.0));
	}

	@Test
	void shouldReturnFalseWhenDiscountExceedsMax() {
		Product product = new Product();
		product.setMaxDiscount(20.0);

		assertFalse(orderService.isDiscountValid(product, 25.0));
	}

}
