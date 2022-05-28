package com.meli.coupon.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemsToBuyReq extends ItemsToBuy {
    private final float amount;

    @Builder
    public ItemsToBuyReq(List<String> item_ids, float amount) {
        super(item_ids);
        this.amount = amount;
    }
}
