package com.nhnacademy.orderpaymentrefund.exception.handle;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;
import com.nhnacademy.orderpaymentrefund.exception.type.ForbiddenExceptionType;
import com.nhnacademy.orderpaymentrefund.exception.type.NotFoundExceptionType;
import com.nhnacademy.orderpaymentrefund.exception.type.UnauthorizedExceptionType;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExceptionRestControllerAdviceTest {

    @InjectMocks
    private ExceptionRestControllerAdvice advice;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testHandleBadRequestException()  {
        BadRequestExceptionType exception = new BadRequestExceptionType("Bad request");

        when(response.getStatus()).thenReturn(HttpStatus.BAD_REQUEST.value());

        String result = advice.handleBadRequestException(exception, response);
        assertEquals("Bad request", result);
    }

    @Test
    void testHandleNotFoundException()  {
        NotFoundExceptionType exception = new NotFoundExceptionType("Not found");

        when(response.getStatus()).thenReturn(HttpStatus.NOT_FOUND.value());

        String result = advice.handleBadRequestException(exception, response);
        assertEquals("Not found", result);
    }

    @Test
    void testHandleUnauthorizedException()  {
        UnauthorizedExceptionType exception = new UnauthorizedExceptionType("Unauthorized");

        when(response.getStatus()).thenReturn(HttpStatus.UNAUTHORIZED.value());

        String result = advice.handleBadRequestException(exception, response);
        assertEquals("Unauthorized", result);
    }

    @Test
    void testHandleForbiddenException()  {
        ForbiddenExceptionType exception = new ForbiddenExceptionType("Forbidden");

        when(response.getStatus()).thenReturn(HttpStatus.FORBIDDEN.value());

        String result = advice.handleForbiddenException(exception, response);
        assertEquals("Forbidden", result);
    }
}
