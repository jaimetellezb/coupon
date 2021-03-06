package com.meli.coupon.application.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ItemsToBuyResponse extends ItemsToBuy {

    private final float total;

    @Builder
    public ItemsToBuyResponse(List<String> item_ids, Float total) {
        super(item_ids);
        this.total = total;
    }
}
