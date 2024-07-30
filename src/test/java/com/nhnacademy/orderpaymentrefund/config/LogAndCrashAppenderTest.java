package com.nhnacademy.orderpaymentrefund.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

class LogAndCrashAppenderTest {

    @InjectMocks
    private LogAndCrashAppender appender;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appender.setAppKey("testAppKey");
        appender.setPlatform("testPlatform");
        ReflectionTestUtils.setField(appender, "restTemplate", restTemplate);
    }

    @Test
    void testAppend() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        when(event.getFormattedMessage()).thenReturn("Test log message");
        when(event.getLevel()).thenReturn(Level.INFO);

        when(restTemplate.postForObject(
            eq("https://api-logncrash.cloud.toast.com/v2/log"),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn("Success");

        appender.append(event);

        verify(restTemplate, times(1)).postForObject(
            eq("https://api-logncrash.cloud.toast.com/v2/log"),
            any(HttpEntity.class),
            eq(String.class)
        );
    }

    @Test
    void testAppendWithException() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        when(event.getFormattedMessage()).thenReturn("Test log message");
        when(event.getLevel()).thenReturn(Level.ERROR);

        ThrowableProxy throwableProxy = mock(ThrowableProxy.class);
        when(throwableProxy.getClassName()).thenReturn("TestException");
        when(throwableProxy.getMessage()).thenReturn("Test exception message");
        when(event.getThrowableProxy()).thenReturn(throwableProxy);

        when(restTemplate.postForObject(
            eq("https://api-logncrash.cloud.toast.com/v2/log"),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn("Success");

        appender.append(event);

        verify(restTemplate, times(1)).postForObject(
            eq("https://api-logncrash.cloud.toast.com/v2/log"),
            any(HttpEntity.class),
            eq(String.class)
        );
    }

    @Test
    void testSendLogFailure() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        when(event.getFormattedMessage()).thenReturn("Test log message");
        when(event.getLevel()).thenReturn(Level.INFO);

        when(restTemplate.postForObject(
            eq("https://api-logncrash.cloud.toast.com/v2/log"),
            any(HttpEntity.class),
            eq(String.class)
        )).thenThrow(new RuntimeException("API call failed"));

        appender.append(event);

        verify(restTemplate, times(1)).postForObject(
            eq("https://api-logncrash.cloud.toast.com/v2/log"),
            any(HttpEntity.class),
            eq(String.class)
        );
    }
}