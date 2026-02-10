package com.zmd.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "books",
        indexes = {
                @Index(name = "uq_books_isbn_idx", columnList = "isbn", unique = true)
        }
)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @Size(max = 255)
    @Column(nullable = false)
    private String title;

    @Size(max = 255)
    @Column(nullable = false)
    private String author;

    @Size(min = 10, max = 17)
    @Column(nullable = false)
    private String isbn;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    public Book(String title, String author, String isbn, LocalDate publishedDate) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
    }

}
