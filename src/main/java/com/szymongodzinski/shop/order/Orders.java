package com.szymongodzinski.shop.order;

import com.szymongodzinski.shop.order.OrderThings.OrderThings;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer")
    private String customerName;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<OrderThings> orderThings;

    public Orders() {
    }

    public Orders(Long id, String customerName, List<OrderThings> orderThings) {
        this.id = id;
        this.customerName = customerName;
        this.orderThings = orderThings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<OrderThings> getOrdersThings() {
        return orderThings;
    }

    public void setOrdersThings(List<OrderThings> orderThings) {
        this.orderThings = orderThings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id) &&
                Objects.equals(customerName, orders.customerName) &&
                Objects.equals(orderThings, orders.orderThings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, orderThings);
    }
}
