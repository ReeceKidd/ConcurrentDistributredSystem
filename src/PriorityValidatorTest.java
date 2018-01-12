import static org.junit.Assert.*;

import org.junit.Test;

public class PriorityValidatorTest {

	@Test
	public void checkValidValues() {
		assertEquals(true, PriorityValidator.isPriorityValid("LOW"));
		assertEquals(true, PriorityValidator.isPriorityValid("MEDIUM"));
		assertEquals(true, PriorityValidator.isPriorityValid("HIGH"));
		assertEquals(true, PriorityValidator.isPriorityValid("VIP"));
	}
	
	@Test
	public void checkInvalidValues(){
		assertEquals(false, PriorityValidator.isPriorityValid(null));
		assertEquals(false, PriorityValidator.isPriorityValid("MED"));
		assertEquals(false, PriorityValidator.isPriorityValid(" "));
		assertEquals(false, PriorityValidator.isPriorityValid("TEST"));
	}
	
	
	@Test
	public void checkCaseSensitivity() {
		assertEquals(true, PriorityValidator.isPriorityValid("HIGH"));
		assertEquals(true, PriorityValidator.isPriorityValid("high"));
	}

}
