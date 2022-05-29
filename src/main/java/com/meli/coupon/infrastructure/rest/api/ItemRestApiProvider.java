package com.meli.coupon.infrastructure.rest.api;

import com.meli.coupon.infrastructure.rest.dto.Item;
import com.meli.coupon.domain.rest.ItemRestApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ItemRestApiProvider implements ItemRestApi {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @Override
    public Item getItemPrice(String itemId) {
        log.debug("Request Item API ".concat(itemId));
        Item itemApiResponse =
            restTemplate.getForObject(environment.getProperty("api.items.url") + itemId, Item.class);
        log.debug("Response Item API ".concat(String.valueOf(itemApiResponse)));
        return itemApiResponse;
    }
}
