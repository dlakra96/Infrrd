package com.infrrd.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details."
        );
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler({BadRegexException.class})
    public ResponseEntity<CustomErrorResponse> handleBadRegexException(BadRegexException exception, WebRequest request){
        logger.info("exception type :- {}",exception.getClass());
        logger.info("excpetion message :- {}",exception.getMessage());
        return buildErrorResponse(exception,HttpStatus.BAD_REQUEST,request);
    }

    @ExceptionHandler({RegexProcessingThreadException.class})
    public ResponseEntity<CustomErrorResponse> handleRegexThreadProcessingException(RegexProcessingThreadException exception, WebRequest request){
        logger.info("exception type :- {}",exception.getClass());
        logger.info("excpetion message :- {}",exception.getMessage());
        return buildErrorResponse(exception,HttpStatus.INTERNAL_SERVER_ERROR,request);
    }

    private ResponseEntity<CustomErrorResponse> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                httpStatus,
                request);
    }

    private ResponseEntity<CustomErrorResponse> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                httpStatus.value(),
                exception.getMessage()
        );
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

}
