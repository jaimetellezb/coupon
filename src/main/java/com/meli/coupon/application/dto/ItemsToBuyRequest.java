package com.meli.coupon.application.dto;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ItemsToBuyRequest extends ItemsToBuy {

    @NotNull
    @Min(1)
    private final float amount;

    public ItemsToBuyRequest(List<String> item_ids, float amount) {
        super(item_ids);
        this.amount = amount;
    }
}
