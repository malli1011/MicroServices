package com.spring.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTime {
    @Test
    public void infiniteSequence() throws Exception {
       Flux<Long> infinateFlux = Flux.interval(Duration.ofMillis(200)).log();
        infinateFlux.subscribe( System.out::println);
       Thread.sleep(3000l);
    }

    @Test
    public void infiniteSequenceTest() throws Exception {
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .take(2)
                .log();
        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0l,1l).verifyComplete();

    }

    @Test
    public void infiniteSequenceTesWithDelayt() throws Exception {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofMillis(300))
                .map(l -> Integer.valueOf(l.intValue()))
                .take(2)
                .log();
        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0,1).verifyComplete();

    }

}
