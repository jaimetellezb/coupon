package com.meli.coupon.infrastructure.dbo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.coupon.domain.model.FavoriteItem;
import com.meli.coupon.infrastructure.dbo.entity.FavoriteItemEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FavoriteItemPersistenceTests {

    @InjectMocks
    private FavoriteItemPersistence favoriteItemPersistence;
    @Mock
    private FavoriteItemDboRepository favoriteItemDboRepository;
    @Mock
    private ObjectMapper objectMapper;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFavoriteItemsOk() {
        Integer limit = 5;
        List<FavoriteItemEntity> response = new ArrayList<>();
        response.add(FavoriteItemEntity.builder().item("ML1").id(1L).quantity(10).build());
        response.add(FavoriteItemEntity.builder().item("ML2").id(2L).quantity(9).build());
        response.add(FavoriteItemEntity.builder().item("ML3").id(3L).quantity(7).build());
        FavoriteItemEntity it = new FavoriteItemEntity();
        it.setId(4L);
        it.setItem("ML4");
        it.setQuantity(6);
        response.add(it);
        response.add(new FavoriteItemEntity(5L, "ML5", 5));
        when(favoriteItemDboRepository.findTopItemsMaxQuantity(limit)).thenReturn(response);
        List<FavoriteItem> favorites = favoriteItemPersistence.getFavoriteItems(limit);
        assertEquals(favorites.size(), limit);
    }

    @Test
    void saveUpdateFavoriteItemsOk() {
        List<String> ids = Arrays.asList("ML1", "ML2", "ML3");
        ids.forEach(item -> {
            when(favoriteItemDboRepository.findByItem(item)).thenReturn(any(FavoriteItemEntity.class));
        });
        favoriteItemPersistence.saveUpdateFavoriteItems(ids);
        verify(favoriteItemDboRepository, times(ids.size())).findByItem(anyString());
    }
}
