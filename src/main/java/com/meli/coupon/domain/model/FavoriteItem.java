package com.meli.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteItem {

    private Long id;
    private String item;
    private Integer quantity;
}
