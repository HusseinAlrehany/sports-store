package com.coding.fitness.services.admin.coupon;

import com.coding.fitness.dtos.CouponDTO;
import com.coding.fitness.dtos.OrderSummary;
import com.coding.fitness.entity.Coupon;

import java.util.List;

public interface CouponService {
    CouponDTO createCoupon(CouponDTO couponDTO);

    List<CouponDTO> findAll();

}
