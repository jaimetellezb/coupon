package com.meli.coupon.infrastructure.http.controller;

import com.meli.coupon.application.dto.ItemsToBuyRequest;
import com.meli.coupon.application.dto.ItemsToBuyResponse;
import com.meli.coupon.application.service.CouponService;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
@Validated
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping
    public ResponseEntity<ItemsToBuyResponse> getItemsToBuy(
        @Valid @RequestBody ItemsToBuyRequest request) {
        return new ResponseEntity<>(couponService.getItemsToBuy(request), HttpStatus.OK);
    }

    @GetMapping("favorites")
    public ResponseEntity<List<Map<String, Integer>>> getFavoriteItems(@RequestParam() @Min(1) Integer limit) {
        return new ResponseEntity<>(couponService.getFavoriteItems(limit), HttpStatus.OK);
    }
}
