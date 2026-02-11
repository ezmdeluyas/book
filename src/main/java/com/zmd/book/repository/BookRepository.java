package com.zmd.book.repository;

import com.zmd.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
