package com.meli.coupon.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.meli.coupon.application.dto.ItemsToBuyRequest;
import com.meli.coupon.application.dto.ItemsToBuyResponse;
import com.meli.coupon.application.helper.ItemToBuyHelper;
import com.meli.coupon.domain.rest.ItemRestApi;
import com.meli.coupon.infrastructure.rest.dto.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

class CouponServiceTests {

    @InjectMocks
    private CouponService couponService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Mock
    private ItemRestApi itemRestApi;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getItemsToBuyOk() {
        // Data
        Float total = 500F;
        List<Item> items = Arrays.asList(new Item("ML1", 200F), new Item("ML2", 300F), new Item("ML3", 280F));

        // Execute mock rest api
        items.forEach(item -> {
            when(itemRestApi.getItemPrice(item.getId())).thenReturn(new Item(item.getId(), item.getPrice()));
        });

        // execute calculate items to buy
        List<Item> expectedItems = ItemToBuyHelper.calculateItemsToBuy(total, items, new ArrayList<>(),
            new ArrayList<>());
        Double totalPrice = expectedItems.stream().mapToDouble(Item::getPrice).sum();

        // Execute result
        ItemsToBuyRequest request = new ItemsToBuyRequest(items.stream().map(Item::getId).collect(Collectors.toList()),
            total);
        ItemsToBuyResponse itemsToBuy = couponService.getItemsToBuy(request);

        // Asserts
        verify(itemRestApi, times(items.size())).getItemPrice(any());
        assertEquals(totalPrice.floatValue(), itemsToBuy.getTotal());
    }


}
