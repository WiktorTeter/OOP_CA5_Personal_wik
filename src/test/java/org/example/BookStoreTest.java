package org.example;

import Server.example.DAOs.MySqlBooksDao;
import Server.example.DTOs.Book;
import Server.example.Exceptions.DaoException;
import Server.example.org.BookStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookStoreTest {
    @org.junit.jupiter.api.Test
    void testFindAllBooks() throws DaoException {
        BookStore bookStore = new BookStore();
        List<Book> books = BookStore.findAllBooks();
        assertEquals(3, books.size());
    }

    @org.junit.jupiter.api.Test
    void testGetBookByID() throws DaoException {
        BookStore bookStore = new BookStore();
        Book book = bookStore.getBookByID(1);
        assertEquals("Java", book.getTitle());
    }

    @org.junit.jupiter.api.Test
    void testInsertBook() throws DaoException {
        BookStore bookStore = new BookStore();
        bookStore.insertBook(4, "Python", "Guido van Rossum", 50.0f);
        List<Book> books = bookStore.findAllBooks();
        assertEquals(8, books.size());
    }

    @org.junit.jupiter.api.Test
    void testDeleteBookByID() throws DaoException {
        BookStore bookStore = new BookStore();
        bookStore.deleteBookByID(4);
        List<Book> books = bookStore.findAllBooks();
        assertEquals(7, books.size());
    }

    @org.junit.jupiter.api.Test
    void testUpdateBookByID() throws DaoException {
        BookStore bookStore = new BookStore();
        bookStore.updateBookByID(1, "Java", "James Gosling", 40.0f);
        Book book = bookStore.getBookByID(1);
        assertEquals("James Gosling", book.getAuthor());
    }

    @org.junit.jupiter.api.Test
    void testGetBookByFilter() {
        BookStore bookStore = new BookStore();
        Book filter = new Book();
        filter.setTitle("Java");
        List<Book> books = bookStore.getBookByFilter(filter);
        assertEquals(1, books.size());
    }


    @org.junit.jupiter.api.Test
    void testGetBookByFilter2() {
        BookStore bookStore = new BookStore();
        Book filter = new Book();
        filter.setAuthor("James Gosling");
        List<Book> books = bookStore.getBookByFilter(filter);
        assertEquals(1, books.size());
    }


    @org.junit.jupiter.api.Test
    void testGetBookByFilter3() {
        BookStore bookStore = new BookStore();
        Book filter = new Book();
        filter.setPrice(String.valueOf(40.0)); // the price variable is set as float but for some reason it expects it as a string.
        List<Book> books = bookStore.getBookByFilter(filter);
        assertEquals(1, books.size());
    }

    @org.junit.jupiter.api.Test
    void testFindAllBooksAndConvertToJSON() throws DaoException {
        BookStore bookStore = new BookStore();
        List<Book> books = bookStore.findAllBooks();
        String json = ((MySqlBooksDao) BookStore.userDao).convertListToJSON(books);
        assertEquals("[{\"id\":1,\"title\":\"Java\",\"author\":\"James Gosling\",\"price\":40.0},{\"id\":2,\"title\":\"C++\",\"author\":\"Bjarne Stroustrup\",\"price\":30.0},{\"id\":3,\"title\":\"C#\",\"author\":\"Anders Hejlsberg\",\"price\":35.0}]", json);
    }

    @org.junit.jupiter.api.Test
    void testConvertBookByIDToJSON() throws DaoException {
        BookStore bookStore = new BookStore();
        Book book = bookStore.getBookByID(1);
        String json = ((MySqlBooksDao) BookStore.userDao).convertEntityToJSON(book);
        assertEquals("{\"id\":1,\"title\":\"Java\",\"author\":\"James Gosling\",\"price\":40.0}", json);
    }




}