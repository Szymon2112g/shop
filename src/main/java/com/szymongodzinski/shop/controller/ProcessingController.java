package com.szymongodzinski.shop.controller;

import com.szymongodzinski.shop.order.Orders;
import com.szymongodzinski.shop.order.OrdersService;
import com.szymongodzinski.shop.processing.Processing;
import com.szymongodzinski.shop.processing.ProcessingService;
import com.szymongodzinski.shop.processing.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProcessingController {

    private ProcessingService processingService;
    private OrdersService ordersService;

    @Autowired
    public ProcessingController(ProcessingService processingService, OrdersService ordersService) {
        this.processingService = processingService;
        this.ordersService = ordersService;
    }

    @GetMapping(path = "/processing/all", produces = { "application/json" })
    public ResponseEntity<?> findAllProcessing() {

        List<Processing> processingList = processingService.findAll();

        if (processingList == null || processingList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(processingList);
    }

    @GetMapping(path = "/processing/", produces = { "application/json" })
    public ResponseEntity<?> getProcessingById(@RequestHeader Long processingId) {

        if (processingId == null || processingId <= 0) {
            return ResponseEntity.badRequest().header("processingId should be higher than 0").build();
        }

        Processing processing = processingService.findById(processingId);

        if (processing == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(processing);
    }


    @PutMapping(path = "/processing/{processingId}/{state}", produces = { "application/json" })
    public ResponseEntity<?> changeStateOfProcessing(@PathVariable Long processingId,
                                                     @PathVariable String state) {

        if (processingId == null || processingId <= 0) {
            return ResponseEntity.badRequest().header("processingId should be higher than 0").build();
        }

        if (state == null || state.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        State stateOfProcessing;

        switch (state) {
            case "delivered":
                stateOfProcessing = State.DELIVERED;
                break;
            case "in-progress":
                stateOfProcessing = State.IN_PROGRESS;
                break;
            case "ready":
                stateOfProcessing = State.READY;
                break;
            case "rejected":
                stateOfProcessing = State.REJECTED;
                break;
            default:
                return ResponseEntity.badRequest().header("Cause",
                        "invalid state you should use " +
                                "\"delivered\", \"in-progress\", \"ready\" or \"rejected\"").build();
        }

        boolean isSuccess = processingService.changeStateOfProcessing(processingId, stateOfProcessing);

        if (!isSuccess) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/processing/order/{orderId}", produces = { "application/json" })
    public ResponseEntity<?> addProcessing(@PathVariable Long orderId) {

        if (orderId == null || orderId <= 0) {
            return ResponseEntity.badRequest().header("Cause", "orderId should be higher than 0").build();
        }

        Orders orders = ordersService.findById(orderId);

        if (orders == null) {
            return ResponseEntity.notFound().header("Cause", "order no exist").build();
        }

        if (processingService.findByOrdersId(orderId) != null) {
            return ResponseEntity.notFound().header("Cause", "processing for this order already exist").build();
        }

        boolean isSuccess = processingService.addProcessingForOrder(orders);

        if (!isSuccess) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/processing/")
    public ResponseEntity<?> deleteProcessing(@RequestParam Long processingId) {

        if (processingId == null || processingId <= 0) {
            return ResponseEntity.badRequest().header("processingId should be higher than 0").build();
        }

        boolean isSuccess = processingService.deleteProcessing(processingId);

        if (!isSuccess) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

}
