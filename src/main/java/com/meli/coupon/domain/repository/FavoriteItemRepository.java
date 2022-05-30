package com.meli.coupon.domain.repository;

import com.meli.coupon.domain.model.FavoriteItem;
import java.util.List;

public interface FavoriteItemRepository {

    List<FavoriteItem> getFavoriteItems(Integer limit);

    void saveUpdateFavoriteItems(List<String> items);
}
