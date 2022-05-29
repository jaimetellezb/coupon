package com.meli.coupon.application.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ItemsToBuy {

    @NotBlank.List({})
    private List<String> item_ids;
}
