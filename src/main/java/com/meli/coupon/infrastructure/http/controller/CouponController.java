package com.meli.coupon.infrastructure.http.controller;

import com.meli.coupon.application.service.CouponService;
import com.meli.coupon.application.dto.ItemsToBuyReq;
import com.meli.coupon.application.dto.ItemsToBuyRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping
    public ResponseEntity<ItemsToBuyRes> getItemsToBuy(@Valid @RequestBody ItemsToBuyReq request) {
        return new ResponseEntity<>(couponService.getItemsToBuy(request),
                HttpStatus.OK);
    }
}
