package library.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import library.borrowbook.BorrowBookControl;
import library.borrowbook.BorrowBookUI;
import library.borrowbook.IBorrowBookControl;
import library.borrowbook.IBorrowBookControl.BorrowControlState;
import library.borrowbook.IBorrowBookUI;
import library.borrowbook.IBorrowBookUI.BorrowUIState;
import library.entities.helpers.BookHelper;
import library.entities.helpers.LoanHelper;
import library.entities.helpers.PatronHelper;

class BorrowBookIntegrationTest {

	ILibrary library;
	IBorrowBookControl control;

	@Test
	void testCardSwipe() {
		library = new Library(new BookHelper(), new PatronHelper(), new LoanHelper());
		control = new BorrowBookControl(library);
		IBorrowBookUI borrowBookUI = new BorrowBookUI(control);
		IPatron patron = library.addPatron("karki", "anubodh", "test@test.com", 123);

		control.cardSwiped(patron.getId());

		assertEquals(BorrowUIState.SCANNING, borrowBookUI.getState());
		assertEquals(BorrowControlState.SCANNING, control.getState());
	}

}
