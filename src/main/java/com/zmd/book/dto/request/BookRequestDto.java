package com.zmd.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookRequestDto {

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 255)
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(min = 1, max = 255)
    private String author;

    @NotBlank(message = "Isbn is mandatory")
    @Size(min = 10, max = 17, message = "Isbn must be between 10 and 17 characters")
    private String isbn;

    @NotNull(message = "Published Date is mandatory")
    private LocalDate publishedDate;

}
