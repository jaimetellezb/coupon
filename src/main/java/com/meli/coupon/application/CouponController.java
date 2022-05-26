package com.meli.coupon.application;

import com.meli.coupon.domain.dto.ItemsToBuyReqDto;
import com.meli.coupon.domain.dto.ItemsToBuyResDto;
import com.meli.coupon.domain.ports.inbound.CouponService;
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
    public ResponseEntity<ItemsToBuyResDto> itemsToBuy(@Valid @RequestBody ItemsToBuyReqDto request) {
        return new ResponseEntity<>(couponService.itemsToBuy(request),
                HttpStatus.OK);
    }
}
