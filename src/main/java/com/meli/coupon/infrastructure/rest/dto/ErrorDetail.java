package com.meli.coupon.infrastructure.rest.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetail {

    private final Date timestamp;
    private final String message;
    private final Object details;
}
