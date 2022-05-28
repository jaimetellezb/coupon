package com.meli.coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class ItemsToBuy {
    private List<String> item_ids;
}
