import java.util.Hashtable;

public class Processor {
	
	private Hashtable <String, String> reservedWords;
	private static final String ERROR_MSG_INVALID_INPUT = "Invalid Input.";
	
	
	public Processor(){
		//load txt file with reserved words into Hashtable
		
	}
	
	
	public CMD translateToCMD(String userInput) {
		
		String[] temp = userInput.split(" ");
		if(!reservedWords.contains(temp[0])) {
			
			return (new CMD(COMMAND_TYPE.ERROR, ERROR_MSG_INVALID_INPUT));
		}
		else{
			String cmd = reservedWords.get(temp[0]);
			COMMAND_TYPE userCMD = determineCommandType(cmd);
			
			switch(userCMD){
				case ADD:
					break;
//				case EDIT:
//				case REMOVE:
				case UNDO: return new CMD(userCMD, null);
				case QUIT: return new CMD(userCMD, null);	
				default:
					break;
			}
		}
		
		return null;		//to be removed
	}

	enum COMMAND_TYPE {
		ADD, DELETE, UNDO, DISPLAY, EDIT, QUIT, ERROR
	};
	
	private COMMAND_TYPE determineCommandType(String commandString) {
		if (commandString.equalsIgnoreCase(".add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandString.equalsIgnoreCase(".delete")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandString.equalsIgnoreCase(".display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandString.equalsIgnoreCase(".undo")) {
			return COMMAND_TYPE.UNDO;
		} else if (commandString.equalsIgnoreCase(".edit")) {
			return COMMAND_TYPE.EDIT;
		} else if (commandString.equalsIgnoreCase(".quit")) {
			return COMMAND_TYPE.QUIT;
		} else
			return COMMAND_TYPE.ERROR;
	}
}
