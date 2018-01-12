
public class PriorityValidator {

	public static boolean isPriorityValid(String userInput) {

		try {
			
		if(userInput.equals(" ")){
			System.out.println("ERROR: A blank space is not a valid priority type");
			System.out.println("Please use either ['LOW', 'MEDIUM', 'HIGH', 'VIP' for client priority");
			return false;
		}
		
		String userInputUpperCase = userInput.toUpperCase();
		

		String validInputs[] = { "LOW", "MEDIUM", "HIGH", "VIP" };

		for (String check : validInputs) {
			if (userInputUpperCase.equals(check)) {
				return true;
			}
		}
		System.out.println("ERROR: " + userInput + " is not a valid Priority type");
		System.out.println("Please use either ['LOW', 'MEDIUM', 'HIGH', 'VIP' for client priority");
		return false;
		
	} catch (NullPointerException e) {
		System.out.println("ERROR: 'null' is not a valid Priority type");
		System.out.println("Please use either ['LOW', 'MEDIUM', 'HIGH', 'VIP' for client priority");
		return false;
	}	
}
}
