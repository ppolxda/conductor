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

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

@ConfigurationProperties("conductor.external-payload-storage.s3")
public class S3Properties {

    /** The s3 bucket name where the payloads will be stored */
    private String bucketName = "conductor_payloads";

    /** The time (in seconds) for which the signed url will be valid */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration signedUrlExpirationDuration = Duration.ofSeconds(5);

    /** The AWS region of the s3 bucket */
    private String region = "us-east-1";

    /** The AWS region of the s3 endpoint */
    private String endpoint = "";

    /** The AWS region of the s3 access key */
    private String accessKey = "";

    /** The AWS region of the s3 secret key */
    private String secretKey = "";

    /** The AWS region of the s3 signerType */
    private String signerType = "";

    /** The AWS region of the s3 pathStyleAccessEnabled */
    private boolean pathStyleAccessEnabled = false;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Duration getSignedUrlExpirationDuration() {
        return signedUrlExpirationDuration;
    }

    public void setSignedUrlExpirationDuration(Duration signedUrlExpirationDuration) {
        this.signedUrlExpirationDuration = signedUrlExpirationDuration;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSignerType() {
        return signerType;
    }

    public void setSignerType(String signerType) {
        this.signerType = signerType;
    }

    public Boolean getPathStyleAccessEnabled() {
        return pathStyleAccessEnabled;
    }

    public void setPathStyleAccessEnabled(Boolean pathStyleAccessEnabled) {
        this.pathStyleAccessEnabled = pathStyleAccessEnabled;
    }
}
