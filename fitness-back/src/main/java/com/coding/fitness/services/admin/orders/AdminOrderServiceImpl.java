package com.coding.fitness.services.admin.orders;
import com.coding.fitness.dtos.AnalyticsResponse;
import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.entity.Order;
import com.coding.fitness.enums.OrderStatus;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.OrderMapper;
import com.coding.fitness.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    //private final Mapper mapper;

    private final OrderMapper orderMapper;
    @Override
    public List<OrderDTO> findAllOrders() {
       List<Order> orders = orderRepository.
                            findAllByOrderStatusIn(List.of(OrderStatus.PLACED,
                                                           OrderStatus.DELIVERED,
                                                           OrderStatus.SHIPPED));
       if(orders.isEmpty()){
           throw new ValidationException("No Orders Found");
       }

        return orderMapper.toDTOList(orders);
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        if(orderDTO.getId() < 0){
            throw new ValidationException("Invalid OrderId");
        }
      Order order =  orderRepository.findById(orderDTO.getId()).
              orElseThrow(()-> new ValidationException("No Order Found"));

        if(!isOrderUpdateExpired(order)){
            throw new ValidationException("Can not update Order, Max Period for update is 24h");
        }

         orderMapper.updateOrder(order, orderDTO);

        Order orderDB = orderRepository.save(order);

        return orderMapper.toDTO(orderDB);
    }

    @Override
    public boolean isOrderUpdateExpired(Order order) {
        Date currentDate = new Date();
        Date orderUpdateExpiration = order.getDate();

        return orderUpdateExpiration != null && currentDate.after(orderUpdateExpiration);
    }

    @Override
    public void deleteOrder(Long orderId) {
        if(orderId < 0){
            throw new ValidationException("Invalid Id");
        }
        orderRepository.findById(orderId)
                .orElseThrow(()-> new ValidationException("No Order Found"));

         orderRepository.deleteById(orderId);
    }

    @Override
    public OrderDTO changeOrderStatus(Long orderId, String orderStatus) {
        if(orderId < 0){
            throw new ValidationException("Invalid order id");
        }

        //Validating the incoming orderStatus if it passed the order status will be updated
         OrderStatus status = OrderStatus.fromStringToOrderStatus(orderStatus);

          Order order   = orderRepository.findById(orderId)
                  .orElseThrow(()-> new ValidationException("No Order Found"));

          //update order status
          order.setOrderStatus(status);
          //if else if here one condition will be executed and the other is skipped
         //but if if both are evaluated but the last one if it is true it will override the first and will be executed

          Order dbOrder = orderRepository.save(order);
         return orderMapper.toDTO(dbOrder);
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
