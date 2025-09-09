package com.rafi.order_service.vo;

import com.rafi.order_service.model.Order;

public class ResponseTemplate {
    Order order;
    Product product;
    Customer customer;

    public ResponseTemplate() {}

    public ResponseTemplate(Order order, Product product, Customer customer) {
        this.order = order;
        this.product = product;
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    
}
