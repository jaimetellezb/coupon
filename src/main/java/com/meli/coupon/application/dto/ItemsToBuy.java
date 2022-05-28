package com.meli.coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemsToBuy {
    private List<String> item_ids;
}
