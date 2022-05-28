package com.meli.coupon.infrastructure.http.controller;

import com.meli.coupon.application.service.CouponService;
import com.meli.coupon.application.dto.ItemsToBuyRequest;
import com.meli.coupon.application.dto.ItemsToBuyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping
    public ResponseEntity<ItemsToBuyResponse> getItemsToBuy(@Valid @RequestBody ItemsToBuyRequest request) {
        return new ResponseEntity<>(couponService.getItemsToBuy(request),
                HttpStatus.OK);
    }

}
