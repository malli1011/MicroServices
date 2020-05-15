package com.in28minutes.microservices.ccs.controller;

import com.in28minutes.microservices.ccs.model.CurrencyConversionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.MacSpi;
import java.math.BigDecimal;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    Environment environment;

    @Autowired
    private CurrencyExchangeServiceProxy exchangeServiceProxy;

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
            ){
        int port = Integer.parseInt(environment.getProperty("local.server.port"));
        Map<String,String> uriVariables = Map.of("from",from,"to",to);

        ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8001/currency-exchange/from/{from}/to/{to}",
                CurrencyConversionBean.class,uriVariables);
        CurrencyConversionBean response = responseEntity.getBody();
        //return new CurrencyConversionBean(1l,from,to,BigDecimal.ONE,quantity,quantity,port);
        return response;
    }

    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ){
          CurrencyConversionBean response = exchangeServiceProxy.retrieveExchangeValue(from,to);

        return response;
    }
}
