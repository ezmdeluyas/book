package com.zmd.book.service.impl;

import com.zmd.book.dto.request.BookRequestDto;
import com.zmd.book.dto.response.BookResponseDto;
import com.zmd.book.entity.Book;
import com.zmd.book.mapper.BookMapper;
import com.zmd.book.repository.BookRepository;
import com.zmd.book.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,  BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        Book created = bookMapper.toEntity(bookRequestDto);
        Book saved = bookRepository.save(created);
        return bookMapper.toDto(saved);
    }

}
