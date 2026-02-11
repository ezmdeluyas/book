package com.zmd.book.service;

import com.zmd.book.dto.request.BookRequestDto;
import com.zmd.book.dto.response.BookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<BookResponseDto> findAll(Pageable pageable);
    BookResponseDto findById(Long id);
    BookResponseDto create(BookRequestDto bookRequestDto);
    BookResponseDto update(Long id, BookRequestDto bookRequestDto);
    void delete(Long id);
}
