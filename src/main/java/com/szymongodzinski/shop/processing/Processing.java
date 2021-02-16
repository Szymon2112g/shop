package com.szymongodzinski.shop.processing;

import com.szymongodzinski.shop.order.Orders;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Processing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state")
    private State state;

    @OneToOne
    private Orders orders;

    public Processing() {
    }

    public Processing(Long id, State state, Orders orders) {
        this.id = id;
        this.state = state;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Processing that = (Processing) o;
        return id.equals(that.id) &&
                state.equals(that.state) &&
                orders.equals(that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, orders);
    }
}
