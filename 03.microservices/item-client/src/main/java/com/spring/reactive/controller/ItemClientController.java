package com.spring.reactive.controller;

import com.spring.reactive.domain.Item;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
public class ItemClientController {
    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("/client/retrieve")
    //Retrieve method will give access to the response body
    public Flux<Item> getAllItemsUsingRetrieve() {
        return webClient.get().uri("/v1/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items in Client Project retrieve :");
    }

    @GetMapping("/client/exchange")
    //Exchange method will give ClientResponse object, which will have status along with response body
    public Flux<Item> getAllItemsUsingExchange() {
        return webClient.get().uri("/v1/items")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("Items in Client Project exchange: ");
    }
}
