package com.zmd.book.controller;

import com.zmd.book.dto.request.BookRequestDto;
import com.zmd.book.dto.response.BookResponseDto;
import com.zmd.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Books API", description = "Endpoints for managing books, including creation, retrieval, update, and deletion")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all books", description = "Returns a paginated list of books. Supports pagination and sorting")
    public Page<BookResponseDto> findAll(
            @ParameterObject
            @PageableDefault(sort = "title") Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a book by ID", description = "Returns the details of a book identified by the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookResponseDto.class)
            )),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters or validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "Book not found for the given ID", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    public ResponseEntity<BookResponseDto> findById(
            @Parameter(description = "Unique identifier of the book. Must be a positive number", required = true)
            @PathVariable @Positive Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Creates a new book with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookResponseDto.class)
            )),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters or validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "409", description = "A book with the same ISBN already exists", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto created = bookService.create(bookRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book", description = "Updates the book identified by the given ID")
    public ResponseEntity<BookResponseDto> update(@PathVariable @Positive Long id,
                                                  @Valid @RequestBody BookRequestDto bookRequestDto) {
        return ResponseEntity.ok(bookService.update(id, bookRequestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes the book identified by the given ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        bookService.delete(id);
    }

}
