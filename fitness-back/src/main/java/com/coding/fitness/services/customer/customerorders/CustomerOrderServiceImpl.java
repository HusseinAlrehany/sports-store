package com.coding.fitness.services.customer.customerorders;

import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.dtos.OrderSummary;
import com.coding.fitness.dtos.PlaceOrderDTO;
import com.coding.fitness.entity.CartItems;
import com.coding.fitness.entity.Coupon;
import com.coding.fitness.entity.Order;
import com.coding.fitness.entity.User;
import com.coding.fitness.enums.OrderStatus;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.OrderMapper;
import com.coding.fitness.repository.CartItemRepository;
import com.coding.fitness.repository.CouponRepository;
import com.coding.fitness.repository.OrderRepository;
import com.coding.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerOrderServiceImpl implements CustomerOrderService{

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final UserRepository userRepository;

    private final CartItemRepository cartItemRepository;

    private final CouponRepository couponRepository;

    private static final int FIXED_SHIPPING_COST = 20;

    @Override
    public OrderDTO placeOrder(PlaceOrderDTO placeOrderDTO) {

        User user = userRepository.findById(placeOrderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the user's cart items that are not yet part of an order
        List<CartItems> cartItems = cartItemRepository.findByUserIdAndOrderIsNull(placeOrderDTO.getUserId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Coupon coupon = null;
        if(placeOrderDTO.getCouponCode() != null && !placeOrderDTO.getCouponCode().isEmpty()){
            coupon = couponRepository.findByCode(placeOrderDTO.getCouponCode())
                    .orElseThrow(()-> new ValidationException("Invalid Coupon!"));

            boolean isCouponUsed = orderRepository.existsByCouponIdAndUserId(coupon.getId(), user.getId());
            if(isCouponUsed){
                throw new ValidationException("This Coupon is already used!!");
            }
        }
        // Calculate total amount
        Long totalAmount = cartItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();

        //Calculate the Amount
        Long amount = cartItems.stream()
                .mapToLong(CartItems::getQuantity)
                .sum();

        //apply coupon if provided and update the totalAmount
        if(coupon != null){
            OrderSummary orderSummary = applyCoupon(placeOrderDTO.getUserId(), placeOrderDTO.getCouponCode());
            totalAmount = orderSummary.getTotalPrice();
        }

        // Create new order , new record in the database table
        Order order = new Order();
        order.setUser(user);
        order.setDate(new Date());
        order.setOrderDescription(placeOrderDTO.getOrderDescription());
        order.setAddress(placeOrderDTO.getAddress());
        order.setOrderStatus(OrderStatus.PLACED);
        order.setTrackingId(UUID.randomUUID());
        order.setTotalAmount(totalAmount);
        order.setAmount(amount);
        order.setCoupon(coupon);
        if(coupon != null) {
            order.setDiscount(coupon.getDiscount());
        }
        // Save the order
        order = orderRepository.save(order);

        // Associate cart items with the order
        for (CartItems item : cartItems) {
            if(item.getOrder() == null){
                item.setOrder(order);
                cartItemRepository.save(item);
            }
        }

        return orderMapper.toDTO(order);

    }

    @Override
    public OrderSummary getOrderSummary(Long userId) {
        List<CartItems> cartItems = cartItemRepository.findByUserIdAndOrderIsNull(userId);
        if(cartItems.isEmpty()){
            throw new ValidationException("Cart Is Empty");
        }
        OrderSummary orderSummary = new OrderSummary();
        Long totalQuantity = cartItems.stream()
                .mapToLong(item-> item.getQuantity()).sum();
        Long totalPrice = cartItems.stream()
                .mapToLong(item-> item.getQuantity() * item.getPrice()).sum();
        Long subTotal = cartItems.stream()
                .mapToLong(item -> item.getQuantity() * item.getPrice()).sum() + FIXED_SHIPPING_COST;

        orderSummary.setTotalPrice(totalPrice);
        orderSummary.setTotalQuantity(totalQuantity);
        orderSummary.setSubTotal(subTotal);

        return orderSummary;
    }

    @Override
    public List<OrderDTO> findAllMyPlacedOrders(Long userId) {

        if(userId < 0){
            throw new ValidationException("Invalid userId");
        }
        List<Order> orders =orderRepository.findByUserIdAndOrderStatusIn(
                userId, List.of(OrderStatus.PLACED,
                        OrderStatus.DELIVERED, OrderStatus.SHIPPED));
        if(orders.isEmpty()){
            throw new ValidationException("No Orders Found");
        }

        return orderMapper.toDTOList(orders);

    }

    @Override
    public OrderSummary applyCoupon(Long userId, String code) {
        if(userId < 0){
            throw new ValidationException("Invalid id");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ValidationException("No user Found"));

        Coupon coupon = couponRepository.findByCode(code.trim())
                .orElseThrow(() -> new ValidationException("Invalid Coupon!"));
        if(IsCouponExpired(coupon)&& coupon!= null){
            throw new ValidationException("Coupon Is Expired");
        }

        boolean isCouponUsedByUser = orderRepository.existsByCouponIdAndUserId(coupon.getId(), user.getId());
        if(isCouponUsedByUser){
            throw new ValidationException("This Coupon is already used!");
        }

        OrderSummary orderSummary = getOrderSummary(userId);

        double discountAmount = ((coupon.getDiscount() / 100.0) * orderSummary.getTotalPrice());
        double netAmount = orderSummary.getTotalPrice() - discountAmount;
        orderSummary.setTotalPrice((long)netAmount);
        orderSummary.setSubTotal((long)netAmount);
        return orderSummary;
    }

    @Override
    public OrderDTO searchOrderByTrackingId(UUID trackingId) {
        Order order = orderRepository.findByTrackingId(trackingId)
                .orElseThrow(()-> new ValidationException("No Order Found!"));
        return orderMapper.toDTO(order);
    }

    public boolean IsCouponExpired(Coupon coupon) {
        Date currentDate = new Date();
        if(coupon != null){
            return coupon.getExpirationDate() != null && currentDate.after(coupon.getExpirationDate());
        }
        return false;
    }
}
