package com.hexad.raghu.web;

import com.hexad.raghu.domain.Book;
import com.hexad.raghu.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Transactional
@RequestMapping("library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @RequestMapping("/getAllBooks")
    public List<Book> selectAll() {

        List<Book> allBooks = libraryService.getAllBooks();
        return allBooks;
    }





}
