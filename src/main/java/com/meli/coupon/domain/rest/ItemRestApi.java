package com.meli.coupon.domain.rest;

import com.meli.coupon.infrastructure.rest.dto.Item;

public interface ItemRestApi {

    Item getItemPrice(String itemId);
}
