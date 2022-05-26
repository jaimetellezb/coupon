package com.meli.coupon.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemsToBuyReqDto extends ItemsToBuyDto {
    private final float amount;

    @Builder
    public ItemsToBuyReqDto(List<String> item_ids, float amount) {
        super(item_ids);
        this.amount = amount;
    }
}
