package com.nhnacademy.orderpaymentrefund.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KeyManagerConfig {
    private final RestTemplate restTemplate;

    @Value("${key-manager.api.key}")
    private String apiKey;

    @Value("${user.access.key.id}")
    private String accessKeyId;

    @Value("${secret.access.key}")
    private String accessKeySecret;

    @Value("${secret.key.rabbitmq.username}")
    private String rabbitmqUsernameKey;

    @Value("${secret.key.rabbitmq.host}")
    private String rabbitmqHostKey;

    @Value("${secret.key.rabbitmq.password}")
    private String rabbitmqPasswordKey;

    @Value("${secret.key.rabbitmq.port}")
    private String rabbitmqPortKey;

    @Value("${secret.key.mysql.url}")
    private String mysqlUrlKey;

    @Value("${secret.key.mysql.username}")
    private String mysqlUsernameKey;

    @Value("${secret.key.mysql.password}")
    private String mysqlPasswordKey;

    @Value("${secret.key.redis.db}")
    private String redisDbKey;

    @Value("${secret.key.redis.host}")
    private String redisHostKey;

    @Value("${secret.key.redis.port}")
    private String redisPortKey;

    @Value("${secret.key.redis.password}")
    private String redisPasswordKey;

    private static final String BASE_URL = "https://api-keymanager.nhncloudservice.com/keymanager/v1.2/appkey/";

    @Bean
    public String rabbitHost() {
        String rabbitmqHost = getKey(getSecret(rabbitmqHostKey));
        log.info("Rabbitmq Host Key: {}", rabbitmqHost);
        return rabbitmqHost;
    }

    @Bean
    public String rabbitPassword() {
        String rabbitmqPassword = getKey(getSecret(rabbitmqPasswordKey));
        log.info("Rabbitmq Password Key: {}", rabbitmqPassword);
        return rabbitmqPassword;
    }

    @Bean
    public String rabbitUsername() {
        String rabbitmqUsername = getKey(getSecret(rabbitmqUsernameKey));
        log.info("Rabbitmq Username: {}", rabbitmqUsername);
        return rabbitmqUsername;
    }

    @Bean
    public Integer rabbitPort() {
        String rabbitmqPort = getKey(getSecret(rabbitmqPortKey));
        log.info("Rabbitmq Port Key: {}", rabbitmqPort);
        return Integer.parseInt(rabbitmqPort);
    }

    @Bean
    public String mysqlUrl() {
        String mysqlUrl = getKey(getSecret(mysqlUrlKey));
        log.info("Mysql Url Key: {}", mysqlUrl);
        return mysqlUrl;
    }

    @Bean
    public String mysqlPassword() {
        String mysqlPassword = getKey(getSecret(mysqlPasswordKey));
        log.info("Mysql Password Key: {}", mysqlPassword);
        return mysqlPassword;
    }

    @Bean
    public String mysqlUsername() {
        String mysqlUsername = getKey(getSecret(mysqlUsernameKey));
        log.info("Mysql Username: {}", mysqlUsername);
        return mysqlUsername;
    }

    @Bean
    public String redisHost() {
        String redisHost = getKey(getSecret(redisHostKey));
        log.info("Redis Host Key: {}", redisHost);
        return redisHost;
    }

    @Bean
    public String redisPassword() {
        String redisPassword = getKey(getSecret(redisPasswordKey));
        log.info("Redis Password Key: {}", redisPassword);
        return redisPassword;
    }

    @Bean
    public Integer redisPort() {
        String redisPort = getKey(getSecret(redisPortKey));
        log.info("Redis Port Key: {}", redisPort);
        return Integer.parseInt(redisPort);
    }

    @Bean
    public Integer redisDb() {
        String redisDb = getKey(getSecret(redisDbKey));
        log.info("Redis Db Key: {}", redisDb);
        return Integer.parseInt(redisDb);
    }


    private String getKey(String jsonResponse) {
        try {
            Map<String, Object> responseMap = new ObjectMapper().readValue(jsonResponse, Map.class);
            Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
            return (String) bodyMap.get("secret");
        } catch (Exception e) {
            log.error("Error parsing JSON response", e);
            return null;
        }
    }

    private String getSecret(String secretKey) {
        String url = BASE_URL + apiKey + "/secrets/" + secretKey;
        HttpHeaders headers = getAccessHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }

    private HttpHeaders getAccessHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TC-AUTHENTICATION-ID", accessKeyId);
        headers.add("X-TC-AUTHENTICATION-SECRET", accessKeySecret);
        return headers;
    }

}
