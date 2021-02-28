package com.reactive.mongo.reactivemongo.controller;

import com.reactive.mongo.reactivemongo.model.Quotes;
import com.reactive.mongo.reactivemongo.repo.MongoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteBlockingController {
    private static final int DELAY_PER_ITEM_MS = 100;

    private final MongoRepository quoteMongoBlockingRepository;

    public QuoteBlockingController(final MongoRepository quoteMongoBlockingRepository) {
        this.quoteMongoBlockingRepository = quoteMongoBlockingRepository;
    }

    @GetMapping("/quotes-blocking")
    public Iterable<Quotes> getQuotesBlocking() throws Exception {
        Thread.sleep(DELAY_PER_ITEM_MS * quoteMongoBlockingRepository.count());
        return quoteMongoBlockingRepository.findAll();
    }

    @GetMapping("/quotes-blocking-paged")
    public Iterable<Quotes> getQuotesBlocking(final @RequestParam(name = "page") int page,
                                             final @RequestParam(name = "size") int size) throws Exception {
        Thread.sleep(DELAY_PER_ITEM_MS * size);
        return quoteMongoBlockingRepository.findAll(PageRequest.of(page, size));
    }
}
