package com.zmd.book.exception;

import lombok.Getter;

import java.net.URI;

@Getter
public enum ApiProblem {

    NOT_FOUND(
            "Resource not found",
            "The request resource does not exist",
            URI.create("https://api.zmd.com/problems/not-found")
    ),

    CONFLICT(
            "Conflict",
            "The request could not be completed due to a conflict with the current state of the resource",
            URI.create("https://api.zmd.com/problems/conflict")
    ),

    VALIDATION_ERROR(
            "Validation Failed",
            "One or more request values are invalid",
            URI.create("https://api.zmd.com/problems/validation-error")
    ),

    TYPE_MISMATCH(
            "Parameter Type Mismatch",
            "One or more request parameters have the wrong type",
            URI.create("https://api.zmd.com/problems/type-mismatch")
    );

    private final String title;
    private final String detail;
    private final URI type;

    ApiProblem(String title, String detail, URI type) {
        this.title = title;
        this.detail = detail;
        this.type = type;
    }

}
