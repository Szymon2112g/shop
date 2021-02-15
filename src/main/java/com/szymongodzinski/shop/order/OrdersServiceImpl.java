package com.szymongodzinski.shop.order;

import com.szymongodzinski.shop.order.OrderThings.OrderThings;
import com.szymongodzinski.shop.order.OrderThings.OrderThingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersServiceImpl implements OrdersService {

    private OrdersRepository ordersRepository;
    private OrderThingsRepository orderThingsRepository;

    @Autowired
    public OrdersServiceImpl(OrdersRepository ordersRepository, OrderThingsRepository orderThingsRepository) {
        this.ordersRepository = ordersRepository;
        this.orderThingsRepository = orderThingsRepository;
    }

    @Override
    public List<Orders> findAll() {
        return ordersRepository.findAll();
    }

    @Override
    public Orders findById(Long orderId) {

        if (orderId == null || orderId <= 0) {
            return null;
        }

        Optional<Orders> ordersOptional = ordersRepository.findById(orderId);
        if (ordersOptional.isEmpty()) {
            return null;
        }

        return ordersOptional.get();
    }

    @Override
    public boolean addOrder(String customerName, List<OrderThings> orderThings) {

        if (customerName == null || customerName.equals("")) {
            return false;
        }

        if (orderThings == null || orderThings.isEmpty()) {
            return false;
        }

        List<OrderThings> orderThingsFromDB = new ArrayList<>();

        orderThings.forEach(orderThings1 -> {
            orderThingsFromDB.add(orderThingsRepository.save(orderThings1));
        });

        if (orderThingsFromDB == null || orderThingsFromDB.isEmpty()) {
            return false;
        }

        Orders orders = new Orders(0L, customerName, orderThingsFromDB);
        ordersRepository.save(orders);

        return true;
    }

    @Override
    public boolean deleteOrder(Long orderId) {

        if (orderId == null || orderId <= 0) {
            return false;
        }

        Optional<Orders> ordersOptional = ordersRepository.findById(orderId);

        if (ordersOptional.isEmpty()) {
            return false;
        }

        Orders orders = ordersOptional.get();
        ordersRepository.delete(orders);

        return true;
    }
}
