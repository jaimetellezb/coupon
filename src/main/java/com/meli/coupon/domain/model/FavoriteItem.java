package com.meli.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteItem {

    private Long id;
    private String item;
    private Integer quantity;
}
