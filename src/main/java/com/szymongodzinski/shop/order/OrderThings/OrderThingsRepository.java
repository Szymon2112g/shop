package com.szymongodzinski.shop.order.OrderThings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderThingsRepository extends JpaRepository<OrderThings, Long> {
}
