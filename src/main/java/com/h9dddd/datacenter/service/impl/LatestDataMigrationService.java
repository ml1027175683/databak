package com.h9dddd.datacenter.service.impl;

import com.h9dddd.datacenter.config.MongoDbCollectionsConfig;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;

@Service
@RequiredArgsConstructor
public class LatestDataMigrationService {
    @Resource
    private final MongoTemplate sourceMongoTemplate;
    @Resource
    private final MongoTemplate targetMongoTemplate;
    

    @Resource
    MongoDbCollectionsConfig collectionsConfig;

    @Async
    @Scheduled(cron = "0 0/1 * * * ?")
    public void oneMinCreate() {
        for (String min1collection : collectionsConfig.getMin1collections()) {
            dealData(min1collection);
        }
    }


    @Async
    @Scheduled(cron = "0 0/5 * * * ?")
    public void fiveMinCreate() {
        for (String min5collection : collectionsConfig.getMin5collections()) {
            dealData(min5collection);
        }
    }

    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void  oneHourCreate() {
        for (String hour1collection : collectionsConfig.getHour1collections()) {
            dealData(hour1collection);
        }
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void oneDayCreate() {
        for (String day1collection : collectionsConfig.getDay1collections()) {
            dealData(day1collection);
        }
    }

    private void dealData(String collectionName) {
        Map latestRecord = getLatestRecord(collectionName, sourceMongoTemplate);
        targetMongoTemplate.insert(latestRecord, collectionName);
        createCollection(collectionName, 11000000000L, -1);
    }

    public Map getLatestRecord(String collectionName, MongoTemplate mongoTemplate) {
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(1);
       // query.fields().exclude("_id");
        Map one = mongoTemplate.findOne(query, Map.class, collectionName);
        return one;
    }

    private void createCollection(String collectionName, long size, long max) {
        if (!targetMongoTemplate.collectionExists(collectionName)) {
            targetMongoTemplate.createCollection(collectionName, CollectionOptions.empty().capped().maxDocuments(max).size(size));
            IndexOptions indexOptions = new IndexOptions();
            indexOptions.unique(true);
            targetMongoTemplate.getCollection(collectionName).createIndex(Indexes.descending("timestamp"), indexOptions);
        }
    }


}