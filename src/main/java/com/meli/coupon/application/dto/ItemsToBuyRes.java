package com.meli.coupon.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemsToBuyRes extends ItemsToBuy {
    private final float total;

    @Builder
    public ItemsToBuyRes(List<String> item_ids, Float total) {
        super(item_ids);
        this.total = total;
    }
}
