package com.szymongodzinski.shop.order.OrderThings;

import javax.persistence.*;

@Entity
public class OrderThings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "thing_id")
    private Long thingId;

    @Column(name = "quantity")
    private int quantity;

    public OrderThings() {
    }

    public OrderThings(Long id, Long thingId, int quantity) {
        this.id = id;
        this.thingId = thingId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThingId() {
        return thingId;
    }

    public void setThingId(Long thingId) {
        this.thingId = thingId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
