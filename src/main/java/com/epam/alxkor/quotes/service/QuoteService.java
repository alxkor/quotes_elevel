package com.epam.alxkor.quotes.service;

import com.epam.alxkor.quotes.model.entities.Quote;
import com.epam.alxkor.quotes.model.ElvlForResponse;

import java.util.List;

public interface QuoteService {

    ElvlForResponse getElvlByIsin(String isin);

    ElvlForResponse handleNewQuote(Quote quote);

    List<ElvlForResponse> getAllElvls();

}
