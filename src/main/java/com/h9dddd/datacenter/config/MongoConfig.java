package com.h9dddd.datacenter.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.source.uri}")
    private String sourceUri;

    @Value("${spring.data.mongodb.target.uri}")
    private String targetUri;

    @Bean(name = "sourceMongoClient")
    public MongoClient sourceMongoClient() {
        return MongoClients.create(sourceUri);
    }

    @Bean(name = "sourceMongoTemplate")
    public MongoTemplate sourceMongoTemplate() {
        return new MongoTemplate(sourceMongoClient(), "shipai");
    }

    @Bean(name = "targetMongoClient")
    public MongoClient targetMongoClient() {
        return MongoClients.create(targetUri);
    }

    @Bean(name = "targetMongoTemplate")
    public MongoTemplate targetMongoTemplate() {
        return new MongoTemplate(targetMongoClient(), "shipai");
    }
}