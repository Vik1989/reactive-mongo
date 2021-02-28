package com.reactive.mongo.reactivemongo.repo;

import com.reactive.mongo.reactivemongo.model.Quotes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface QuoteMongoReactiveRepository extends ReactiveSortingRepository<Quotes,String> {

    Flux<Quotes> findAllByIdNotNullOrderByIdAsc(final Pageable page);
}
