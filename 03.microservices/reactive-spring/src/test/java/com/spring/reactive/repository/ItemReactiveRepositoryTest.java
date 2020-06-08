package com.spring.reactive.repository;

import com.spring.reactive.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoActionOperation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> items = List.of(new Item("123", "Samsung TV", 200.0),
            new Item(null, "Samsung Mobile", 150.0),
            new Item(null, "Beats", 200.0));

    @BeforeEach
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemReactiveRepository::save)
                .doOnNext((item -> {
                    System.out.println("Inserted Item is:" + item);
                })).blockLast();// this makes the actual test case to wait until the current code completes.
    }

    @Test
    public void getAllItemsTest() {
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(3l)
                .verifyComplete();
    }

    @Test
    public void getItemByIdTest() {
        StepVerifier.create(itemReactiveRepository.findById("123"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Samsung TV"))
                .verifyComplete();
    }

    @Test
    public void getItemByDescriptionTest() {
        StepVerifier.create(itemReactiveRepository.findByDescription("Samsung TV"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Samsung TV"))
                .verifyComplete();
    }


    @Test
    public void saveItemTest() {
        Item item =  new Item(null, "Chrome cast", 30.0);
        StepVerifier.create(itemReactiveRepository.save(item))
                .expectSubscription()
                .expectNextMatches(item1 -> item1.getId()!=null && item1.getDescription().equals(item.getDescription()))
                .verifyComplete();
    }

    @Test
    public void updateItem(){
        double newPrice= 100.0;
       Flux<Item> updatedItem = itemReactiveRepository.findByDescription("Samsung TV")
                .map(item ->{
                    item.setPrice(newPrice);
                    return item;
                })
                .flatMap(item -> itemReactiveRepository.save(item));

        StepVerifier.create(updatedItem)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 100.0 )
                .verifyComplete();
    }


    @Test
    public void deleteItemByIdTest() {

        Flux<Object> deletedItem = itemReactiveRepository.findByDescription("Samsung TV").flatMap(item -> {
            return itemReactiveRepository.deleteById(item.getId());
        });
        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();
    }

}
