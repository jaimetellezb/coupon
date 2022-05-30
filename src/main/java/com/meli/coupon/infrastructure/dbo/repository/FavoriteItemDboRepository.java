package com.meli.coupon.infrastructure.dbo.repository;

import com.meli.coupon.infrastructure.dbo.entity.FavoriteItemEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteItemDboRepository extends JpaRepository<FavoriteItemEntity, Long> {

    @Query(nativeQuery = true, value = "select * from favorite_items f order by quantity desc limit :limit ")
    List<FavoriteItemEntity> findTopItemsMaxQuantity(@Param("limit") Integer limit);

    @Query(nativeQuery = true, value = "select * from favorite_items f where f.item =:item ")
    FavoriteItemEntity findByItem(@Param("item") String item);
}
