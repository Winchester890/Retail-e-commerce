package retail.com.controllers.dtos;

import retail.com.models.PaymentMethod;

import java.io.Serializable;

public class NewOrderDTO implements Serializable {
    private String productId;
    private Integer quantity;
    private String customerId;
    private PaymentMethod paymentMethod;
    private Double discount;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "NewOrderDTO{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", customerId='" + customerId + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", discount=" + discount +
                '}';
    }
}
