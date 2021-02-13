package com.szymongodzinski.shop.controller;

import com.szymongodzinski.shop.processing.Processing;
import com.szymongodzinski.shop.processing.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProcessingController {

    private ProcessingService processingService;

    @Autowired
    public ProcessingController(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @GetMapping(path = "/processing/all")
    public List<Processing> findAllProcessing() {
        return processingService.findAll();
    }

    @PutMapping(path = "/processing/{id}/reject")
    public void rejectProcessing(@PathVariable int id) {
        processingService.rejectProcessing(Long.valueOf(id));
    }

    @PostMapping(path = "/processing/order/{id}/add")
    public void addProcessing(@PathVariable Long id) {
        processingService.addProcessingForOrder(id);
    }
}
