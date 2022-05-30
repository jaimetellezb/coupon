package com.meli.coupon.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {

    private String id;
    private Float price;
}
