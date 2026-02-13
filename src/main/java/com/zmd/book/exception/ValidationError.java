package com.zmd.book.exception;

public record ValidationError(String field, String message, String code) {}
