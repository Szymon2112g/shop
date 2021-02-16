package com.szymongodzinski.shop.processing;

import com.szymongodzinski.shop.order.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcessingServiceImpl implements ProcessingService {

    private ProcessingRepository processingRepository;

    @Autowired
    public ProcessingServiceImpl(ProcessingRepository processingRepository) {
        this.processingRepository = processingRepository;
    }

    @Override
    public List<Processing> findAll() {
        return processingRepository.findAll();
    }

    @Override
    public Processing findById(Long processingId) {

        if (processingId == null || processingId <= 0) {
            return null;
        }

        Optional<Processing> optionalProcessing = processingRepository.findById(processingId);

        if (optionalProcessing.isEmpty()) {
            return null;
        }

        Processing processing = optionalProcessing.get();
        return processing;
    }

    @Override
    public boolean addProcessingForOrder(Orders orders) {

        if (orders == null) {
            return false;
        }

        Processing processing = new Processing(0L, State.IN_PROGRESS, orders);
        processingRepository.save(processing);
        return true;
    }

    @Override
    public boolean changeStateOfProcessing(Long processingId, State state) {

        if (processingId == null || processingId <= 0) {
            return false;
        }

        Optional<Processing> optionalProcessing = processingRepository.findById(processingId);

        if (optionalProcessing.isEmpty()) {
            return false;
        }

        if (state == null) {
            return false;
        }

        Processing processing = optionalProcessing.get();
        processing.setState(state);
        processingRepository.save(processing);

        return true;
    }

    @Override
    public boolean deleteProcessing(Long processingId) {

        if (processingId == null || processingId <= 0) {
            return false;
        }

        Optional<Processing> optionalProcessing = processingRepository.findById(processingId);

        if (optionalProcessing.isEmpty()) {
            return false;
        }

        Processing processing = optionalProcessing.get();;
        processingRepository.delete(processing);

        return true;
    }

    @Override
    public Processing findByOrdersId(Long ordersId) {

        if (ordersId == null || ordersId <= 0) {
            return null;
        }

        Processing processing = processingRepository.findByOrders_Id(ordersId);
        return processing;
    }
}
