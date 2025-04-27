package com.coding.fitness.mapper;

import com.coding.fitness.dtos.CouponDTO;
import com.coding.fitness.entity.Coupon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CouponMapper {


    public CouponDTO toDTO(Coupon coupon){
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setId(coupon.getId());
        couponDTO.setName(coupon.getName());
        couponDTO.setCode(coupon.getCode());
        couponDTO.setDiscount(coupon.getDiscount());
        couponDTO.setExpirationDate(coupon.getExpirationDate());

        return couponDTO;
    }

    public Coupon toEntity(CouponDTO couponDTO){
        Coupon coupon = new Coupon();
        coupon.setCode(couponDTO.getCode());
        coupon.setName(couponDTO.getName());
        coupon.setDiscount(couponDTO.getDiscount());
        coupon.setExpirationDate(couponDTO.getExpirationDate());

        return coupon;
    }

    public List<CouponDTO> toDTOList(List<Coupon> coupons){
        return coupons != null ?
                coupons.stream().map(this::toDTO)
                        .toList() :
                new ArrayList<>();
    }
}
