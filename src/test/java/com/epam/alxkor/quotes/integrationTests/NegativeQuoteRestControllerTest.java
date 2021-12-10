package com.epam.alxkor.quotes.integrationTests;

import com.epam.alxkor.quotes.QuoteBuilder;
import com.epam.alxkor.quotes.TestConstants;
import com.epam.alxkor.quotes.model.entities.Quote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NegativeQuoteRestControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String ISIN_NOT_EXIST = "ISINNOTEXIST";
    private final String BID_VAlUE1 = "10.05";
    private final String ASK_LESS_THAN_BID_VAlUE1 = "0.02";

    private final String MESSAGE = "message";

    private final String ELVL_NOT_FOUND = "No elvls was found";

    @Test
    public void whenIsinHasWrongFormat_thenExceptionIsThrown() {
        ResponseEntity<LinkedHashMap> responseEntity = testRestTemplate.getForEntity(TestConstants.BASE_URL + TestConstants.ISIN_WRONG_VALUE, LinkedHashMap.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().get(MESSAGE), TestConstants.CHECK_THE_PARAMS);
    }

    @Test
    public void whenAskLessThenBid_thenExceptionIsThrown() {
        Quote quote = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(ASK_LESS_THAN_BID_VAlUE1).setBid(BID_VAlUE1).build();

        ResponseEntity<LinkedHashMap> responseEntity = testRestTemplate.postForEntity(TestConstants.BASE_URL, quote, LinkedHashMap.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().get(MESSAGE), TestConstants.CHECK_THE_PARAMS);
    }

    @Test
    public void whenAskIsNotSpecified_thenExceptionIsThrown() {
        Quote quote = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setBid(BID_VAlUE1).build();

        ResponseEntity<LinkedHashMap> responseEntity = testRestTemplate.postForEntity(TestConstants.BASE_URL, quote, LinkedHashMap.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().get(MESSAGE), TestConstants.CHECK_THE_PARAMS);
    }

    @Test
    public void whenIsinIsNotFound_thenExceptionIsThrown() {
        ResponseEntity<LinkedHashMap> responseEntity = testRestTemplate.getForEntity(TestConstants.BASE_URL + ISIN_NOT_EXIST, LinkedHashMap.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().get(MESSAGE), ELVL_NOT_FOUND);
    }

}
