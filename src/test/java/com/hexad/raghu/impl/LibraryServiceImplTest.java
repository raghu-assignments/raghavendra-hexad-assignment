package com.hexad.raghu.impl;

import com.hexad.raghu.domain.Book;
import com.hexad.raghu.domain.User;
import com.hexad.raghu.exception.BookBorrowingException;
import com.hexad.raghu.exception.UserNotFoundException;
import com.hexad.raghu.repository.LibraryRepository;
import com.hexad.raghu.repository.UserRepository;
import com.hexad.raghu.service.LibraryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class LibraryServiceImplTest {

    @InjectMocks
    LibraryService libraryService = new LibraryServiceImpl();

    @Mock
    LibraryRepository libraryRepository;

    @Mock
    UserRepository userRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    //1.1
    @Test
    public void should_return_empty_book_list_when_no_books_in_library() {
        //Given
        List<Book> books = new ArrayList<>();
        when(libraryRepository.findAll()).thenReturn(books);

        //When
        List<Book> allBooks = libraryService.getAllBooks();

        //Then
        verify(libraryRepository, times(1)).findAll();
        Assert.assertEquals(0, allBooks.size());


    }

    //1.2
    @Test
    public void should_return_book_list_when_books_available_in_library() {
        //Given
        Book book1 = new Book("Java", 101);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        when(libraryRepository.findAll()).thenReturn(books);

        //When
        List<Book> allBooks = libraryService.getAllBooks();

        //Then
        verify(libraryRepository, times(1)).findAll();
        assertEquals(books.size(), allBooks.size());

    }

    //2
    @Test
    public void should_add_book_to_borrower_list_when_book_is_chosen() throws BookBorrowingException {
        //Given
        Book book = new Book("JAVA", 101);
        User user = new User("Raghu", 101, Arrays.asList(new Book()));
        when(userRepository.findOne(user.getUserId())).thenReturn(user);

        //When
        libraryService.addBookToBorrowedList(Arrays.asList(book), user);

        //Then
        assertEquals(userRepository.findOne(user.getUserId()).getBorrowedBooks().contains(book), true);
        assertTrue(user.getBorrowedBooks().size() <= 2);
        assertEquals(0, libraryService.getBooks(book.getBookId()).size());
        assertEquals(0, libraryService.getAllBooks().size());
        verify(libraryRepository,times(1)).delete(Arrays.asList(book));
        // Max two books
        // no .of books in the library should reduce.
    }


    //3.1
    @Test
    public void should_add_only_one_copy_to_the_borrowerList_when_multiple_copies_available() throws BookBorrowingException {
        //Given
        Book book1 = new Book("JAVA", 101);
        Book book2 = new Book("JAVA", 101);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        User user = new User("RAGHU", 101, Arrays.asList(book1));
        when(libraryService.getAllBooks()).thenReturn(books);

        //When
        Boolean added = libraryService.addBookToBorrowedList(books, user);

        //Then
        assertFalse(added);


    }

    //3.2
    @Test
    public void should_add_final_copy_of_the_book_to_borrower_list_when_only_one_copy_is_available() throws BookBorrowingException {
        //Given
        Book book1 = new Book("JAVA", 101);
        User user = new User("RAGHU", 101, Arrays.asList(book1));

        //When
        Boolean added = libraryService.addBookToBorrowedList(Arrays.asList(book1), user);

        //Then
        assertFalse(added);
        assertFalse(libraryService.getAllBooks().contains(book1));

        // Book is removed from the library.
    }

    //4.1
    @Test
    public void should_update_the_borrowedList_and_libraryStock_when_the_user_returns_one_book() throws UserNotFoundException {

        //Given
        Book book1 = new Book("JAVA", 101);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        User user = new User("Raghu", 101, books);
        when(userRepository.findOne(101)).thenReturn(user);

        //When
        libraryService.returnBook(user.getUserId(), Arrays.asList(book1));

        //Then
        verify(libraryRepository, times(1)).save(book1);

    }

    //4.2
    @Test
    public void should_update_the_borrowedList_and_libraryStock_when_the_user_returns_both_the_books() throws UserNotFoundException {

        //Given
        Book book1 = new Book("JAVA", 101);
        Book book2 = new Book("React JA", 101);
        Book book3 = new Book("React JA", 101);
        List<Book> existingBooks = new ArrayList<>();
        existingBooks.add(book3);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        User user = new User("Raghu", 101, books);
        when(userRepository.findOne(101)).thenReturn(user);

        //When
        libraryService.returnBook(user.getUserId(), books);

        //Then
        verify(libraryRepository, times(1)).save(books);

    }
}
