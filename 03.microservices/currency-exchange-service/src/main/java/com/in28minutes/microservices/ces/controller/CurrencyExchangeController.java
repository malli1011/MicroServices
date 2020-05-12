package com.in28minutes.microservices.ces.controller;

import com.in28minutes.microservices.ces.model.ExchangeValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController("/")
public class CurrencyExchangeController {

    @GetMapping("from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from,@PathVariable String to){
        return new ExchangeValue(1l,"USD","INR", BigDecimal.valueOf(65));
    }
}
