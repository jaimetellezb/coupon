package com.meli.coupon.infrastructure.http.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.meli.coupon.CouponApplication;
import com.meli.coupon.application.dto.ItemsToBuyRequest;
import com.meli.coupon.application.service.CouponService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CouponApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class CouponControllerTests {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private CouponService service;


    @BeforeEach
    public void init() {

    }


    @Test
    void itemsToBuyOk() throws Exception {
        List<String> ids = Arrays.asList(
            "ML1",
            "ML2",
            "ML3"
        );

        ItemsToBuyRequest request = new ItemsToBuyRequest(ids, 500F);

        ResponseEntity<String> response = this.testRestTemplate.postForEntity("/coupon/", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).getItemsToBuy(any(ItemsToBuyRequest.class));
    }

    @Test
    void getFavoriteItemsOk() {

        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/coupon/favorites?limit=5", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).getFavoriteItems(any(Integer.class));
    }

    @Test
    void itemsToBuyNotFoundUrl() {

        List<String> ids = Arrays.asList("ML1", "ML2", "ML3");
        ItemsToBuyRequest request = new ItemsToBuyRequest(ids, 500F);
        String baseUrl = "http://localhost:" + randomServerPort + "/coupon2/";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<ItemsToBuyRequest> entity = new HttpEntity<ItemsToBuyRequest>(request, headers);

        ResponseEntity<String> responseService = this.testRestTemplate.exchange(baseUrl, HttpMethod.POST, entity,
            String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseService.getStatusCode());
    }

    @Test
    void itemsToBuyBadRequest() {

        List<String> ids = Arrays.asList("ML1", "ML2");
        ItemsToBuyRequest newBook = new ItemsToBuyRequest(ids, 0F);

        ResponseEntity<String> response = this.testRestTemplate.postForEntity("/coupon/", newBook, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
