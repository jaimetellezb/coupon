package com.meli.coupon.infrastructure.api;

import com.meli.coupon.domain.dto.ItemDto;
import com.meli.coupon.domain.ports.outbound.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MeliApiProvider implements ItemService {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public ItemDto getItemPrice(String itemId) {
        ItemDto item = restTemplate.getForObject("https://api.mercadolibre.com/items/" + itemId, ItemDto.class);
        return item;
    }
}
