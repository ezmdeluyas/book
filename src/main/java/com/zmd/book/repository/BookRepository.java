package com.zmd.book.repository;

import com.zmd.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
