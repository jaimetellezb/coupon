package com.meli.coupon.infrastructure.dto;

import com.meli.coupon.domain.dto.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeliApiResponse {
    private int code;
    private ItemDto body;
}
