package com.example.demo.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, String> {

    Page<BookEntity> findAllByIsbnLikeOrTitleLike(String isbn, String title, Pageable pageable);

}
