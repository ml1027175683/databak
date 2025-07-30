package com.h9dddd.datacenter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Data
@Configuration
@ConfigurationProperties(prefix = "bakdata.mongodb")
public class MongoDbCollectionsConfig {
    ArrayList<String> day1collections;
    ArrayList<String> hour1collections;
    ArrayList<String> min1collections;
    ArrayList<String> min5collections;
}