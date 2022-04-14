package com.jjrepos.profileimage.api.exception;

import com.jjrepos.atom.api.security.NotAuthorizedException;
import com.jjrepos.profileimage.api.representation.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        ErrorMessage error = new ErrorMessage("Bad Request", Objects.requireNonNull(ex.getRootCause()).getMessage());
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConversionFailedException.class})
    public ResponseEntity<Object> handleConversionFailedException(ConversionFailedException ex, WebRequest request) {
        ErrorMessage error = new ErrorMessage("Bad Request", ex.getMessage());
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorMessage error = new ErrorMessage("Bad Request", ex.getMessage());
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorMessage error = new ErrorMessage("Bad Request", ex.getMessages());
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NotAuthorizedException.class})
    public ResponseEntity<Object> handleNoUserContextException(Exception ex, WebRequest request) {
        ErrorMessage error = new ErrorMessage("Unauthorized access", ex.getMessage());
        return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleAnyRunTimeException(RuntimeException ex, WebRequest request) {
        LOG.error("Internal server error", ex);
        ErrorMessage error = new ErrorMessage("Internal Server Error", ex.getMessage());
        return new ResponseEntity<Object>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
