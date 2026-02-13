package kr.co.breadfeetserver.infra.persistence.s3.property;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws.s3")
@Slf4j
public class S3Properties {

    private String bucketName;
    private String region;
    private String baseUrl;

    @PostConstruct
    public void init() {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = String.format("https://%s.s3.%s.amazonaws.com", bucketName, region);
        }
        log.info("S3 Properties Loaded. bucketName: {}, baseUrl: {}", bucketName, baseUrl);
    }
}