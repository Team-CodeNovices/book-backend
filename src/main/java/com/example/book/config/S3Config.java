package com.example.book.config;

import com.amazonaws.auth.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    String asccesskey;
    @Value("${cloud.aws.credentials.secret-key}")
    String secretkey;
    @Value("${cloud.aws.s3.bucketname}")
    String bucketname;
    @Value("${cloud.aws.region.static}")
    String region;

    @Bean
    public AmazonS3 S3Builder() {
        AWSCredentials basicAWSCredentials = new BasicAWSCredentials(asccesskey, secretkey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(region).build();
    }
}
