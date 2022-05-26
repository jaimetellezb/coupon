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
import java.util.Arrays;
import java.util.Comparator;
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
        final Float[] sumTotal = {0f};


        // revisar si hay items repetidos y quitarlos
        List<String> itemsWithoutDuplicates = request.getItem_ids().stream().distinct().collect(Collectors.toList());
        itemsWithoutDuplicates.stream().forEach(element -> {
            // consumir servicio de mercado libre para obtener precio de item
            ItemDto item = itemService.getItemPrice(element);
            if (item.getPrice() < request.getAmount()) {
                items.add(item);
            }
        });

        List<ItemDto> sortedItemPrice = items.stream().sorted(Comparator.comparingDouble(ItemDto::getPrice))
                .collect(Collectors.toList());

        List<ItemDto> itms = new ArrayList<>();
        List<ItemDto> itms2 = new ArrayList<>();
        itms.add(ItemDto.builder().id("ML1").price(120F).build());
        itms.add(ItemDto.builder().id("ML2").price(70F).build());
        itms.add(ItemDto.builder().id("ML3").price(200F).build());

        List<ItemDto> res = this.calculateItemsToBuy(request.getAmount(), itms, itms2, new ArrayList<>());
        log.info("RES = " + Arrays.toString(res.toArray()));

        return null;
    }

    List<ItemDto> calculateItemsToBuy(Float total, List<ItemDto> items, List<ItemDto> tempArr, List<ItemDto> temp) {
        // recorrer la lista de items
        for (int i = 0; i < items.size(); i++) {
            ItemDto item = items.get(i);
            List<ItemDto> itemsMinor = new ArrayList<>(temp);
            // se suma el precio de todos los items de la lista
            Double sumItems = items.stream()
                    .mapToDouble(ItemDto::getPrice)
                    .sum();
            Double sumTempArr = tempArr.stream()
                    .mapToDouble(ItemDto::getPrice)
                    .sum();
            Double sumTemp = temp.stream()
                    .mapToDouble(ItemDto::getPrice)
                    .sum();

            // si la suma de los items es igual a el total, retorna
            // la lista de items que estén
            if (sumItems.floatValue() == total.floatValue()) {
                return items;
            }
            // si la lista de tempArr es igual al total, retorna
            // toda la lista
            if (sumTempArr.floatValue() == total.floatValue()) {
                return tempArr;
            }

            // si alguno de los items tiene el mismo precio del total, retorna
            if (item.getPrice().floatValue() == total.floatValue()) {
                ///////////////////////////////////////
                // se limpia el arreglo
                items.clear();
                // se agrega el item actual
                items.add(item);
                // retorna la lista con el item agregado
                return items;
            }

            if (item.getPrice().floatValue() > sumTemp.floatValue() && item.getPrice().floatValue() <= total.floatValue()) {
                itemsMinor.clear();
                itemsMinor.add(item);

            }

            // poner cuidado a esta validación
            // mejorarlas
            if (sumItems.floatValue() > sumTempArr.floatValue() && sumItems.floatValue() < total.floatValue()) {
                itemsMinor.clear();
                itemsMinor.addAll(items);
            } else if (sumTempArr.floatValue() < total.floatValue() && sumTempArr.floatValue() > sumTemp.floatValue()) {
                itemsMinor.clear();
                itemsMinor.addAll(tempArr);
            }

            if (sumTempArr.floatValue() > total.floatValue() && sumTemp.floatValue() < total.floatValue() && sumTemp.floatValue() > 0) {
                return temp;
            }

            // valida siempre que la suma de los precios de items
            // sea diferente al total o monto ingresado
            if (sumItems.floatValue() != total.floatValue() && sumTempArr.floatValue() != total.floatValue()) {
                // se elimina el item actual de la lista de items
                // para iterar con el siguiente item en la lista
                items.remove(item);
                // se agrega el item en la lista de tempArr para
                // ir suman y comparar con el total
                tempArr.add(item);
                // se llama de nuevo el método, utilizando recursividad
                return calculateItemsToBuy(total, items, tempArr, itemsMinor);
            }
        }
        return items;
    }

}
