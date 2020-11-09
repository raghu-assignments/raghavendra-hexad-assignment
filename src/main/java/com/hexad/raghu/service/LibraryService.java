package com.hexad.raghu.service;

import com.hexad.raghu.domain.Book;
import com.hexad.raghu.exception.BookBorrowingException;
import com.hexad.raghu.domain.User;
import com.hexad.raghu.exception.UserNotFoundException;

import java.util.List;


public interface LibraryService {

    List<Book> getAllBooks();

    List<Book> getBooks(int bookId);

    Boolean addBookToBorrowedList(List<Book> books, User user) throws BookBorrowingException;

    void addBookToLibrary(List<Book> books);

    void removeBookFromLibrary(List<Book> books);

    Boolean returnBook(Integer userId, List<Book> bookIds) throws UserNotFoundException;


}
