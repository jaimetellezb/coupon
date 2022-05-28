package com.meli.coupon.application.service;

import com.meli.coupon.domain.model.Item;
import com.meli.coupon.application.dto.ItemsToBuyReq;
import com.meli.coupon.application.dto.ItemsToBuyRes;
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
    private ItemRestApi itemService;

    public ItemsToBuyRes getItemsToBuy(ItemsToBuyReq request) {
        List<Item> items = new ArrayList<>();

        // revisar si hay items repetidos y quitarlos
        List<String> itemsWithoutDuplicates = request.getItem_ids().stream().distinct().collect(Collectors.toList());
        itemsWithoutDuplicates.stream().forEach(element -> {
            // consumir servicio de mercado libre para obtener precio de item
            Item item = itemService.getItemPrice(element);
            if (item.getPrice() < request.getAmount()) {
                items.add(item);
            }
        });
        List<Item>  itemsRes = this.calculateItemsToBuy(request.getAmount(), items, new ArrayList<>(), new ArrayList<>());
        List<String> itemIds = itemsRes.stream().map(w -> w.getId()).collect(Collectors.toList());
        Double priceItemsRes = itemsRes.stream().mapToDouble(Item::getPrice).sum();

        return ItemsToBuyRes.builder().item_ids(itemIds).total(priceItemsRes.floatValue()).build();
    }

    List<Item> calculateItemsToBuy(Float total, List<Item> items, List<Item> nextItemsArr, List<Item> itemsResponse) {
        Float sumPriceItems = 0F;
        // se suma el precio de todos los items agregados para validar
        Double priceNextItems = nextItemsArr.stream()
                .mapToDouble(Item::getPrice)
                .sum();
        // se suma el precio de todos los items de la lista de respueta
        Double priceItemsRes = itemsResponse.stream()
                .mapToDouble(Item::getPrice)
                .sum();
        sumPriceItems = priceNextItems.floatValue();

        // si la suma de los items es igual a el total, agrega
        // la lista de nextItemsArr a la de respuesta
        if (sumPriceItems.floatValue() == total.floatValue()) {
            itemsResponse.clear();
            itemsResponse.addAll(nextItemsArr);
        } else if (sumPriceItems.floatValue() <= total.floatValue() && sumPriceItems.floatValue() > priceItemsRes.floatValue()) {
            // si la suma de los items es menor o igual al monto y la suma es mayor a la suma del arreglo de resta
            // se agrega el arreglo actual al arreglo de respuesta
            itemsResponse.clear();
            itemsResponse.addAll(nextItemsArr);
        } else if (sumPriceItems.floatValue() < total.floatValue()) {
            // se ejecuta el ciclo siempre y cuando el  precio de los items sea menor al monto ingresado
            // recorrer la lista de items
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                // valida siempre que la suma de los precios de items
                // sea diferente al total o monto ingresado
                if (sumPriceItems.floatValue() != total.floatValue()) {
                    List<Item> itemsTemp = new ArrayList<>(items);
                    // se elimina el item actual de la lista de items
                    // para iterar con el siguiente item en la lista
                    itemsTemp.remove(itemsTemp.get(i));
                    List<Item> nextItems = new ArrayList<>(nextItemsArr);
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
