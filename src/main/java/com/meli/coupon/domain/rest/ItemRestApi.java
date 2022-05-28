package com.meli.coupon.domain.rest;

import com.meli.coupon.domain.model.Item;

public interface ItemRestApi {
    Item getItemPrice(String itemId);
}
