package com.szymongodzinski.shop.controller;

import com.szymongodzinski.shop.order.Orders;
import com.szymongodzinski.shop.order.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrdersController {

    private OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping(path = "/orders/all")
    public ResponseEntity<?> findAllOrders() {
        return ResponseEntity.ok(ordersService.findAll());
    }
}
