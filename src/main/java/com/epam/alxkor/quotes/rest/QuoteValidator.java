package com.epam.alxkor.quotes.rest;

import com.epam.alxkor.quotes.exceptions.QuotesValidationException;
import com.epam.alxkor.quotes.model.entities.Quote;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class QuoteValidator {

    void validateIsin(String isin) {
        if (isin == null || isin.length() != 12) {
            throw new QuotesValidationException();
        }
    }

    void validateQuote(Quote quote) {
        if (quote == null) {
            throw new QuotesValidationException();
        }
        validateIsin(quote.getIsin());
        BigDecimal ask = quote.getAsk();
        BigDecimal bid = quote.getBid();

        if (ask == null || (bid != null && ask.compareTo(bid) < 0)) {
            throw new QuotesValidationException();
        }
    }

}
