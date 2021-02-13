package com.szymongodzinski.shop.processing;

import com.szymongodzinski.shop.order.Orders;

import java.util.List;

public interface ProcessingService {

    public boolean rejectProcessing(Long id);

    public List<Processing> findAll();

    public boolean addProcessingForOrder(Long orders_Id);
}
