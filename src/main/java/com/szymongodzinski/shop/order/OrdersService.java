package com.szymongodzinski.shop.order;

import com.szymongodzinski.shop.order.OrderThings.OrderThings;

import java.util.List;

public interface OrdersService {

    public List<Orders> findAll();
    public Orders findById(Long orderId);
    public boolean addOrder(String customerName, List<OrderThings> orderThings);
    public boolean deleteOrder(Long orderId);

}
