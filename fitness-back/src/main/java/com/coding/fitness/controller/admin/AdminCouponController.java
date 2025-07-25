package com.coding.fitness.controller.admin;

import com.coding.fitness.dtos.CouponDTO;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.services.admin.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/coupons")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;


    @PostMapping
    public ResponseEntity<?> createCoupon(@RequestBody CouponDTO couponDTO) {
        try {
            CouponDTO createdCoupon =  couponService.createCoupon(couponDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdCoupon);
        }
        catch(ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<List<CouponDTO>> findAllCoupons(){
         return ResponseEntity.ok(couponService.findAll());
    }

}
