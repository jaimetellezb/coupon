package com.meli.coupon.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemsToBuyResDto extends ItemsToBuyDto {
    private final float total;

    @Builder
    public ItemsToBuyResDto(List<String> item_ids, Float total) {
        super(item_ids);
        this.total = total;
    }
}
