package com.nhnacademy.orderpaymentrefund.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class LogAndCrashAppender extends AppenderBase<ILoggingEvent> {
    private static final String URL = "https://api-logncrash.cloud.toast.com/v2/log";
    private RestTemplate restTemplate;
    @Setter
    private String appKey;
    @Setter
    private String platform;

    @Override
    public void start() {
        if (appKey == null || platform == null) {
            addError("AppKey and Platform must be set for LogAndCrashAppender");
            return;
        }
        restTemplate = new RestTemplate();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        String logMessage = eventObject.getFormattedMessage();
        if (eventObject.getThrowableProxy() != null) {
            logMessage += "\n" + eventObject.getThrowableProxy().getClassName() + ": "
                    + eventObject.getThrowableProxy().getMessage();
        }
        sendLog(logMessage, eventObject.getLevel());
    }

    private void sendLog(String message, Level level) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("projectName", appKey);
        logData.put("projectVersion", "1.0.0");
        logData.put("logVersion", "v2");
        logData.put("body", message);
        logData.put("logSource", "http");
        logData.put("logType", "log");
        logData.put("Platform", platform);
        logData.put("logLevel", level.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(logData, headers);
        try {
            String response = restTemplate.postForObject(URL, request, String.class);
            log.info("Log sent successfully. Response: {}", response);
        } catch (Exception e) {
            log.error("Failed to send log: {}", e.getMessage());
        }
    }
}