package com.szymongodzinski.shop.order;

import com.szymongodzinski.shop.order.OrderThings.OrderThings;

import javax.persistence.*;
import java.util.List;

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
}
