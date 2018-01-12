import static org.junit.Assert.*;

import org.junit.Test;

public class PriorityConvertorTest {

	@Test
	public void checkConversionsFromPriorityToString() {
		assertEquals("LOW", PriorityConvertor.convertPriorityToString(Priority.LOW));
		assertEquals("MEDIUM", PriorityConvertor.convertPriorityToString(Priority.MEDIUM));
		assertEquals("HIGH", PriorityConvertor.convertPriorityToString(Priority.HIGH));
		assertEquals("VIP", PriorityConvertor.convertPriorityToString(Priority.VIP));
	}
	
	@Test
	public void checkConversionsFromStringToPriority() {
		assertEquals(Priority.LOW, PriorityConvertor.convertStringToPriority("LOW"));
		assertEquals(Priority.MEDIUM, PriorityConvertor.convertStringToPriority("MEDIUM"));
		assertEquals(Priority.HIGH, PriorityConvertor.convertStringToPriority("HIGH"));
		assertEquals(Priority.VIP, PriorityConvertor.convertStringToPriority("VIP"));
	}

}
