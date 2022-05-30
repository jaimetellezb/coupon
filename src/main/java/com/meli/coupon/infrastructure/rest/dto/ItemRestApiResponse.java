package com.meli.coupon.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRestApiResponse {

    private int code;
    private Item body;
}
