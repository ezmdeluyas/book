package com.zmd.book.service.impl;

import com.zmd.book.dto.request.BookRequestDto;
import com.zmd.book.dto.response.BookResponseDto;
import com.zmd.book.entity.Book;
import com.zmd.book.exception.BookNotFoundException;
import com.zmd.book.exception.DuplicateIsbnException;
import com.zmd.book.mapper.BookMapper;
import com.zmd.book.repository.BookRepository;
import com.zmd.book.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional(readOnly = true)
    public Page<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    @Override
    public BookResponseDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        if (bookRepository.existsByIsbn(bookRequestDto.isbn())) {
            throw new DuplicateIsbnException(bookRequestDto.isbn());
        }
        Book created = bookMapper.toEntity(bookRequestDto);
        Book saved = bookRepository.save(created);
        return bookMapper.toDto(saved);
    }

    @Override
    public BookResponseDto update(Long id, BookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        String newIsbn = bookRequestDto.isbn();
        if (!book.getIsbn().equals(newIsbn)
                && bookRepository.existsByIsbnAndIdNot(newIsbn, id)) {
            throw new DuplicateIsbnException(bookRequestDto.isbn());
        }

        book.setTitle(bookRequestDto.title());
        book.setAuthor(bookRequestDto.author());
        book.setIsbn(bookRequestDto.isbn());
        book.setPublishedDate(bookRequestDto.publishedDate());

        return bookMapper.toDto(book);
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }

}
