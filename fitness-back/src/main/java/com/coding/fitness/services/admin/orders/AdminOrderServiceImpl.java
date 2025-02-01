package com.coding.fitness.services.admin.orders;
import com.coding.fitness.dtos.AnalyticsResponse;
import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.entity.Coupon;
import com.coding.fitness.entity.Order;
import com.coding.fitness.enums.OrderStatus;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
import com.coding.fitness.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    private final Mapper mapper;
    @Override
    public List<OrderDTO> findAllOrders() {
       List<Order> orders = Optional.of(orderRepository.findAllByOrderStatusIn(List.of(OrderStatus.PLACED, OrderStatus.DELIVERED, OrderStatus.SHIPPED)))
               .filter(ord-> !ord.isEmpty())
               .orElseThrow(()-> new ValidationException("No Orders Found"));
        return orders.stream()
                .map(mapper::getOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Optional.ofNullable(orderDTO.getId())
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid OrderId"));
      Optional <Order> order =  Optional.of(orderRepository.findById(orderDTO.getId()))
                .filter(Optional::isPresent)
                .orElseThrow(()-> new ValidationException("No Order Found"));
     Optional.of(order.get())
             .filter(ord-> !isOrderUpdateExpired(ord))
             .orElseThrow(()-> new ValidationException("Can not update Order, Max Period for update is 24h"));

         order.get().setOrderStatus(OrderStatus.PLACED);
         order.get().setOrderDescription(orderDTO.getOrderDescription());
         order.get().setDate(new Date());
         order.get().setTrackingId(UUID.randomUUID());
         order.get().setAddress(orderDTO.getAddress());

        Order orderDB = orderRepository.save(order.get());

        return mapper.getOrderDTO(orderDB);
    }

    @Override
    public boolean isOrderUpdateExpired(Order order) {
        Date currentDate = new Date();
        Date orderUpdateExpiration = order.getDate();

        return orderUpdateExpiration != null && currentDate.after(orderUpdateExpiration);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Optional.ofNullable(orderId)
                .filter(id -> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid Id"));
        Optional.of(orderRepository.findById(orderId))
                .filter(ord-> ord.isPresent())
                .orElseThrow(()-> new ValidationException("No Order Found"));

         orderRepository.deleteById(orderId);
    }

    @Override
    public OrderDTO changeOrderStatus(Long orderId, String orderStatus) {
        Optional.ofNullable(orderId)
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid order id"));

        String trimmedStatus = Optional.ofNullable(orderStatus)
                .map(String::trim)
                .filter(status -> !status.isEmpty())
                .orElseThrow(() -> new ValidationException("Invalid order status"));

          Order order   = orderRepository.findById(orderId)
                  .orElseThrow(()-> new ValidationException("No Order Found"));

           Map<String, Consumer<Order>> statusActions = Map.of(
                "Shipped", ord-> ord.setOrderStatus(OrderStatus.SHIPPED),
                "Delivered", ord-> ord.setOrderStatus(OrderStatus.DELIVERED));

          Optional.ofNullable(statusActions.get(trimmedStatus))
                  .ifPresentOrElse(
                          action-> action.accept(order),
                          ()-> {throw new ValidationException("Invalid order status Too");}
                  );

        Order dbOrder = orderRepository.save(order);
        return mapper.getOrderDTO(dbOrder);
    }

    @Override
    public AnalyticsResponse calculateAnalytics() {

        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonthDate = currentDate.minusMonths(1);

        Long currentMonthOrders = getTotalOrdersForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthOrders = getTotalOrdersForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long currentMonthEarnings = getTotalEarningsForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthEarning = getTotalEarningsForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long placedOrders = orderRepository.countByOrderStatus(OrderStatus.PLACED);
        Long shippedOrders = orderRepository.countByOrderStatus(OrderStatus.SHIPPED);
        Long deliveredOrders = orderRepository.countByOrderStatus(OrderStatus.DELIVERED);

        return new AnalyticsResponse(placedOrders, shippedOrders, deliveredOrders,
                                     currentMonthOrders, previousMonthOrders,
                                     currentMonthEarnings, previousMonthEarning
                                    );
    }

    private Long getTotalOrdersForMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();
        //move calendar to the end of the specified month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();

        List<Order> orders = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth,endOfMonth, OrderStatus.DELIVERED);

        return (long)orders.size();
    }


    private Long getTotalEarningsForMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();
        //move calendar to the end of the specified month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();

        List<Order> orders = orderRepository.findByDateBetweenAndOrderStatus(startOfMonth,endOfMonth, OrderStatus.DELIVERED);

        return orders.stream().mapToLong(Order::getTotalAmount).sum();
    }


}
