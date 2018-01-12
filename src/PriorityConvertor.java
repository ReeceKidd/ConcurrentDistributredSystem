/*
 * This class converts a String input to a priority Level. It was necessary to do this was as the run configurations would not accept a Priority Enum Type
 */
public class PriorityConvertor {

	public static Priority convertStringToPriority(String initialPriority){
		
		if(initialPriority.equals("LOW")){
			return Priority.LOW;
		}
		
		if(initialPriority.equals("MEDIUM")){
			return Priority.MEDIUM;
		}
		
		if(initialPriority.equals("HIGH")){
			return Priority.HIGH;
		}
		
		if(initialPriority.equals("VIP")){
			return Priority.VIP;
		}
		
		return Priority.LOW;
	}
	
public static String convertPriorityToString(Priority initialPriority){
		
		if(initialPriority.equals(Priority.LOW)){
			return "LOW";
		}
		
		if(initialPriority.equals(Priority.MEDIUM)){
			return "MEDIUM";
		}
		
		if(initialPriority.equals(Priority.HIGH)){
			return "HIGH";
		}
		
		if(initialPriority.equals(Priority.VIP)){
			return "VIP";
		}
		
		return "LOW";
	}

}
