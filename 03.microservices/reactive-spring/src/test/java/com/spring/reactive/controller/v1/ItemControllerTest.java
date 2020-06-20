package com.spring.reactive.controller.v1;

import com.spring.reactive.document.Item;
import com.spring.reactive.repository.ItemReactiveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.spring.reactive.Constants.ITEMS_END_POINT;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @BeforeEach
    public void setUp() {
        List<Item> items = List.of(new Item("123", "Samsung TV", 200.0),
                new Item(null, "Samsung Mobile", 150.0),
                new Item(null, "Beats", 200.0));
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(
                        item -> System.out.println("Item :" + item)
                ).blockLast();
    }

    @Test
    public void getAllItemsTest() {
        webTestClient.get().uri(ITEMS_END_POINT)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(3);
    }

    @Test
    public void getAllItemsTest_approach2() {
        webTestClient.get().uri(ITEMS_END_POINT)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(3)
                .consumeWith(res -> {
                    List<Item> items = res.getResponseBody();
                    items.forEach(
                            item -> Assertions.assertTrue(item.getId() != null)
                    );
                });
    }

    @Test
    public void getAllItemsTest_approach3() {
        Flux<Item> itemsFlux = webTestClient.get().uri(ITEMS_END_POINT)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(itemsFlux.log("Value for next call")).expectSubscription()
                .expectNextCount(3l)
                .verifyComplete();
    }

    @Test
    public void getOneItemTest() {
        webTestClient.get().uri(ITEMS_END_POINT.concat("/{id}"), "123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 200.0); // gives access to the json object in response
    }

    @Test
    public void getOneItemTest2() {
        webTestClient.get().uri(ITEMS_END_POINT.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void itemCreateTest() {
        Item item = new Item(null, "Iphone X ", 999.0);
        webTestClient.post().uri(ITEMS_END_POINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isNotEmpty()
                .jsonPath("$.price").isEqualTo(999.00);

    }


    @Test
    public void itemDeleteTest() {
        Item item = new Item(null, "Iphone X ", 999.0);
        webTestClient.delete().uri(ITEMS_END_POINT.concat("/{id}"), "123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

    }

    @Test
    public void updateItemTest() {
        Item item = new Item(null, "Iphone XR", 700.0);
        webTestClient.put().uri(ITEMS_END_POINT.concat("/{id}"), "123")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo(700.00);

    }

    @Test
    public void updateItemTest2() {
        Item item = new Item(null, "Iphone XR", 700.0);
        webTestClient.put().uri(ITEMS_END_POINT.concat("/{id}"), "abc")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound();

    }
}
