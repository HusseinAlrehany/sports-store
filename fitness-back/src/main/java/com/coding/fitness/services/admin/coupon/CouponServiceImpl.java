package com.coding.fitness.services.admin.coupon;

import com.coding.fitness.dtos.CouponDTO;
import com.coding.fitness.entity.Coupon;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.CouponMapper;
import com.coding.fitness.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements  CouponService{

    private final CouponRepository couponRepository;

    private final CouponMapper couponMapper;

    @Override
    public CouponDTO createCoupon(CouponDTO couponDTO) {
        if(couponRepository.existsByCode(couponDTO.getCode())){
            throw new ValidationException("Coupon Is Already Exists");
        }

        if(isCouponBlank(couponDTO.getCode()) || isCouponBlank(couponDTO.getName())){
            throw new ValidationException("Invalid Coupon name or code");
        }
        Coupon coupon = couponMapper.toEntity(couponDTO);

        return couponMapper.toDTO(couponRepository.save(coupon));

    }

    @Override
    public List<CouponDTO> findAll() {

        return couponMapper.toDTOList(couponRepository.findAll());
    }

    public boolean isCouponBlank(String value){
        return value == null || value.trim().isBlank();
    }

}
