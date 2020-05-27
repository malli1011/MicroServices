package com.spring.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoErrorHandling {

    @Test
    public void fluxTestWithError() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWithValues("D")
                .onErrorResume((e) -> {
                    System.out.println("e = " + e);
                    return Flux.just("Default Value");
                });

        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A", "B", "C")
                //.expectError(RuntimeException.class)
                .expectNext("Default Value")
                .verifyComplete();
    }

    @Test
    public void fluxTestWithErrorMap() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWithValues("D")
                .onErrorMap(e-> new CustomException(e))
                .retry(2);


        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A", "B", "C")// actual call
                .expectNext("A", "B", "C")//1st retry
                .expectNext("A", "B", "C")//2nd retry
                .expectError(CustomException.class)
                .verify();
    }
}
