package com.spring.reactive.controller.v1;

import com.mongodb.MongoNodeIsRecoveringException;
import com.spring.reactive.document.Item;
import com.spring.reactive.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.spring.reactive.Constants.ITEMS_END_POINT;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEMS_END_POINT)
    public Flux<Item> getAllItems(){
        return itemReactiveRepository.findAll();
    }

    @GetMapping(ITEMS_END_POINT+"/{id}")
    public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id){
        return  itemReactiveRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>( HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEMS_END_POINT)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item){
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping(ITEMS_END_POINT+"/{id}")
    public Mono<Void> deleteItem(@PathVariable String id){
        return itemReactiveRepository.deleteById(id);
    }

    @PutMapping(ITEMS_END_POINT+"/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item item){
        return itemReactiveRepository.findById(id)
                .flatMap(oldItem ->{
                    oldItem.setPrice(item.getPrice());
                    oldItem.setDescription(item.getDescription());
                    return itemReactiveRepository.save(oldItem);
                })
                .map(updateItem -> new ResponseEntity<Item>(updateItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
    @GetMapping(ITEMS_END_POINT+"/runtimeException")
    public Flux<Item> runtimeException(){
        return itemReactiveRepository.findAll()
                .concatWith(Mono.error(new RuntimeException("Runtime Exception Occurred.")));
    }


}
