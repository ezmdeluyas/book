package com.zmd.book.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<ValidationError> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationError(
                        err.getField(),
                        err.getDefaultMessage(),
                        err.getCode()
                ))
                .toList();

        return buildProblemDetail(
                request,
                HttpStatus.BAD_REQUEST,
                ApiProblem.VALIDATION_ERROR,
                errors
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        String expectedType = Optional.ofNullable(exception.getRequiredType())
                .map(Class::getSimpleName)
                .orElse("Unknown");
        List<ValidationError> errors = List.of(
                new ValidationError(
                        exception.getName(),
                        "Value '" + exception.getValue() + "' must be of type " + expectedType,
                        ApiProblem.TYPE_MISMATCH.name()
                ));
        return buildProblemDetail(
                request,
                HttpStatus.BAD_REQUEST,
                ApiProblem.TYPE_MISMATCH,
                errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        List<ValidationError> errors = exception.getConstraintViolations()
                .stream()
                .map(v -> new ValidationError(
                        extractParameterName(v.getPropertyPath().toString()),
                        v.getMessage(),
                        v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()))
                .toList();

        return buildProblemDetail(
                request,
                HttpStatus.BAD_REQUEST,
                ApiProblem.VALIDATION_ERROR,
                errors
        );
    }

    @ExceptionHandler(value = BookNotFoundException.class)
    public ProblemDetail handleBookNotFound(
            BookNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildProblemDetail(
                request,
                HttpStatus.NOT_FOUND,
                ApiProblem.NOT_FOUND,
                exception.getMessage()
        );
    }

    @ExceptionHandler(value = DuplicateIsbnException.class)
    public ProblemDetail handleDuplicateIsbn(
            DuplicateIsbnException exception,
            HttpServletRequest request
    ) {
        return buildProblemDetail(
                request,
                HttpStatus.CONFLICT,
                ApiProblem.CONFLICT,
                exception.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        return buildProblemDetail(
                request,
                HttpStatus.CONFLICT,
                ApiProblem.CONFLICT,
                ApiProblem.CONFLICT.getDetail()
        );
    }

    private ProblemDetail buildProblemDetail(
            HttpServletRequest request,
            HttpStatus status,
            ApiProblem apiProblem,
            String detailOverride,
            List<ValidationError> errors
    ) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(apiProblem.getTitle());
        problemDetail.setType(apiProblem.getType());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", apiProblem.name());

        if (StringUtils.isNotBlank(detailOverride)) {
            problemDetail.setDetail(detailOverride);
        } else {
            problemDetail.setDetail(apiProblem.getDetail());
        }

        if (errors != null &&  !errors.isEmpty()) {
            problemDetail.setProperty("errors", errors);
        }

        return problemDetail;
    }

    private ProblemDetail buildProblemDetail(
            HttpServletRequest request,
            HttpStatus status,
            ApiProblem apiProblem,
            List<ValidationError> errors
    ) {
        return buildProblemDetail(request, status, apiProblem, null, errors);
    }

    private ProblemDetail buildProblemDetail(
            HttpServletRequest request,
            HttpStatus status,
            ApiProblem apiProblem,
            String message
    ) {
        return buildProblemDetail(request, status, apiProblem, message, null);
    }

    private String extractParameterName(String propertyPath) {
        int lastDot = propertyPath.lastIndexOf('.');
        return lastDot != -1 ?  propertyPath.substring(lastDot + 1) : propertyPath;
    }

}
