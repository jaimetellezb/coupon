package com.meli.coupon.domain.service;

import com.meli.coupon.domain.dto.ItemDto;
import com.meli.coupon.domain.dto.ItemsToBuyReqDto;
import com.meli.coupon.domain.dto.ItemsToBuyResDto;
import com.meli.coupon.domain.ports.inbound.CouponService;
import com.meli.coupon.domain.ports.outbound.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private ItemService itemService;

    @Override
    public ItemsToBuyResDto itemsToBuy(ItemsToBuyReqDto request) {
        List<ItemDto> items = new ArrayList<>();

        // revisar si hay items repetidos y quitarlos
        List<String> itemsWithoutDuplicates = request.getItem_ids().stream().distinct().collect(Collectors.toList());
        itemsWithoutDuplicates.parallelStream().forEach(element -> {
            // consumir servicio de mercado libre para obtener precio de item
            ItemDto item = itemService.getItemPrice(element);
            if (item.getPrice() < request.getAmount()) {
                items.add(item);
            }
        });
        List<ItemDto>  itemsRes = this.calculateItemsToBuy(request.getAmount(), items, new ArrayList<>(), new ArrayList<>());
        List<String> itemIds = itemsRes.stream().map(w -> w.getId()).collect(Collectors.toList());
        Double priceItemsRes = itemsRes.stream().mapToDouble(ItemDto::getPrice).sum();

        return ItemsToBuyResDto.builder().item_ids(itemIds).total(priceItemsRes.floatValue()).build();
    }

    List<ItemDto> calculateItemsToBuy(Float total, List<ItemDto> items, List<ItemDto> nextItemsArr, List<ItemDto> itemsResponse) {
        Float sumPriceItems = 0F;
        // se suma el precio de todos los items agregados para validar
        Double priceNextItems = nextItemsArr.stream()
                .mapToDouble(ItemDto::getPrice)
                .sum();
        // se suma el precio de todos los items de la lista de respueta
        Double priceItemsRes = itemsResponse.stream()
                .mapToDouble(ItemDto::getPrice)
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
                ItemDto item = items.get(i);
                // valida siempre que la suma de los precios de items
                // sea diferente al total o monto ingresado
                if (sumPriceItems.floatValue() != total.floatValue()) {
                    List<ItemDto> itemsTemp = new ArrayList<>(items);
                    // se elimina el item actual de la lista de items
                    // para iterar con el siguiente item en la lista
                    itemsTemp.remove(itemsTemp.get(i));
                    List<ItemDto> nextItems = new ArrayList<>(nextItemsArr);
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
