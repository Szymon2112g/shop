package com.szymongodzinski.shop.processing;

import com.szymongodzinski.shop.order.Orders;

import java.util.List;

public interface ProcessingService {

    public List<Processing> findAll();
    public Processing findById(Long processingId);
    public boolean addProcessingForOrder(Orders orders);
    public boolean changeStateOfProcessing(Long processingId, State state);
    public boolean deleteProcessing(Long processingId);
    public Processing findByOrdersId(Long ordersId);
}
