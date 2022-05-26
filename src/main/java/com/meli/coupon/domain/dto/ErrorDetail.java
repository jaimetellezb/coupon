package com.meli.coupon.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ErrorDetail {
    private final Date timestamp;
    private final String message;
    private final String details;
}
