package com.epam.alxkor.quotes.service;

import com.epam.alxkor.quotes.QuoteBuilder;
import com.epam.alxkor.quotes.TestConstants;
import com.epam.alxkor.quotes.model.entities.Elvl;
import com.epam.alxkor.quotes.model.entities.Quote;
import com.epam.alxkor.quotes.model.ElvlForResponse;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class QuoteServiceImplementationHelperTest {

    QuoteServiceImplementationHelper underTest = new QuoteServiceImplementationHelper();

    @Test
    public void testConvertElvl_whenMethodIsCalled_thenElvlForResponseReturned() {
        Elvl elvl = new Elvl();
        elvl.setIsin(TestConstants.CORRECT_ISIN_VALUE1);
        elvl.setElvl(new BigDecimal("123.00"));

        ElvlForResponse expected = new ElvlForResponse();
        expected.setIsin(TestConstants.CORRECT_ISIN_VALUE1);
        expected.setElvl("123");

        ElvlForResponse actual = underTest.convertElvl(elvl);

        assertEquals(expected, actual);
    }

    @Test
    public void testConvertListOfElvl_whenMethodIsCalled_thenListOfElvlForResponseReturned() {
        Elvl elvl1 = new Elvl();
        elvl1.setIsin(TestConstants.CORRECT_ISIN_VALUE1);
        elvl1.setElvl(new BigDecimal("123.00"));

        Elvl elvl2 = new Elvl();
        elvl2.setIsin(TestConstants.CORRECT_ISIN_VALUE2);
        elvl2.setElvl(new BigDecimal("12.00"));

        List<Elvl> elvls = Arrays.asList(elvl1, elvl2);

        List<ElvlForResponse> actual = underTest.convertListOfElvl(elvls);

        assertEquals(elvls.size(), actual.size());
    }

    @Test
    public void testPrepareElvlForUpsertl_whenFoundElvlIsNullAndBidIsNull_thenElvlIsAsk() {
        Quote quote = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk("123").build();

        Elvl actual = underTest.prepareElvlForUpsert(quote, null);

        assertNull(quote.getBid());
        assertEquals(quote.getIsin(), actual.getIsin());
        assertEquals(quote.getAsk(), actual.getElvl());
    }

    @Test
    public void testPrepareElvlForUpsertl_whenFoundElvlIsNullAndBidIsNotNull_thenElvlIsBid() {
        Quote quote = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk("123").setBid("122").build();

        Elvl actual = underTest.prepareElvlForUpsert(quote, null);

        assertNotNull(quote.getBid());
        assertEquals(quote.getIsin(), actual.getIsin());
        assertEquals(quote.getBid(), actual.getElvl());
    }

    @Test
    public void testPrepareElvlForUpsertl_whenFoundElvlIsNotNullAndBidIsNull_thenElvlIsAsk() {
        Quote quote = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setAsk("123").build();
        Elvl found = new Elvl();
        found.setIsin(TestConstants.CORRECT_ISIN_VALUE1);
        found.setElvl(new BigDecimal("12.00"));

        Elvl actual = underTest.prepareElvlForUpsert(quote, found);

        assertNull(quote.getBid());
        assertEquals(quote.getIsin(), actual.getIsin());
        assertEquals(quote.getAsk(), actual.getElvl());
    }

    @Test
    public void testPrepareElvlForUpsertl_whenFoundElvlIsNotNullAndBidIsGreaterThanElvl_thenElvlIsBid() {
        Quote quote = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setBid("122").setAsk("123").build();
        Elvl found = new Elvl();
        found.setIsin(TestConstants.CORRECT_ISIN_VALUE1);
        found.setElvl(new BigDecimal("120.00"));

        Elvl actual = underTest.prepareElvlForUpsert(quote, found);

        assertEquals(quote.getIsin(), actual.getIsin());
        assertEquals(quote.getBid(), actual.getElvl());
    }

    @Test
    public void testPrepareElvlForUpsertl_whenFoundElvlIsNotNullAndAskIsLessThanElvl_thenElvlIsAsk() {
        Quote quote = new QuoteBuilder().setIsin(TestConstants.CORRECT_ISIN_VALUE1).setBid("102").setAsk("113").build();
        Elvl found = new Elvl();
        found.setIsin(TestConstants.CORRECT_ISIN_VALUE1);
        found.setElvl(new BigDecimal("120.00"));

        Elvl actual = underTest.prepareElvlForUpsert(quote, found);

        assertEquals(quote.getIsin(), actual.getIsin());
        assertEquals(quote.getAsk(), actual.getElvl());
    }

}
