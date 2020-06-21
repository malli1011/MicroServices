package com.spring.reactive.controller;

import com.spring.reactive.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping("/client/getItem")
    //Exchange method will give ClientResponse object, which will have status along with response body
    public Mono<Item> getOneItemUsingExchange() {
        String id = "123";
        return webClient.get().uri("/v1/items/{id}", id)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("Items in Client Project exchange: ");
    }

    @PostMapping("/client/createItem")
    public Mono<Item> createItem(@RequestBody Item item) {
        return webClient.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(item, Item.class)
                .retrieve()
                .bodyToMono(Item.class).log("Created Item is :");
    }

    @PutMapping("/client/updateItem/{id}")
    public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item){
        return webClient.put().uri("/v1/items/{id}",id)
                .body(item,Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Updated Item is: ");
    }

    @DeleteMapping("/client/deleteItem/{id}")
    public Mono<Void> deleteItem(@PathVariable String id){
        return webClient.delete().uri("/v1/deleteItem/{id}",id)
                .retrieve()
                .bodyToMono(Void.class)
                .log("Deleted Item: ");
    }
}
