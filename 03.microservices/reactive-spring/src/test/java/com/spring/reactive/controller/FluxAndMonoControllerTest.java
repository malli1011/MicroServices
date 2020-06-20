package com.spring.reactive.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@WebFluxTest // this will scan for controllers only
@AutoConfigureWebTestClient
public class FluxAndMonoControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxApproach1() {
        Flux<Integer> integerFlux = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk().
                        returnResult(Integer.class).getResponseBody();
        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void fluxApproach2() {
        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(4);


    }

    @Test
    public void fluxApproach3() {
        List<Integer> expectedList = List.of(1, 2, 3, 4);
        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectedList, listEntityExchangeResult.getResponseBody());
    }

    @Test
    public void fluxApproach4() {
        List<Integer> expectedList = List.of(1, 2, 3, 4);
        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .consumeWith(res -> {
                    assertEquals(expectedList, res.getResponseBody());
                });

        //assertEquals(expectedList, listEntityExchangeResult.getResponseBody());
    }

    @Test
    public void fluxStreamTest() {
        Flux<Long> longStreamFlux = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();
        StepVerifier.create(longStreamFlux)
                .expectSubscription()
                .expectNext(1l)
                .expectNext(2l)
                .expectNext(3l)
                .expectNext(4l)
                .thenCancel()
                .verify();
    }

    @Test
    public void mono(){
        webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith( response -> {
                    assertEquals(1, response.getResponseBody());
                });
    }
}
