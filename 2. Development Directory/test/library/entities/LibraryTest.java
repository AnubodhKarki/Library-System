package library.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import library.entities.helpers.IBookHelper;
import library.entities.helpers.ILoanHelper;
import library.entities.helpers.IPatronHelper;

@ExtendWith(MockitoExtension.class)
class LibraryTest {
    @Mock
    private IBookHelper mockBookHelper;
    @Mock
    private IPatronHelper mockPatronHelper;
    @Mock
    private ILoanHelper mockLoanHelper;
    @Mock
    private ILoan loanMock;
    @Mock
    private IBook bookMock;
    @Mock
    private IPatron patronMock;

    @InjectMocks
    Library library;

    // testing patron without any restriction can borrow book
    @Test
    void testPatronCanBorrowOK() {
        // arrange: nothing
        // act
        boolean patronCanBorrow = library.patronCanBorrow(patronMock);
        // assert
        assertTrue(patronCanBorrow);
    }

    // testing patron may not borrow further books if they have overdue loans
    @Test
    void testPatronCanBorrowOverdueLoan() {
        // arrange
        when(patronMock.hasOverDueLoans()).thenReturn(true);
        // act
        boolean patronCanBorrow = library.patronCanBorrow(patronMock);
        // assert
        assertFalse(patronCanBorrow);
    }

    // testing patron may not if they have already borrowed the maximum number of
    // books permissible
    @Test
    void testPatronCanBorrowMaxBook() {
        // arrange
        when(patronMock.getNumberOfCurrentLoans()).thenReturn(2);
        // act
        boolean patronCanBorrow = library.patronCanBorrow(patronMock);
        // assert
        assertFalse(patronCanBorrow);
    }

    // testing patron may not borrow if they owe more than a threshold value of
    // fines payable
    @Test
    void testPatronCanBorrowFines() {
        // arrange
        when(patronMock.getFinesPayable()).thenReturn(23.2);
        // act
        boolean patronCanBorrow = library.patronCanBorrow(patronMock);
        // assert
        assertFalse(patronCanBorrow);
    }

    // testing commit loan
    @Test
    void testCommitLoanOk() {
        // arrange
        doNothing().when(loanMock).commit(anyInt(), any(Date.class));
        when(loanMock.getPatron()).thenReturn(patronMock);
        when(loanMock.getBook()).thenReturn(bookMock);
        when(bookMock.getId()).thenReturn(1);

        // act
        library.commitLoan(loanMock);

        // assert
        verify(loanMock, times(1)).commit(anyInt(), any(Date.class));
        assertEquals(1, library.getCurrentLoansList().size());
    }

    // testing issue loan
    @Test
    void testIssueLoanOk() {
        // arrange
        when(mockLoanHelper.makeLoan(bookMock, patronMock)).thenReturn(loanMock);

        // act
        ILoan issuedLoan = library.issueLoan(bookMock, patronMock);

        // assert
        verify(mockLoanHelper, times(1)).makeLoan(bookMock, patronMock);
        assertNotNull(issuedLoan);
    }
}
