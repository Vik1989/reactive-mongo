package com.reactive.mongo.reactivemongo.repo;

import com.reactive.mongo.reactivemongo.model.Quotes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MongoRepository extends PagingAndSortingRepository<Quotes,String> {

    List<Quotes> findAllByIdNotNullOrderByIdAsc(final Pageable page);
}
