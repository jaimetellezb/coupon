package com.meli.coupon.infrastructure.rest.dto;

import com.meli.coupon.domain.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRestApiResponse {
    private int code;
    private Item body;
}
