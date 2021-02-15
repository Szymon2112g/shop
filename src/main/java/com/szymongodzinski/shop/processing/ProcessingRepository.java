package com.szymongodzinski.shop.processing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingRepository extends JpaRepository<Processing, Long> {
    public Processing findByOrders_Id(Long ordersId);
}
