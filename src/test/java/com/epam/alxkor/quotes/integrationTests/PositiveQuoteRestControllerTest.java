package com.epam.alxkor.quotes.integrationTests;

import com.epam.alxkor.quotes.QuoteBuilder;
import com.epam.alxkor.quotes.TestConstants;
import com.epam.alxkor.quotes.model.entities.Quote;
import com.epam.alxkor.quotes.service.QuoteServiceImplementation;
import com.epam.alxkor.quotes.model.ElvlForResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PositiveQuoteRestControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private QuoteServiceImplementation service;

    private final String BID_VAlUE1 = "10";
    private final String ASK_VAlUE1 = "11";
    private final String LESSER_BID_VAlUE1 = "1";
    private final String LESSER_ASK_VAlUE1 = "2";
    private final String GREATER_BID_VALUE1 = "21";
    private final String GREATER_ASK_VALUE1 = "22";

    private final String BID_VAlUE2 = "1000";
    private final String ASK_VAlUE2 = "1001";

    @Test
    public void whenAddNewQuoteWithNewIsin_thenNewElvlWithBidValueCreated() {
        ResponseEntity<ElvlForResponse> responseResponseEntity = testRestTemplate.getForEntity(TestConstants.BASE_URL + TestConstants.CORRECT_ISIN_VALUE1, ElvlForResponse.class);
        assertEquals(responseResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

        Quote quote = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(ASK_VAlUE1).setBid(BID_VAlUE1).build();

        ResponseEntity<ElvlForResponse> responseEntity = testRestTemplate.postForEntity(TestConstants.BASE_URL, quote, ElvlForResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(BID_VAlUE1, responseEntity.getBody().getElvl());
        assertEquals(TestConstants.CORRECT_ISIN_VALUE1, responseEntity.getBody().getIsin());

        cleanup(TestConstants.CORRECT_ISIN_VALUE1);
    }


    @Test
    public void whenQuoteHasNoBidValue_thenElvlHasAskValue() {
        Quote quote = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(ASK_VAlUE1).build();

        ResponseEntity<ElvlForResponse> responseEntity = testRestTemplate.postForEntity(TestConstants.BASE_URL, quote, ElvlForResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ASK_VAlUE1, responseEntity.getBody().getElvl());
        assertEquals(TestConstants.CORRECT_ISIN_VALUE1, responseEntity.getBody().getIsin());

        cleanup(TestConstants.CORRECT_ISIN_VALUE1);
    }

    @Test
    public void whenFindByIsin_thenElvlReturned() {
        Quote quote1 = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(ASK_VAlUE1).setBid(BID_VAlUE1).build();
        createElvl(quote1);

        ResponseEntity<ElvlForResponse> responseEntity = testRestTemplate.getForEntity(TestConstants.BASE_URL + TestConstants.CORRECT_ISIN_VALUE1, ElvlForResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(BID_VAlUE1, responseEntity.getBody().getElvl());
        assertEquals(TestConstants.CORRECT_ISIN_VALUE1, responseEntity.getBody().getIsin());

        cleanup(TestConstants.CORRECT_ISIN_VALUE1);
    }

    @Test
    public void whenFindByAllIsin_thenListOfElvlReturned() {
        Quote quote1 = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(ASK_VAlUE1).setBid(BID_VAlUE1).build();
        Quote quote2 = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE2).setAsk(ASK_VAlUE2).setBid(BID_VAlUE2).build();
        createElvl(quote1);
        createElvl(quote2);

        ResponseEntity<ElvlForResponse[]> responseEntity = testRestTemplate.getForEntity(TestConstants.BASE_URL, ElvlForResponse[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().length >= 2);

        cleanup(TestConstants.CORRECT_ISIN_VALUE1);
        cleanup(TestConstants.CORRECT_ISIN_VALUE2);
    }

    @Test
    public void whenBidMoreThanElvl_thenElvlIsBid() {
        Quote quote1 = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(ASK_VAlUE1).setBid(BID_VAlUE1).build();
        createElvl(quote1);

        ResponseEntity<ElvlForResponse> initialResponseEntity = testRestTemplate.getForEntity(TestConstants.BASE_URL + TestConstants.CORRECT_ISIN_VALUE1, ElvlForResponse.class);
        assertNotNull(initialResponseEntity.getBody());
        assertEquals(BID_VAlUE1, initialResponseEntity.getBody().getElvl());

        Quote quote2 = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(GREATER_ASK_VALUE1).setBid(GREATER_BID_VALUE1).build();

        ResponseEntity<ElvlForResponse> responseEntity = testRestTemplate.postForEntity(TestConstants.BASE_URL, quote2, ElvlForResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(GREATER_BID_VALUE1, responseEntity.getBody().getElvl());
        assertEquals(TestConstants.CORRECT_ISIN_VALUE1, responseEntity.getBody().getIsin());

        cleanup(TestConstants.CORRECT_ISIN_VALUE1);
    }

    @Test
    public void whenAscLessThanElvl_thenElvlIsASK() {
        Quote quote1 = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(ASK_VAlUE1).setBid(BID_VAlUE1).build();
        createElvl(quote1);

        ResponseEntity<ElvlForResponse> initialResponseEntity = testRestTemplate.getForEntity(TestConstants.BASE_URL + TestConstants.CORRECT_ISIN_VALUE1, ElvlForResponse.class);
        assertNotNull(initialResponseEntity.getBody());
        assertEquals(BID_VAlUE1, initialResponseEntity.getBody().getElvl());

        Quote quote2 = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(LESSER_ASK_VAlUE1).setBid(LESSER_BID_VAlUE1).build();

        ResponseEntity<ElvlForResponse> responseEntity = testRestTemplate.postForEntity(TestConstants.BASE_URL, quote2, ElvlForResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(LESSER_ASK_VAlUE1, responseEntity.getBody().getElvl());
        assertEquals(TestConstants.CORRECT_ISIN_VALUE1, responseEntity.getBody().getIsin());

        cleanup(TestConstants.CORRECT_ISIN_VALUE1);
    }

    @Test
    public void whenQuoteRunsForExistIsin_thenNewElvlNotCreated() {
        Quote quote1 = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(ASK_VAlUE1).setBid(BID_VAlUE1).build();
        createElvl(quote1);

        ResponseEntity<ElvlForResponse[]> initialResponseEntity = testRestTemplate.getForEntity(TestConstants.BASE_URL, ElvlForResponse[].class);
        assertEquals(HttpStatus.OK, initialResponseEntity.getStatusCode());
        assertNotNull(initialResponseEntity.getBody());
        long initialNumberOfElvlsWithIsin = Arrays.stream(initialResponseEntity.getBody()).filter(el->el.getIsin().equals(TestConstants.CORRECT_ISIN_VALUE1)).count();
        assertEquals(1, initialNumberOfElvlsWithIsin);

        Quote quote2 = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk(ASK_VAlUE2).setBid(BID_VAlUE2).build();
        ResponseEntity<ElvlForResponse> responseEntity = testRestTemplate.postForEntity(TestConstants.BASE_URL, quote2, ElvlForResponse.class);
        assertEquals(HttpStatus.OK, initialResponseEntity.getStatusCode());

        ResponseEntity<ElvlForResponse[]> newResponseEntity = testRestTemplate.getForEntity(TestConstants.BASE_URL, ElvlForResponse[].class);
        assertEquals(HttpStatus.OK, newResponseEntity.getStatusCode());
        assertNotNull(newResponseEntity.getBody());
        long newNumberOfElvlsWithIsin = Arrays.stream(newResponseEntity.getBody()).filter(el->el.getIsin().equals(TestConstants.CORRECT_ISIN_VALUE1)).count();
        assertEquals(initialNumberOfElvlsWithIsin, newNumberOfElvlsWithIsin);

        cleanup(TestConstants.CORRECT_ISIN_VALUE1);
    }

    private void createElvl(Quote quote) {
        ResponseEntity<ElvlForResponse> responseEntity = testRestTemplate.postForEntity(TestConstants.BASE_URL, quote, ElvlForResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    private void cleanup(String isin) {
        service.deleteByIsin(isin);
    }
}
