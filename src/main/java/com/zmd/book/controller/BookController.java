package com.zmd.book.controller;

import com.zmd.book.dto.request.BookRequestDto;
import com.zmd.book.dto.response.BookResponseDto;
import com.zmd.book.service.BookService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Page<BookResponseDto> findAll(
            @ParameterObject
            @PageableDefault(sort = "title") Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<BookResponseDto> toDto(@Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto created = bookService.create(bookRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> update(@PathVariable Long id,
                                                  @Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto updated = bookService.update(id, bookRequestDto);
        return ResponseEntity.ok(updated);
    }

}
