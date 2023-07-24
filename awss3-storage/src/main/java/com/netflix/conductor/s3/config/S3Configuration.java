/*
 * Copyright 2022 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.s3.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.utils.IDGenerator;
import com.netflix.conductor.s3.storage.S3PayloadStorage;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

@Configuration
@EnableConfigurationProperties(S3Properties.class)
@ConditionalOnProperty(name = "conductor.external-payload-storage.type", havingValue = "s3")
public class S3Configuration {

    @Bean
    public ExternalPayloadStorage s3ExternalPayloadStorage(
            IDGenerator idGenerator, S3Properties properties, AmazonS3 s3Client) {
        return new S3PayloadStorage(idGenerator, properties, s3Client);
    }

    @ConditionalOnProperty(
            name = "conductor.external-payload-storage.s3.use_default_client",
            havingValue = "true",
            matchIfMissing = true)
    @Bean
    public AmazonS3 amazonS3(S3Properties properties) {
        String region = properties.getRegion();
        String accessKey = properties.getAccessKey();
        String secretKey = properties.getSecretKey();
        String endpoint = properties.getEndpoint();
        String signerType = properties.getSignerType();
        Boolean pathStyle = properties.getPathStyleAccessEnabled();
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();

        if (accessKey != null
                && !accessKey.isEmpty()
                && secretKey != null
                && !secretKey.isEmpty()) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            builder = builder.withCredentials(new AWSStaticCredentialsProvider(credentials));
        }

        if (endpoint != null && !endpoint.isEmpty()) {
            builder =
                    builder.withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(endpoint, region));
        } else if (region != null && !region.isEmpty()) {
            builder = builder.withRegion(region);
        }

        if (signerType != null && !signerType.isEmpty()) {
            ClientConfiguration clientConfig = new ClientConfiguration();
            clientConfig.setSignerOverride(signerType);
            builder = builder.withClientConfiguration(clientConfig);
        }

        if (pathStyle) {
            builder = builder.withPathStyleAccessEnabled(true);
        }
        return builder.build();
    }
}
