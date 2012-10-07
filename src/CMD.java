
class CMD {

	public int type;
	
	enum COMMAND_TYPE {
		ADD, DELETE, UNDO, DISPLAY, EDIT
	};
	
	String commandString = getFirstWord(userCommand);
	
	COMMAND_TYPE commandType = determineCommandType(commandString);
	
	public COMMAND_TYPE determineCommandType(String commandString) {
		if (commandString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		}
		else if (commandString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		}
		else if (commandString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		}
		else if (commandString.equalsIgnoreCase("undo")) {
			return COMMAND_TYPE.UNDO;
		}
		else if (commandString.equalsIgnoreCase("edit")) {
			return COMMAND_TYPE.EDIT;
		}
	
}
