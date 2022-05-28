package com.meli.coupon.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(callSuper = true)
public class ItemsToBuyRequest extends ItemsToBuy {
    private final float amount;

    @Builder
    public ItemsToBuyRequest(List<String> item_ids, float amount) {
        super(item_ids);
        this.amount = amount;
    }
}
