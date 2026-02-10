package com.zmd.book.dto.response;

import java.time.LocalDate;

public record BookResponseDto (
        Long id,
        String title,
        String author,
        String isbn,
        LocalDate publishedDate
) {}
