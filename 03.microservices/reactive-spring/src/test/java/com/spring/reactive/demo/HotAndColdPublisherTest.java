package com.spring.reactive.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotAndColdPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux= Flux.just("A","B","C","D","E","F","G","H")
                .delayElements(Duration.ofSeconds(1));
                //.log();
        stringFlux.subscribe(s -> System.out.println("Subscriber1 :"+s));
        Thread.sleep(2000l);
        stringFlux.subscribe(s -> System.out.println("Subscriber2 :"+s));
        Thread.sleep(4000l);
    }


    @Test
    public void hotPublisherTest(){
        Flux<String> stringFlux= Flux.just("A","B","C","D","E","F","G","H").delayElements(Duration.ofSeconds(1));
    }
}
