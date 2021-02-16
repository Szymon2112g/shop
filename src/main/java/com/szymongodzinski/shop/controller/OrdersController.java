package com.szymongodzinski.shop.controller;

import com.szymongodzinski.shop.order.OrderThings.OrderThings;
import com.szymongodzinski.shop.order.Orders;
import com.szymongodzinski.shop.order.OrdersService;
import com.szymongodzinski.shop.thing.Thing;
import com.szymongodzinski.shop.thing.ThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class OrdersController {

    private OrdersService ordersService;
    private ThingService thingService;

    @Autowired
    public OrdersController(OrdersService ordersService, ThingService thingService) {
        this.ordersService = ordersService;
        this.thingService = thingService;
    }

    @GetMapping(path = "/orders/all", produces = { "application/json" })
    public ResponseEntity<?> findAllOrders() {

        List<Orders> ordersList = ordersService.findAll();

        if (ordersList == null || ordersList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ordersList);
    }

    @GetMapping(path = "/orders/", produces = { "application/json" })
    public ResponseEntity<?> findOrderById(@RequestHeader Long orderId) {

        if (orderId == null || orderId <= 0) {
            return ResponseEntity.badRequest().header("orderId should be higher than 0").build();
        }

        Orders orders = ordersService.findById(orderId);

        if (orders == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orders);
    }

    @PostMapping(path = "/orders/", produces = { "application/json" })
    public ResponseEntity<?> addOrder(@RequestHeader String customerName,
                                      @RequestBody Map<Long, Integer> things) {

        if (customerName == null || customerName.isEmpty()) {
            return ResponseEntity.badRequest().header("Cause", "customer name is empty").build();
        }

        if (things == null || things.isEmpty()) {
            return ResponseEntity.badRequest().header("Cause", "things is empty").build();
        }

        List<OrderThings> orderThingsList = new ArrayList<>();

        Set<Map.Entry<Long, Integer>> entrySetOfThings = things.entrySet();

        String bodyError = checkIfEntrySetOfThingsIsValid(entrySetOfThings);

        if (bodyError != null) {
            return ResponseEntity.badRequest().header("Cause", bodyError).build();
        }

        for(Map.Entry<Long, Integer> entry : entrySetOfThings) {

            Long thingId = entry.getKey();
            Integer quantity = entry.getValue();

            Thing thing = thingService.getById(thingId);
            thing.setQuantity(thing.getQuantity() - quantity);
            thingService.update(thing);

            orderThingsList.add(new OrderThings(0L, thing.getId(), quantity));
        }

        boolean isSuccess = ordersService.addOrder(customerName, orderThingsList);

        if (!isSuccess) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    private String checkIfEntrySetOfThingsIsValid(Set<Map.Entry<Long, Integer>> entrySet) {
        for(Map.Entry<Long, Integer> entry : entrySet) {

            Long thingId = entry.getKey();
            Integer quantity = entry.getValue();

            if (thingId == null || thingId <= 0) {
                return "RequestBody have error," +
                        " name = thingId and value = quantity , name and value are numeral, error in thingId, " +
                        "value is empty or below than 1, check name=" + thingId + " value=" + quantity;
            }

            if (quantity == null || quantity <= 0) {
                return "RequestBody have error," +
                        " name = thingId and value = quantity , name and value are numeral, error in quantity, " +
                        "value is empty or below than 1, check name=" + thingId + " value=" + quantity;
            }

            Thing thing = thingService.getById(thingId);

            if (thing == null) {
                return  "RequestBody have error," +
                        " no exist thing with id = " + thingId + " ,check name=" + thingId + " value=" + quantity;
            }

            if (thing.getQuantity() < quantity) {
                return "RequestBody have error," +
                        " too small quantity thing with id = " + thingId + " ,check name=" + thingId + " value=" + quantity;
            }
        }
        return null;
    }

    @DeleteMapping(path = "/orders/", produces = { "application/json" })
    public ResponseEntity<?> deleteOrder(@RequestHeader Long ordersId) {

        if (ordersId == null || ordersId.equals("")) {
            return ResponseEntity.badRequest().header("Cause", "ordersId name is empty").build();
        }

        boolean isSuccess = ordersService.deleteOrder(ordersId);

        if (!isSuccess) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
