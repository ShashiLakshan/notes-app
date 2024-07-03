package com.my.code.notes_app.advisor;

import com.my.code.notes_app.dto.StatusResponse;
import com.my.code.notes_app.exception.CustomGlobalException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

    @ExceptionHandler({CustomGlobalException.class})
    public ResponseEntity<Object> handleCustomGlobalException(CustomGlobalException e, WebRequest request) {
        log.error("Error {}/{}", e.getCode(), e.getMessage());
        StatusResponse statusResponse = StatusResponse.builder().code(e.getCode()).message(e.getMessage()).build();
        return new ResponseEntity<>(statusResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e, WebRequest request) {
        log.error("Error occurred due to : ()", e);
        StatusResponse statusResponse = StatusResponse.builder()
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + " from " + request)
                .build();
        return new ResponseEntity<>(statusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMException(MethodArgumentNotValidException e, WebRequest request) {
        StatusResponse statusResponse = StatusResponse.builder().code("APP_400")
                .message(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()).build();
        return new ResponseEntity<>(statusResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        List<StatusResponse> statusResponseList = buildValidationErrors(e.getConstraintViolations());
        return new ResponseEntity<>(statusResponseList, HttpStatus.BAD_REQUEST);
    }

    private List<StatusResponse> buildValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(violation -> StatusResponse.builder()
                        .message(constructMessage(violation))
                        .code("APP_400")
                        .build()).toList();
    }

    private String constructMessage(ConstraintViolation<?> violation) {
        if (violation.getMessageTemplate().startsWith("{") && violation.getMessageTemplate().endsWith("}")) {
            return StringUtils.capitalize(
                    Objects.requireNonNull(StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                            .reduce((first, second) -> second).orElse(null)).toString()) + " " + violation.getMessage();
        }
        return violation.getMessage();
    }

}
