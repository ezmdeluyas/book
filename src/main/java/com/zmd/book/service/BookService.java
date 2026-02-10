package com.zmd.book.service;

import com.zmd.book.dto.request.BookRequestDto;
import com.zmd.book.dto.response.BookResponseDto;

public interface BookService {
    BookResponseDto create(BookRequestDto bookRequestDto);
}
