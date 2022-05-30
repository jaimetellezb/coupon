package com.meli.coupon.application.service;

import com.meli.coupon.application.dto.ItemsToBuyRequest;
import com.meli.coupon.application.dto.ItemsToBuyResponse;
import com.meli.coupon.application.helper.ItemToBuyHelper;
import com.meli.coupon.domain.model.FavoriteItem;
import com.meli.coupon.domain.repository.FavoriteItemRepository;
import com.meli.coupon.domain.rest.ItemRestApi;
import com.meli.coupon.infrastructure.rest.dto.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponService {

    @Autowired
    private ItemRestApi itemRestApi;

    @Autowired
    private FavoriteItemRepository favoriteItemRepository;

    public ItemsToBuyResponse getItemsToBuy(ItemsToBuyRequest request) {
        log.info(request.toString());
        List<Item> itemsToValidate = new ArrayList<>();

        List<String> itemsWithoutDuplicates = request.getItem_ids().stream().distinct().collect(Collectors.toList());
        log.debug("Items Ids to validate ".concat(itemsWithoutDuplicates.toString()));
        itemsWithoutDuplicates.stream().forEach(element -> {
            // consumir servicio de mercado libre para obtener precio de item
            Item item = itemRestApi.getItemPrice(element);
            if (item.getPrice() <= request.getAmount()) {
                itemsToValidate.add(item);
            }
        });
        List<Item> itemsRes = ItemToBuyHelper.calculateItemsToBuy(request.getAmount(), itemsToValidate,
            new ArrayList<>(),
            new ArrayList<>());
        List<String> itemIds = itemsRes.stream().map(Item::getId).collect(Collectors.toList());
        Double priceItemsResponse = itemsRes.stream().mapToDouble(Item::getPrice).sum();

        ItemsToBuyResponse itemsToBuyResponse = ItemsToBuyResponse.builder().item_ids(itemIds)
            .total(priceItemsResponse.floatValue()).build();

        favoriteItemRepository.saveUpdateFavoriteItems(itemsToBuyResponse.getItem_ids());
        log.info(itemsToBuyResponse.toString());
        return itemsToBuyResponse;
    }

    public List<Map<String, Integer>> getFavoriteItems(Integer limit) {
        log.info("Request: limit=" + limit);
        Map<String, Integer> favoritesMap = new HashMap<>();
        List<FavoriteItem> favoriteItems = favoriteItemRepository.getFavoriteItems(limit);

        favoriteItems.stream().forEach(item -> favoritesMap.put(item.getItem(), item.getQuantity()));
        Map<String, Integer> favoritesMapReversed = new LinkedHashMap<>();
        favoritesMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEachOrdered(x -> favoritesMapReversed.put(x.getKey(), x.getValue()));
        List<Map<String, Integer>> favoritesResponse = new ArrayList<>();
        favoritesResponse.add(favoritesMapReversed);
        log.info("Response: Items=" + favoritesResponse);
        return favoritesResponse;
    }
}
