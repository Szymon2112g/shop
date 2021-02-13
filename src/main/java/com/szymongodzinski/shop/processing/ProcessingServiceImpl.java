package com.szymongodzinski.shop.processing;

import com.szymongodzinski.shop.order.Orders;
import com.szymongodzinski.shop.order.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcessingServiceImpl implements ProcessingService {

    private ProcessingRepository processingRepository;
    private OrdersService ordersService;

    @Autowired
    public ProcessingServiceImpl(ProcessingRepository processingRepository, OrdersService ordersService) {
        this.processingRepository = processingRepository;
        this.ordersService = ordersService;
    }

    @Override
    public boolean rejectProcessing(Long id) {
        Optional<Processing> optionalProcessing = processingRepository.findById(id);

        Processing processing = optionalProcessing.get();

        processing.setState(State.REJECTED);
        processingRepository.save(processing);

        return true;
    }

    @Override
    public List<Processing> findAll() {
        return processingRepository.findAll();
    }

    @Override
    public boolean addProcessingForOrder(Long orders_Id) {

        List<Orders> orders = ordersService.findAll();

        Processing processing = new Processing(0L, State.IN_PROGRESS, orders.get(0));
        processingRepository.save(processing);
        return true;
    }
}
