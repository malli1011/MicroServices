package com.spring.reactive.repository;

import com.spring.reactive.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
    public Flux<Item> findByDescription(String description);
}
