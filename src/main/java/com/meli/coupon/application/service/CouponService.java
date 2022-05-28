package com.meli.coupon.application.service;

import com.meli.coupon.application.dto.ItemsToBuyRequest;
import com.meli.coupon.application.dto.ItemsToBuyResponse;
import com.meli.coupon.domain.model.Item;
import com.meli.coupon.domain.rest.ItemRestApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CouponService {

    @Autowired
    private ItemRestApi itemRestApi;

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
        List<Item> itemsRes = this.calculateItemsToBuy(request.getAmount(), itemsToValidate, new ArrayList<>(), new ArrayList<>());
        List<String> itemIds = itemsRes.stream().map(Item::getId).collect(Collectors.toList());
        Double priceItemsResponse = itemsRes.stream().mapToDouble(Item::getPrice).sum();

        ItemsToBuyResponse itemsToBuyResponse = ItemsToBuyResponse.builder().item_ids(itemIds).total(priceItemsResponse.floatValue()).build();
        log.info(itemsToBuyResponse.toString());
        return itemsToBuyResponse;
    }

    List<Item> calculateItemsToBuy(Float total, List<Item> itemsToValidate, List<Item> nextItemList, List<Item> itemsResponse) {
        // se suma el precio de todos los itemsToValidate agregados para validar
        Double priceNextItems = nextItemList.stream()
                .mapToDouble(Item::getPrice)
                .sum();
        // se suma el precio de todos los itemsToValidate de la lista de respueta
        Double priceItemsRes = itemsResponse.stream()
                .mapToDouble(Item::getPrice)
                .sum();
        Float sumPriceItems = priceNextItems.floatValue();

        // si la suma de los itemsToValidate es menor o igual al total y es mayor a la suma del arreglo de respuesta temporal
        // la lista de nextItemList a la de respuesta
        if (sumPriceItems.floatValue() <= total.floatValue() && sumPriceItems.floatValue() > priceItemsRes.floatValue()) {
            itemsResponse.clear();
            itemsResponse.addAll(nextItemList);
        } else if (sumPriceItems.floatValue() < total.floatValue()) {
            // se ejecuta el ciclo siempre y cuando el  precio de los itemsToValidate sea menor al monto ingresado
            // recorrer la lista de itemsToValidate
            for (int i = 0; i < itemsToValidate.size(); i++) {
                Item item = itemsToValidate.get(i);
                // valida siempre que la suma de los precios de itemsToValidate
                // sea diferente al total o monto ingresado
                if (sumPriceItems.floatValue() != total.floatValue()) {
                    List<Item> itemsTemp = new ArrayList<>(itemsToValidate);
                    // se elimina el item actual de la lista de itemsToValidate
                    // para iterar con el siguiente item en la lista
                    itemsTemp.remove(itemsTemp.get(i));
                    List<Item> nextItems = new ArrayList<>(nextItemList);
                    // se agrega el item actual en la lista que tiene las nuevas iteraciones
                    nextItems.add(item);
                    // se llama de nuevo el método, utilizando recursividad
                    calculateItemsToBuy(total, itemsTemp, nextItems, itemsResponse);
                }
            }
        }
        return itemsResponse;
    }

}
