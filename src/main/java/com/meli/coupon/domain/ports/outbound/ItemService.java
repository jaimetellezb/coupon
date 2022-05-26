package com.meli.coupon.domain.ports.outbound;

import com.meli.coupon.domain.dto.ItemDto;

public interface ItemService {
    ItemDto getItemPrice(String itemId);
}
