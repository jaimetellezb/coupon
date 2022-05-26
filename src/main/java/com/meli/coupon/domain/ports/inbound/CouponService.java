package com.meli.coupon.domain.ports.inbound;

import com.meli.coupon.domain.dto.ItemsToBuyReqDto;
import com.meli.coupon.domain.dto.ItemsToBuyResDto;

public interface CouponService {
    ItemsToBuyResDto itemsToBuy(ItemsToBuyReqDto request);
}
