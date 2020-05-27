package com.in28minutes.microservices.ces.controller;

import com.in28minutes.microservices.ces.model.CurrencyConversionBean;
import com.in28minutes.microservices.ces.model.ExchangeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@RestController("/")
public class CurrencyExchangeController {

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from,@PathVariable String to){
        ExchangeValue value = new ExchangeValue(1l,from,to, BigDecimal.valueOf(65));
        value.setPort(Integer.parseInt(environment.getProperty("local.server.port")));

        return value;
    }
}
