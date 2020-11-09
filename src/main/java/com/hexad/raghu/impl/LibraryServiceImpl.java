package com.hexad.raghu.impl;

import com.hexad.raghu.domain.Book;
import com.hexad.raghu.domain.User;
import com.hexad.raghu.exception.BookBorrowingException;
import com.hexad.raghu.exception.UserNotFoundException;
import com.hexad.raghu.repository.LibraryRepository;
import com.hexad.raghu.repository.UserRepository;
import com.hexad.raghu.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private UserRepository userRepository;

    public LibraryServiceImpl() {
    }


    @Override
    public List<Book> getAllBooks() {

        List<Book> allBooks = libraryRepository.findAll();

        return allBooks;

    }

    @Override
    public List<Book> getBooks(int bookId) {
        List<Book> booksById = libraryRepository.getBooksById(bookId);
        return booksById;
    }

    @Override
    public Boolean addBookToBorrowedList(List<Book> books, User user) throws BookBorrowingException {

        if (books.size() > 2) {
            throw new BookBorrowingException("Cannot Borrow more than two books.");
        }

        if (user.getBorrowedBooks().size() == 2) {
            throw new BookBorrowingException("User already borrowed two books, cannot borrow further.");
        }
        List<String> existingBookIds = new ArrayList<>();
        List<Book> borrowedBooks = user.getBorrowedBooks();
        for (Book borrowedBook : borrowedBooks) {
            existingBookIds.add(borrowedBook.getBookName());
        }
        for (Book book : books) {

            if (existingBookIds.contains(book.getBookName())) {
                return false;
            }
            user.setBorrowedBooks(Arrays.asList(book));
            userRepository.save(user);
            removeBookFromLibrary(books);
        }
        return true;
    }



    @Override
    public void addBookToLibrary(List<Book> books) {
        libraryRepository.save(books);
    }

    @Override
    public void removeBookFromLibrary(List<Book> books) {
        libraryRepository.delete(books);
    }

    @Override
    public Boolean returnBook(Integer userId, List<Book> books) throws UserNotFoundException {
        User userFromDB = userRepository.findOne(userId);
        if (userFromDB == null) {
            throw new UserNotFoundException("User Not Found");
        }

        List<Book> borrowedBooks = userFromDB.getBorrowedBooks();

        if (books.size() == 1 && borrowedBooks.contains(books.get(0))) {
            Book book = books.get(0);
            borrowedBooks.remove(book);
            userFromDB.setBorrowedBooks(borrowedBooks);
            userRepository.save(userFromDB);
            libraryRepository.save(books.get(0));
            addBookToLibrary(Arrays.asList(books.get(0)));
            return true;


        } else if (borrowedBooks.size() == 2 && borrowedBooks.containsAll(books)) {
            userFromDB.setBorrowedBooks(new ArrayList<>());
            userRepository.save(userFromDB);
            addBookToLibrary(books);
            return true;
        }
         throw new UserNotFoundException("Invalid return request");
    }


}
