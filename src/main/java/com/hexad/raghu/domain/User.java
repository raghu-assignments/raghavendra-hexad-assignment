package com.hexad.raghu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class User {

    @Column(name = "UserName")
    private String userName;
    @Id
    @Column(name = "UserId")
    private int userId;

    @OneToMany
    @Column(name = "Books")
    private List<Book> borrowedBooks;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public User() {
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public User(String userName, int userId, List<Book> borrowedBooks) {
        this.userName = userName;
        this.userId = userId;
        this.borrowedBooks = borrowedBooks;
    }
}
