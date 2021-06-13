package library.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import library.entities.Book;
import library.entities.IBook;
import library.entities.IBook.BookState;

@ExtendWith(MockitoExtension.class)
class BookTest {

    @Test
    void testIsAvailable() {
        IBook book = new Book("author", "title", "callNo", 100);
        boolean isAvailable = book.isAvailable();
        assertTrue(isAvailable);
    }

    //test borrow book that is available from library
    @Test
    void testBorrowFromLibrary() {
        IBook book = new Book("author", "title", "callNo", 100);
        book.borrowFromLibrary();
        assertEquals(BookState.ON_LOAN, book.getState());
    }
    
    //test borrow from library already on loan
    @Test
    void testBorrowFromLibraryOnLoan() {
        IBook book = new Book("author", "title", "callNo", 100);
        book.borrowFromLibrary();
        assertThrows(RuntimeException.class, () -> book.borrowFromLibrary());
    }

}
