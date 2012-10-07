class CMD {

	private COMMAND_TYPE commandType;
	private Object data;

	public CMD(String command, Object data) {
		setCommandType(determineCommandType(command));
		this.setData(data);
	}
	
	enum COMMAND_TYPE {
		ADD, DELETE, UNDO, DISPLAY, EDIT, ERROR
	};

	private COMMAND_TYPE determineCommandType(String commandString) {
		if (commandString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandString.equalsIgnoreCase("undo")) {
			return COMMAND_TYPE.UNDO;
		} else if (commandString.equalsIgnoreCase("edit")) {
			return COMMAND_TYPE.EDIT;
		} else
			return COMMAND_TYPE.ERROR;

	}

	public COMMAND_TYPE getCommandType() {
		return commandType;
	}

	public void setCommandType(COMMAND_TYPE commandType) {
		this.commandType = commandType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
