package com.spring.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackPressureTest {
    // Backpressure: Subscriber controls the data flow from the producer.
    //Subscriber will subscribe for data but request one item at a time until it makes a cancel call.
    //when data is not required anymore.

    @Test
    public void backPressureTest() {
        Flux<Integer> intFlux = Flux.range(1, 10);// generates 10 elements

        StepVerifier.create(intFlux.log()).expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {
        Flux<Integer> intFlux = Flux.range(1, 10);

        intFlux.subscribe(e -> System.out.println("Element: " + e),
                err -> System.err.println("Exception :" + err),
                () -> System.out.println("Done"),
                (subscription -> subscription.request(2)));
    }

    @Test
    public void backPressure_cancel() {
        Flux<Integer> intFlux = Flux.range(1, 10);

        intFlux.subscribe(e -> System.out.println("Element: " + e),
                err -> System.err.println("Exception :" + err),
                () -> System.out.println("Done"),
                subscription -> subscription.cancel());
    }

    @Test
    public void backPressure_customized() {
        Flux<Integer> intFlux = Flux.range(1, 10).log();
        intFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(2);
                System.out.println("value = " + value);
                if (value == 4) {
                    cancel();
                }
            }

        });
    }
}