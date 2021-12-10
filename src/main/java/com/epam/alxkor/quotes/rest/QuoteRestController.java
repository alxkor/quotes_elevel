package com.epam.alxkor.quotes.rest;

import com.epam.alxkor.quotes.model.entities.Quote;
import com.epam.alxkor.quotes.service.QuoteService;
import com.epam.alxkor.quotes.model.ElvlForResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes/")
public class QuoteRestController {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteValidator validator;

    @RequestMapping(value = "{isin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ElvlForResponse> getElvlByIsin(@PathVariable("isin") String isin) {
        this.validator.validateIsin(isin);
        ElvlForResponse elvl = quoteService.getElvlByIsin(isin);

        if (elvl == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(elvl, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ElvlForResponse> handleQuote(@RequestBody @Valid Quote quote) {
        this.validator.validateQuote(quote);
        ElvlForResponse elvl = quoteService.handleNewQuote(quote);

        return new ResponseEntity<>(elvl, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ElvlForResponse>> getAllElvl() {
        List<ElvlForResponse> elvls = quoteService.getAllElvls();

        return new ResponseEntity<>(elvls, HttpStatus.OK);
    }

}
