package com.agro.orderservice.repository;

import com.agro.orderservice.model.Order;
import com.agro.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(String userId);

    List<Order> findByProductIdIn(List<Long> productIds);

    long countByStatus(OrderStatus status);



    @Query("""
SELECT COALESCE(SUM(o.totalPrice),0)
FROM Order o
WHERE o.status='DELIVERED'
""")
    Double getTotalRevenue();


}
