package com.szymongodzinski.shop.order;

import com.szymongodzinski.shop.thing.Thing;

import javax.persistence.*;
import java.util.List;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer")
    private String customerName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "thing_orders",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "thing_id"))
    private List<Thing> thingList;

    public Orders() {
    }

    public Orders(Long id, String customerName, List<Thing> thingList) {
        this.id = id;
        this.customerName = customerName;
        this.thingList = thingList;
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

    public List<Thing> getThingList() {
        return thingList;
    }

    public void setThingList(List<Thing> thingList) {
        this.thingList = thingList;
    }


}
