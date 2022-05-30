package com.meli.coupon.infrastructure.dbo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.coupon.domain.model.FavoriteItem;
import com.meli.coupon.domain.repository.FavoriteItemRepository;
import com.meli.coupon.infrastructure.dbo.entity.FavoriteItemEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteItemPersistence implements FavoriteItemRepository {

    @Autowired
    private FavoriteItemDboRepository favoriteItemDboRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public List<FavoriteItem> getFavoriteItems(Integer limit) {
        List<FavoriteItem> favorites = new ArrayList<>();
        List<FavoriteItemEntity> favoritesEntity = favoriteItemDboRepository.findTopItemsMaxQuantity(limit);
        favoritesEntity.stream().forEach(item -> favorites.add(objectMapper.convertValue(item, FavoriteItem.class)));
        return favorites;
    }

    @Override
    public void saveUpdateFavoriteItems(List<String> items) {
        items.stream().forEach(item -> {
            FavoriteItemEntity entity = favoriteItemDboRepository.findByItem(item);

            if (entity != null) {
                entity.setQuantity(entity.getQuantity() + 1);
            } else {
                entity = FavoriteItemEntity.builder().item(item).quantity(1).build();
            }
            favoriteItemDboRepository.save(entity);
        });
    }
}
