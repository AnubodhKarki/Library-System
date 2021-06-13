package library.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import library.entities.ILoan;
import library.entities.Patron;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatronTest {
    @Mock
    private ILoan loanMock;

    @InjectMocks
    Patron patron = new Patron("karki", "anubodh", "test@email.com", 1234567, 100);

    @Test
    void testTakeOutLoan() {
        patron.takeOutLoan(loanMock);
        assertEquals(1, patron.getNumberOfCurrentLoans());
    }

    @Test
    void testTakeOutDuplicateLoan() {
        when(loanMock.getId()).thenReturn(1);
        patron.takeOutLoan(loanMock);
        assertThrows(RuntimeException.class, () -> patron.takeOutLoan(loanMock));
    }

    @Test
    void testHasOverDueLoans() {
        patron.takeOutLoan(loanMock);
        when(loanMock.isOverDue()).thenReturn(true);
        boolean hasOverdueLoans = patron.hasOverDueLoans();
        assertTrue(hasOverdueLoans);
        verify(loanMock, times(1)).isOverDue();
    }

}
