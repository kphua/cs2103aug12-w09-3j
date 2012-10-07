
class CMD {
	
	private Processor.COMMAND_TYPE commandType;
	private Object data;

	public CMD(Processor.COMMAND_TYPE command, Object data) {
		commandType = command;
		this.setData(data);
	}

	public Processor.COMMAND_TYPE getCommandType() {
		return commandType;
	}

	public void setCommandType(Processor.COMMAND_TYPE commandType) {
		this.commandType = commandType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
