package com.meli.coupon.infrastructure.rest.api;

import com.meli.coupon.domain.model.Item;
import com.meli.coupon.domain.rest.ItemRestApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ItemRestApiProvider implements ItemRestApi {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Item getItemPrice(String itemId) {
        Item item = restTemplate.getForObject("https://api.mercadolibre.com/items/" + itemId, Item.class);
        return item;
    }
}
