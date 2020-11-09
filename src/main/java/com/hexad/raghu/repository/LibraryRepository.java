package com.hexad.raghu.repository;

import com.hexad.raghu.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<Book, Long> {

    List<Book> getBooksById(int bookId);


}
