package com.meli.coupon.application.helper;

import com.meli.coupon.infrastructure.rest.dto.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemToBuyHelper {

    private ItemToBuyHelper() {
        throw new IllegalStateException("ItemToBuyHelper class");
    }
    public static List<Item> calculateItemsToBuy(Float total, List<Item> itemsToValidate, List<Item> nextItemList,
        List<Item> itemsResponse) {
        // se suma el precio de todos los itemsToValidate agregados para validar
        Double priceNextItems = nextItemList.stream().mapToDouble(Item::getPrice).sum();
        // se suma el precio de todos los itemsToValidate de la lista de respueta
        Double priceItemsRes = itemsResponse.stream().mapToDouble(Item::getPrice).sum();
        Float sumPriceItems = priceNextItems.floatValue();

        // si la suma de los itemsToValidate es menor o igual al total y es mayor a la suma del arreglo
        // de respuesta
        if (sumPriceItems.floatValue() <= total.floatValue()
            && sumPriceItems.floatValue() > priceItemsRes.floatValue()) {
            itemsResponse.clear();
            itemsResponse.addAll(nextItemList);
        } else if (sumPriceItems.floatValue() < total.floatValue()) {
            // se ejecuta el ciclo siempre y cuando el  precio de los itemsToValidate sea menor al monto
            // ingresado
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
