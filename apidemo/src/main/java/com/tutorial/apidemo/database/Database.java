package com.tutorial.apidemo.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);

//    @Bean
//    CommandLineRunner initDatabase(ProductRepository productRepository) {
//        return new CommandLineRunner() {
//            @Override
//            public void run(String... args) throws Exception {
////                Product pro1 = new Product("Macbook Pro 16", 2020, 2400.0, "");
////                Product pro2 = new Product("iPad Air Green", 2021, 599.0, "");
////                logger.info("Insert Data: " + productRepository.save(pro1));
////                logger.info("Insert Data: " + productRepository.save(pro2));
//            }
//
//            ;
//        };
//    }
}
