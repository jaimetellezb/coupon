package com.meli.coupon.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemsToBuyDto {
    private List<String> item_ids;
}
