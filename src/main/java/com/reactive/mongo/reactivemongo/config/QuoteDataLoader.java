package com.reactive.mongo.reactivemongo.config;

import ch.qos.logback.classic.Logger;
import com.reactive.mongo.reactivemongo.model.Quotes;
import com.reactive.mongo.reactivemongo.repo.QuoteMongoReactiveRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Supplier;

@Component
public class QuoteDataLoader implements ApplicationRunner {

    private static final Logger log = (Logger) LoggerFactory.getLogger(QuoteDataLoader.class);

    private final QuoteMongoReactiveRepository quoteMongoReactiveRepository;

    QuoteDataLoader(final QuoteMongoReactiveRepository quoteMongoReactiveRepository) {
        this.quoteMongoReactiveRepository = quoteMongoReactiveRepository;
    }

    @Override
    public void run(final ApplicationArguments args) {
        if (quoteMongoReactiveRepository.count().block() == 0L) {
            var idSupplier = getIdSequenceSupplier();
            var bufferedReader = new BufferedReader(
                    new InputStreamReader(getClass()
                            .getClassLoader()
                            .getResourceAsStream("pg2000.txt"))
            );
            Flux.fromStream(
                    bufferedReader.lines()
                            .filter(l -> !l.trim().isEmpty())
                            .map(l -> quoteMongoReactiveRepository.save(
                                    new Quotes(idSupplier.get(),
                                            "El Quijote", l))
                            )
            ).subscribe(m -> log.info("New quote loaded: {}", m.block()));
            log.info("Repository contains now {} entries.",
                    quoteMongoReactiveRepository.count().block());
        }
    }

    private Supplier<String> getIdSequenceSupplier() {
        return new Supplier<>() {
            Long l = 0L;

            @Override
            public String get() {
                // adds padding zeroes
                return String.format("%05d", l++);
            }
        };
    }
}
