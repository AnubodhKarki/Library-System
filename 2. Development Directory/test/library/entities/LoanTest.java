package library.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import library.entities.IBook;
import library.entities.IPatron;
import library.entities.Loan;
import library.entities.ILoan.LoanState;

@ExtendWith(MockitoExtension.class)
class LoanTest {
    @Mock
    private IBook mockBook;
    @Mock
    private IPatron mockPatron;

    @InjectMocks
    Loan loan;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void testCommitLoanOk() {
        // arrange
        Date dueDate = new Date();
        int loanId = 1;
        doNothing().when(mockPatron).takeOutLoan(loan);
        doNothing().when(mockBook).borrowFromLibrary();
        // act
        loan.commit(loanId, dueDate);

        // assert
        verify(mockPatron, times(1)).takeOutLoan(loan);
        verify(mockBook, times(1)).borrowFromLibrary();
    }

    @Test
    void checkOverDueDate() {
        // arrange: nothing
        // act
        boolean overDue = loan.checkOverDue(new Date());
        // assert
        assertFalse(overDue);

    }

    @Test
    void checkOverDueCurrentDateAfterDueDate() throws ParseException {
        // arrange
        doNothing().when(mockPatron).takeOutLoan(loan);
        doNothing().when(mockBook).borrowFromLibrary();
        String beforeDate = "2018-03-11";
        Date dueDate = FORMAT.parse(beforeDate);
        loan.commit(new Random().nextInt(), dueDate);
        assertEquals(LoanState.CURRENT, loan.getState());
        // act
        boolean overDue = loan.checkOverDue(new Date());

        // assert loan is overdue and state is updated as appropriate
        assertTrue(overDue);
        assertEquals(LoanState.OVER_DUE, loan.getState());

    }

}
