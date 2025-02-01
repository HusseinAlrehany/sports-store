package com.coding.fitness.repository;

import com.coding.fitness.entity.Order;
import com.coding.fitness.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdAndOrderStatusIn(Long userId, List<OrderStatus> orderStatus);
    List<Order> findAllByOrderStatusIn(List<OrderStatus> orderStatuses);

    //Optional<Order> findByUserIdAndCouponNotNull(Long userId);

    boolean existsByCouponIdAndUserId(Long couponId,Long userId);

    Optional<Order> findByTrackingId(UUID trackingId);

    List<Order> findByDateBetweenAndOrderStatus(Date startOfMonth, Date endOfMonth, OrderStatus orderStatus);

    Long countByOrderStatus(OrderStatus orderStatus);
}
