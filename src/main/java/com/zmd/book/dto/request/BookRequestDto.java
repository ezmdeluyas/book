package com.zmd.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record BookRequestDto (

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255)
    String title,

    @NotBlank(message = "Author is mandatory")
    @Size(max = 255)
    String author,

    @NotBlank(message = "ISBN is mandatory")
    @Size(min = 10, max = 17, message = "ISBN must be between 10 and 17 characters")
    String isbn,

    @NotNull(message = "Published date is mandatory")
    LocalDate publishedDate
) {}
