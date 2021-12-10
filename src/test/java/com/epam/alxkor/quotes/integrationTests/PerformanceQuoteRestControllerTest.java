package com.epam.alxkor.quotes.integrationTests;

import com.epam.alxkor.quotes.QuoteBuilder;
import com.epam.alxkor.quotes.TestConstants;
import com.epam.alxkor.quotes.model.entities.Quote;
import com.epam.alxkor.quotes.service.QuoteServiceImplementation;
import com.epam.alxkor.quotes.model.ElvlForResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerformanceQuoteRestControllerTest {

    private final Logger LOGGER = LoggerFactory.getLogger(PerformanceQuoteRestControllerTest.class);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private QuoteServiceImplementation service;

    private final String ISIN_VALUE_TEMPLATE = "ELVL123";
    List<Quote> quotes;

    @Test
    public void whenHandles100Operations_thenItTakesLessThanSecond() {
        quotes = createListOf1000QuotesWithDifferentIsin();

        List<ResponseEntity<ElvlForResponse>> responseEntityList = new ArrayList<>();
        warmUp();

        long start = System.currentTimeMillis();
        quotes.forEach(quote -> responseEntityList.add(testRestTemplate
                .postForEntity(TestConstants.BASE_URL, quote, ElvlForResponse.class)));
        long end = System.currentTimeMillis();

        long timeFor100Operations = (end - start) / 10;

        LOGGER.info("\n\nTime for 100 operations = " + timeFor100Operations + "ms.\n");

        cleanUp();

        responseEntityList.forEach(re -> assertEquals(HttpStatus.OK, re.getStatusCode()));
    }

    private List<Quote> createListOf1000QuotesWithDifferentIsin() {
        List<Quote> quotes = new ArrayList<>();
        int bid = 2;
        int ask = 3;
        int initNumberOfIsin = 10000;

        for (int i = 0; i < 1000; i++) {
            quotes.add(new QuoteBuilder()
                    .setIsin(ISIN_VALUE_TEMPLATE + (initNumberOfIsin + i))
                    .setAsk(String.valueOf(ask++))
                    .setBid(String.valueOf(bid++)).build());
        }

        return quotes;
    }

    private void warmUp() {
        quotes.forEach(quote -> testRestTemplate.postForEntity(TestConstants.BASE_URL, quote, ElvlForResponse.class));
    }

    private void cleanUp() {
        int initNumberOfIsin = 10000;
        for (int i = 0; i < 1000; i++) {
            service.deleteByIsin(ISIN_VALUE_TEMPLATE + (initNumberOfIsin + i));
        }
    }

}
