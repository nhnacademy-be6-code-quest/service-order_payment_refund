package com.nhnacademy.orderpaymentrefund.exception.handle;

import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;
import com.nhnacademy.orderpaymentrefund.exception.type.ForbiddenExceptionType;
import com.nhnacademy.orderpaymentrefund.exception.type.NotFoundExceptionType;
import com.nhnacademy.orderpaymentrefund.exception.type.UnauthorizedExceptionType;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionRestControllerAdvice {

    @ExceptionHandler(ParseException.class)
    public String handleException(ParseException exception, HttpServletResponse response){
        return handleException(exception, response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestExceptionType.class)
    public String handleBadRequestException(BadRequestExceptionType exception, HttpServletResponse response) {
        return handleException(exception, response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundExceptionType.class)
    public String handleBadRequestException(NotFoundExceptionType exception, HttpServletResponse response) {
        return handleException(exception, response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedExceptionType.class)
    public String handleBadRequestException(UnauthorizedExceptionType exception, HttpServletResponse response) {
        return handleException(exception, response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenExceptionType.class)
    public String handleForbiddenException(ForbiddenExceptionType exception, HttpServletResponse response) {
        return handleException(exception, response, HttpStatus.FORBIDDEN);
    }

    private String handleException(RuntimeException e, HttpServletResponse response, HttpStatus httpStatus){
        response.setStatus(httpStatus.value());
        return e.getMessage();
    }

    private String handleException(Exception e, HttpServletResponse response, HttpStatus httpStatus){
        response.setStatus(httpStatus.value());
        return e.getMessage();
    }

}
