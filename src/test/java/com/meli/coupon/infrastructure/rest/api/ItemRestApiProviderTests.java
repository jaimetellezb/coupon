package com.meli.coupon.infrastructure.rest.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.meli.coupon.CouponApplication;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = CouponApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"api.items.url=http://localhost:8080/items"})
public class ItemRestApiProviderTests {

    @InjectMocks
    private ItemRestApiProvider itemRestApiProvider;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Environment environment;


    @Test
    void getItemPriceOk() {
        ResponseEntity<String> response = this.testRestTemplate.postForEntity("/items/", "ML2", String.class);
        itemRestApiProvider.getItemPrice("ML2");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
