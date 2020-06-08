package com.spring.reactive.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Supplier;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTest {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Spring Boot Reactive")
                .concatWithValues("Spring Boot New")
                // .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();

        stringFlux.subscribe(System.out::println,
                e -> System.err.println(e),
                () -> System.out.println("On complete call"));

    }

    @Test
    public void fluxTestVerify() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Spring Boot Reactive")
                .concatWithValues("Spring Boot New")
                // .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Spring Boot Reactive")
                .expectNext("Spring Boot New")
                .verifyComplete();// call this to invoke the flow of stream elements

        StepVerifier.create(stringFlux).expectNextCount(4).verifyComplete();

    }

    @Test
    public void fluxTestVerify_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Spring Boot Reactive")
                .concatWithValues("Spring Boot New")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Spring Boot Reactive", "Spring Boot New")
                //.expectError(RuntimeException.class)
                .expectErrorMessage("Exception Occurred")
                .verify();// to invoke the stream

    }

    @Test
    public void monoTest() {
        Mono<String> monoString = Mono.just("Spring").log();
        StepVerifier.create(monoString).expectNext("Spring").verifyComplete();
        StepVerifier.create(Mono.error(new RuntimeException("Mono Exception"))).expectError().verify();
    }

    @Test
    public void FluxUsingIterator(){
        Flux<String> stringFlux= Flux.fromIterable(List.of("Malli","Arjun","Sakthi"));

        StepVerifier.create(stringFlux)
                .expectNext("Malli","Arjun","Sakthi")
                .verifyComplete();
    }

    @Test
    public void fluxFromRange(){
        Flux<Integer> intFlux= Flux.range(1,10).log();
        StepVerifier.create(intFlux).expectNext(1,2,3,4,5,6,7,8,9,10).verifyComplete();
    }

    @Test
    public void monoTestWithEmpty(){
        Mono<String> empty= Mono.justOrEmpty(null);
        StepVerifier.create(empty).verifyComplete();

        Supplier<String> stringSupplier = ()-> "Malli";
        //we can get the value of a supplier by calling its get method.
        System.out.println(stringSupplier.get());
        //when we pass the supplier to mono, it will take care of calling the supplier's get method
        Mono<String> mono= Mono.fromSupplier(stringSupplier);

        StepVerifier.create(mono).expectNext("Malli").verifyComplete();


    }

    @Test
    public void transformWithFlatMap(){
        Flux<String> stringFlux= Flux.fromIterable(List.of("A","B","C","D"))
                .flatMap(e-> Flux.fromIterable(convertToList(e)))
                .log();
        StepVerifier.create(stringFlux).expectNextCount(8).verifyComplete();
    }


    @Test
    public void transformWithFlatMap_withParallel(){
        Flux<String> stringFlux= Flux.fromIterable(List.of("A","B","C","D","E","F"))
                .window(2)
                .flatMap(e -> e.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(s -> Flux.fromIterable(s))
                .log();
        StepVerifier.create(stringFlux).expectNextCount(12).verifyComplete();
    }

    private List<String> convertToList(String e){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        return List.of(e, e+e);
    }
}

